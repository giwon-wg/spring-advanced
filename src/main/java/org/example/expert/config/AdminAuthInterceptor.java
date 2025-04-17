package org.example.expert.config;

import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AdminAuthInterceptor implements HandlerInterceptor {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		Long userId = (Long) request.getAttribute("userId");
		String userRole = (String) request.getAttribute("userRole");

		if (userId == null || userRole == null) {
			log.warn("어드민 인증 실패 - 인증 정보 누락 - URI: {}, time: {}", request.getRequestURI(), LocalDateTime.now());
			throw new IllegalStateException("인증 정보가 없습니다.");
		}

		if (!"ADMIN".equals(userRole)) {
			log.warn("어드민 인증 실패 - 어드민 아님 - userId: {}, URI: {}, time: {}", userId, request.getRequestURI(), LocalDateTime.now());
			throw new IllegalStateException("어드민만 접근할 수 있습니다.");
		}

		log.info("어드민 API 접근 - userId: {}, URI: {}, time: {}", userId, request.getRequestURI(), LocalDateTime.now());
		return true;
	}

}
