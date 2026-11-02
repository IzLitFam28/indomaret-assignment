package com.indomaret.assignment.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.indomaret.assignment.entity.AuditLog;
import com.indomaret.assignment.repository.AuditLogRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = new ObjectMapper(); 
        this.objectMapper.findAndRegisterModules();
    }

    public void logAction(String action, String entityName, Long entityId, Object payload) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        String changesJson = null;
        
        try {
            if (payload != null) {
                changesJson = objectMapper.writeValueAsString(payload);
            }
        } catch (JsonProcessingException e) {
            changesJson = "{\"error\": \"Failed to serialize payload\"}";
        }

        AuditLog log = AuditLog.builder()
                .username(username)
                .action(action)
                .entityName(entityName)
                .entityId(entityId)
                .changes(changesJson)
                .build();

        auditLogRepository.save(log);
    }
}