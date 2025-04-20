package org.example.expert.domain.common.logging;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Aspect
@Component
@Slf4j
public class AdminLogAspect {

    @Around("@annotation(org.example.expert.domain.common.logging.AdminLog)")
    public Object logAdminAccess(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String method = request.getMethod();
        String uri = request.getRequestURI();

        Long userId = (Long) request.getAttribute("userId");

        Object[] args = joinPoint.getArgs();
        long start = System.currentTimeMillis();

        Object result = null;
        try {
            result = joinPoint.proceed();
            return result;
        } finally {
            long duration = System.currentTimeMillis() - start;

            log.info("🛡️ [ADMIN] {} {} | user={} | duration={}ms", method, uri, userId, duration);
            log.debug("RequestBody: {}", Arrays.toString(args));
            log.debug("ResponseBody: {}", result);
        }
    }
}

