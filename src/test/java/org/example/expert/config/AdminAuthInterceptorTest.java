package org.example.expert.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class AdminAuthInterceptorTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestJwtUtil testJwtUtil;

	@Test
	void 어드민_토큰_요청_성공() throws Exception {

		//given
		String adminToken = testJwtUtil.createAdminToken(1L);

		//when, then
		mockMvc.perform(delete("/admin/comments/1").header("Authorization", adminToken)).andExpect(status().isOk());
	}

	@Test
	void 일반유저_토큰_요청_403() throws Exception {

		//given
		String adminToken = testJwtUtil.createUserToken(2L);

		//when, then
		mockMvc.perform(delete("/admin/comments/1")
				.header("Authorization", adminToken))
			.andExpect(status().isForbidden());
	}

	@Test
	void 잘못된_토큰_요청_401() throws Exception {

		//given
		String adminToken = testJwtUtil.createInvalidToken();

		//when, then
		mockMvc.perform(delete("/admin/comments/1")
				.header("Authorization", adminToken))
			.andExpect(status().isUnauthorized());
	}

}
