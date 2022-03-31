package com.api.support;

import com.bilibili.domain.exception.ConditionException;
import com.bilibili.service.util.TokenUtil;
import jdk.nashorn.internal.parser.Token;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class UserSupport {

    public Long getCurrentUserId() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        String token = requestAttributes.getRequest().getHeader("token");
        Long userId = TokenUtil.verifyToken(token);
        if (userId < 0) {
            throw new ConditionException("Invalid user!");
        }
        return userId;
    }
}
