package com.indomaret.assignment.service;

import com.indomaret.assignment.config.AppRulesProperties;
import com.indomaret.assignment.entity.Branch;
import com.indomaret.assignment.repository.BranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BranchService {

    private final BranchRepository branchRepository;
    private final AuditLogService auditLogService;
    private final AppRulesProperties appRules;

    public BranchService(BranchRepository branchRepository, AuditLogService auditLogService, AppRulesProperties appRules) {
        this.branchRepository = branchRepository;
        this.auditLogService = auditLogService;
        this.appRules = appRules;
    }

    @Transactional
    public Branch updateBranch(Long id, String name, String address, Boolean isActive) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

        if (name != null) branch.setName(name);
        if (address != null) branch.setAddress(address);
        if (isActive != null) branch.setIsActive(isActive);

        Branch savedBranch = branchRepository.save(branch);
        auditLogService.logAction("UPDATE", "Branch", savedBranch.getId(), savedBranch);
        return savedBranch;
    }

    @Transactional
    public void deleteBranch(Long id) {
        Branch branch = branchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Branch not found with id: " + id));

        branch.setIsDeleted(true);
        branch.setIsActive(false);

        // Configuration-Based Design: Cascade soft delete based on application.yaml
        if (appRules.getBranch().isCascadeSoftDeleteToStores()) {
            branch.getStores().forEach(store -> {
                store.setIsDeleted(true);
                store.setIsActive(false);
            });
        }

        branchRepository.save(branch);
        auditLogService.logAction("SOFT_DELETE", "Branch", branch.getId(), null);
    }
}