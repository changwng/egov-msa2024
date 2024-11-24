package org.egovframe.cloud.boardservice.service.auth;

import lombok.RequiredArgsConstructor;
import org.egovframe.cloud.boardservice.domain.auth.DelegatedAuthority;
import org.egovframe.cloud.boardservice.domain.auth.Permission;
import org.egovframe.cloud.boardservice.domain.auth.RolePermission;
import org.egovframe.cloud.common.domain.Role;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final RolePermissionRepository rolePermissionRepository;
    private final DelegatedAuthorityRepository delegatedAuthorityRepository;

    /**
     * 사용자의 권한 확인
     */
    public boolean hasPermission(String userId, String targetType, String permission) {
        return hasPermission(userId, targetType, null, permission);
    }

    /**
     * 사용자의 특정 리소스에 대한 권한 확인
     */
    public boolean hasPermission(String userId, String targetType, String resourceId, String permission) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) return false;

        // 1. Role 기반 권한 확인
        List<RolePermission> rolePermissions = rolePermissionRepository.findByRoleAndResourceType(
                Role.valueOf(authentication.getAuthorities().iterator().next().getAuthority()),
                targetType
        );

        for (RolePermission rolePermission : rolePermissions) {
            if (rolePermission.getPermission().getAction().equals(permission)) {
                if (resourceId == null || rolePermission.getResourceId() == null || 
                    rolePermission.getResourceId().equals(resourceId)) {
                    return true;
                }
            }
        }

        // 2. 위임된 권한 확인
        if (resourceId != null) {
            LocalDateTime now = LocalDateTime.now();
            List<DelegatedAuthority> delegatedAuthorities = delegatedAuthorityRepository
                    .findByDelegateeIdAndResourceTypeAndResourceIdAndActiveTrue(
                            userId, targetType, resourceId);

            return delegatedAuthorities.stream()
                    .anyMatch(da -> now.isAfter(da.getStartDate()) && 
                                  now.isBefore(da.getEndDate()));
        }

        return false;
    }

    /**
     * 권한 위임
     */
    @Transactional
    public void delegateAuthority(String delegatorId, String delegateeId, 
                                String resourceType, String resourceId,
                                LocalDateTime startDate, LocalDateTime endDate) {
        // 위임자가 메인 관리자인지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.SUPER_ADMIN.getKey()))) {
            throw new IllegalStateException("메인 관리자만 권한을 위임할 수 있습니다.");
        }

        DelegatedAuthority delegatedAuthority = new DelegatedAuthority();
        // delegatedAuthority 설정
        delegatedAuthorityRepository.save(delegatedAuthority);
    }

    /**
     * 권한 위임 취소
     */
    @Transactional
    public void revokeDelegation(String delegatorId, String delegateeId, String resourceId) {
        // 위임자가 메인 관리자인지 확인
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals(Role.SUPER_ADMIN.getKey()))) {
            throw new IllegalStateException("메인 관리자만 권한을 취소할 수 있습니다.");
        }

        List<DelegatedAuthority> delegatedAuthorities = delegatedAuthorityRepository
                .findByDelegatorIdAndDelegateeIdAndResourceId(delegatorId, delegateeId, resourceId);
        
        delegatedAuthorities.forEach(da -> da.setActive(false));
        delegatedAuthorityRepository.saveAll(delegatedAuthorities);
    }
}
