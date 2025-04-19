package org.example.expert.domain.user.controller;

import org.example.expert.config.TestJwtUtil;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(OutputCaptureExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class AuthInterceptorTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private TestJwtUtil testJwtUtil;

	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	void setup() {
		User user = new User("admin@test.com", "password123", UserRole.ADMIN);
		userRepository.save(user);
	}

	@Test
	void AuthInterceptorTest(CapturedOutput output) throws Exception {
		String token = testJwtUtil.createAdminToken(1L);

		mockMvc.perform(delete("/admin/comments/1")
				.header("Authorization", token))
			.andExpect(status().isOk());

		assertThat(output.getOut()).contains("어드민 API 접근");
		assertThat(output.getOut()).contains("/admin/comments/1");
	}

	@Test
	void AOP_로깅_정상_작동_확인(CapturedOutput output) throws Exception {
		String token = testJwtUtil.createAdminToken(1L);

		mockMvc.perform(patch("/admin/users/1")
				.header("Authorization", token)
				.contentType("application/json")
				.content("""
                    {
                        "role": "ADMIN"
                    }
                """))
			.andExpect(status().isOk());

		assertThat(output.getOut()).contains("어드민 요청");
		assertThat(output.getOut()).contains("/admin/users/1");
		assertThat(output.getOut()).contains("ADMIN");
		assertThat(output.getOut()).contains("어드민 응답");
	}
}
