package com.indomaret.assignment.service;

import com.indomaret.assignment.entity.Store;
import com.indomaret.assignment.entity.WhitelistedStore;
import com.indomaret.assignment.repository.StoreRepository;
import com.indomaret.assignment.repository.WhitelistedStoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WhitelistService {

    private final WhitelistedStoreRepository whitelistRepository;
    private final StoreRepository storeRepository;
    private final AuditLogService auditLogService;

    public WhitelistService(WhitelistedStoreRepository whitelistRepository, StoreRepository storeRepository, AuditLogService auditLogService) {
        this.whitelistRepository = whitelistRepository;
        this.storeRepository = storeRepository;
        this.auditLogService = auditLogService;
    }

    public Page<WhitelistedStore> getAllWhitelistedStores(Pageable pageable) {
        return whitelistRepository.findAll(pageable);
    }

    @Transactional
    public WhitelistedStore addStoreToWhitelist(Long storeId, String reason) {
        Store store = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException("Store not found with id: " + storeId));

        if (whitelistRepository.findByStoreId(storeId).isPresent()) {
            throw new RuntimeException("Store is already whitelisted");
        }

        WhitelistedStore whitelistEntry = WhitelistedStore.builder()
                .store(store)
                .reason(reason)
                .build();

        WhitelistedStore savedEntry = whitelistRepository.save(whitelistEntry);
        auditLogService.logAction("CREATE", "WhitelistedStore", savedEntry.getId(), savedEntry);
        return savedEntry;
    }

    @Transactional
    public void removeStoreFromWhitelist(Long id) {
        WhitelistedStore entry = whitelistRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Whitelist entry not found with id: " + id));

        entry.setIsDeleted(true);
        entry.setIsActive(false);

        whitelistRepository.save(entry);
        auditLogService.logAction("SOFT_DELETE", "WhitelistedStore", entry.getId(), null);
    }
}