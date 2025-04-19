package org.example.expert.logging;

import java.time.LocalDateTime;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class AdminLogginAspect {

	private final HttpServletRequest request;

	private final ObjectMapper objectMapper;

	@Around("@annotation(org.example.expert.logging.AdminLogging)")
	public Object logAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {

		Long userId = (Long) request.getAttribute("userId");
		String uri = request.getRequestURI();
		LocalDateTime time = LocalDateTime.now();

		String requestBody = extractArgument(joinPoint);

		log.info("어드민 요청 - userId: {}, URI: {}, time: {}, request: {}", userId, uri, time, requestBody);

		Object result = joinPoint.proceed();

		String responseJson = objectMapper.writeValueAsString(joinPoint.getArgs());

		log.info("어드민 응답 - userId: {}, URI: {}, time: {}, request: {}", userId, uri, time, responseJson);

		return result;
	}

	private String extractArgument(ProceedingJoinPoint joinPoint) {
		try {
			Object[] args = joinPoint.getArgs();
			return objectMapper.writeValueAsString(args);
		} catch (Exception e) {
			return "요청 본문의 반환에 실패 하였습니다.";
		}
	}
}
