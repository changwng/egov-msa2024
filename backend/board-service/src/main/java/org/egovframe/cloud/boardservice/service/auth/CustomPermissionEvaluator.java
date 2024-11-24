package org.egovframe.cloud.boardservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.egovframe.cloud.common.domain.Role;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@RequiredArgsConstructor
public class CustomPermissionEvaluator implements PermissionEvaluator {

    private final AuthorizationService authorizationService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if ((authentication == null) || (targetDomainObject == null) || !(permission instanceof String)) {
            return false;
        }

        String userId = authentication.getName();
        String targetType = targetDomainObject.getClass().getSimpleName().toUpperCase();
        String permissionString = ((String) permission).toUpperCase();

        return authorizationService.hasPermission(userId, targetType, permissionString);
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }

        String userId = authentication.getName();
        String permissionString = ((String) permission).toUpperCase();

        return authorizationService.hasPermission(userId, targetType.toUpperCase(), targetId.toString(), permissionString);
    }
}
