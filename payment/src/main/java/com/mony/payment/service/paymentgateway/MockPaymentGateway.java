package com.mony.payment.service.paymentgateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mony.payment.model.dtos.PaymentWriteDTO;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class MockPaymentGateway implements PaymentGateway {

    private final RestTemplate restTemplate;
    //URL para consulta a API externa para processar o pagamento
    private final String PAYMENT_AUTHORIZATION_URL;

    public MockPaymentGateway() {
        this.restTemplate = new RestTemplate();
        PAYMENT_AUTHORIZATION_URL = "https://util.devi.tools/api/v2/authorize";
    }

    @Override
    public boolean processPayment(PaymentWriteDTO paymentWriteDTO) {
        //Captura o Json retornado da API e transforma em string
        try{
            String response = restTemplate.getForObject(PAYMENT_AUTHORIZATION_URL, String.class);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseJson = objectMapper.readTree(response);
            String status = responseJson.path("status").asText();
            boolean authorization = responseJson.path("data").path("authorization").asBoolean();

            //retorna verdadeiro se o status é success. caso contrário retorna false.
            return "success".equals(status) && authorization;
        } catch(HttpClientErrorException e) {
            System.out.println("Erro ao chamar a API de autorização de pagamento: " + e.getMessage());
            return false;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
