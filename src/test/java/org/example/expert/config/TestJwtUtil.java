package org.example.expert.config;

import org.example.expert.domain.user.enums.UserRole;
import org.springframework.stereotype.Component;

@Component
public class TestJwtUtil {

	private final JwtUtil jwtUtil;

	public TestJwtUtil(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	public String createAdminToken(Long userId) {
		return jwtUtil.createToken(userId, "admin@test.com", UserRole.ADMIN);
	}

	public String createUserToken(Long userId) {
		return jwtUtil.createToken(userId, "user@test.com", UserRole.USER);
	}

	public String createInvalidToken() {
		return "Bearer invalid.token.value";
	}
}
