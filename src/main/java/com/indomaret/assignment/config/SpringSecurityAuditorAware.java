package com.indomaret.assignment.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditorProvider")
public class SpringSecurityAuditorAware implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || 
            !authentication.isAuthenticated() || 
            authentication.getPrincipal().equals("anonymousUser")) {
            // Fallback for system-level actions (like database seeding) before login
            return Optional.of("SYSTEM"); 
        }

        // Returns the username extracted from the JWT token
        return Optional.of(authentication.getName());
    }
}