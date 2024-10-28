package com.mony.account;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.when;

import com.mony.account.controller.UserController;
import com.mony.account.dto.UserDTO;
import com.mony.account.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;

import java.util.List;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Test
	void testFindAllUsers() throws Exception {
		// Simulando dados de retorno para findAllUsers
		UserDTO user1 = new UserDTO("User1", "12345678901", "user1@example.com");
		UserDTO user2 = new UserDTO("User2", "09876543210", "user2@example.com");
		List<UserDTO> users = List.of(user1, user2);

		// Mock do serviço para retornar a lista simulada
		when(userService.findAllUsers(0, 2)).thenReturn(users);

		// Teste para o endpoint
		mockMvc.perform(get("/api/users")  // Supondo que o endpoint base seja /users
						.param("page", "0")
						.param("size", "2")
						.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.size()").value(2))
				.andExpect(jsonPath("$[0].nome").value("User1"))
				.andExpect(jsonPath("$[0].cpf").value("12345678901"))
				.andExpect(jsonPath("$[0].email").value("user1@example.com"))
				.andExpect(jsonPath("$[1].nome").value("User2"))
				.andExpect(jsonPath("$[1].cpf").value("09876543210"))
				.andExpect(jsonPath("$[1].email").value("user2@example.com"));
	}
}
