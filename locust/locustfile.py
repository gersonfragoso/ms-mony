from locust import HttpUser, task, between
from faker import Faker
import random

fake = Faker()

class MyServiceUser(HttpUser):
    wait_time = between(1, 3)  # Tempo entre as requisições
    token = None
    user_data = None  # Dados do usuário para login

    # URLs base dos serviços
    account_url = "http://localhost:8081/api/users"
    order_url = "http://localhost:8085/orders"
    payment_url = "http://localhost:8087/payments"

    # Configuração do Timeout
    timeout = 30  # Timeout de 30 segundos

    def on_start(self):
        """Executa ao iniciar o teste: Registra usuário e faz login."""
        try:
            self.register_user()
            self.login_user()  # Logar após o registro
        except Exception as e:
            print(f"Erro ao executar on_start: {e}")

    def register_user(self):
        """Faz o registro de um novo usuário com dados gerados aleatoriamente."""
        fake = Faker()
        self.user_data = {
            "nome": fake.name(),
            "cpf": random.randint(10000000000, 99999999999),
            "email": fake.email(),
            "password": "123teste"
        }

        response = self.client.post(
            f"{self.account_url}/register",
            json=self.user_data,
            timeout=self.timeout,
        )

        print(f"Status da resposta do registro: {response.status_code}")
        if response.status_code == 201:
            print(f"Usuário registrado com sucesso!")
        else:
            print(f"Falha no registro: {response.status_code} - {response.text}")

    @task
    def login_user(self):
        """Faz o login e armazena o token."""
        if not self.user_data:
            print("Erro: Usuário não registrado. Login não pode ser feito.")
            return

        response = self.client.post(
            f"{self.account_url}/login",
            json={
                "email": self.user_data["email"],
                "password": self.user_data["password"]
            },
            timeout=self.timeout,
        )

        print(f"Status da resposta do login: {response.status_code}")
        print(f"Corpo da resposta: {response.text}")

        if response.status_code == 200:
            # Supondo que o token JWT venha diretamente como texto na resposta
            self.token = response.text.strip()  # Token vindo como uma string simples
            print(f"Token JWT: {self.token}")
        else:
            print(f"Falha no login: {response.status_code} - {response.text}")


    @task(6)
    def create_order(self):
        """Cria um novo pedido e armazena o ID do pedido gerado."""
        if self.token:
            headers = {"token": self.token}  # Usar o token nas requisições de criação (POST)
            response = self.client.post(
                f"{self.order_url}",
                headers=headers,
                json={
                    "items": [
                        {
                            "productName": fake.word(),
                            "quantity": fake.random_int(min=1, max=5),
                            "price": round(fake.random_number(digits=2), 2)
                        }
                    ]
                },
                timeout=self.timeout,
            )
            print(f"Status da requisição create_order: {response.status_code}")
            if response.status_code == 201:
                # Supondo que o ID do pedido seja retornado no corpo da resposta
                self.order_id = response.json().get("id")
                print(f"Pedido criado com sucesso! ID do pedido: {self.order_id}")
            else:
                print(f"Falha ao criar pedido: {response.status_code} - {response.text}")

    @task(3)
    def get_orders(self):
        """Busca pedidos existentes."""
        # Não é necessário token para GET, então fazemos a requisição sem o token.
        response = self.client.get(f"{self.order_url}", timeout=self.timeout)
        print(f"Status da requisição get_orders: {response.status_code}")


    @task(7)
    def make_payment(self):
        """Efetua pagamento para um pedido utilizando o order_id."""
        if self.token and self.order_id:
            headers = {"token": self.token}  # Usar o token nas requisições de pagamento (POST)

            # Verificar o status do pedido antes de efetuar o pagamento
            order_response = self.client.get(
                f"{self.order_url}/{self.order_id}",  # Endpoint para pegar os detalhes do pedido
                headers=headers,
                timeout=self.timeout,
            )

            # Verifica se o pedido está "ativo" ou "pendente"
            if order_response.status_code == 200:
                order_data = order_response.json()
                if order_data.get('status') == 'PENDING':  # Verifique o campo "status" do pedido
                    # Efetuar pagamento se o pedido estiver pendente
                    payment_response = self.client.post(
                        f"{self.payment_url}/pay/{self.order_id}",  # Usar o order_id na URL
                        headers=headers,
                        json={
                            "nameCard": fake.name(),
                            "numberCard": fake.credit_card_number(),
                            "dueDate": fake.credit_card_expire(),
                            "code": fake.credit_card_security_code()
                        },
                        timeout=self.timeout,
                    )

                    print(f"Status da requisição make_payment: {payment_response.status_code}")
                    if payment_response.status_code == 200:
                        print("Pagamento efetuado com sucesso!")
                    else:
                        print(f"Falha no pagamento: {payment_response.status_code} - {payment_response.text}")
                else:
                    print(f"Não é possível pagar o pedido {self.order_id}: Status do pedido é '{order_data.get('status')}'")
            else:
                print(f"Falha ao obter o status do pedido: {order_response.status_code} - {order_response.text}")
        else:
            print("Erro: Não foi possível efetuar o pagamento. Verifique o token e o order_id.")
