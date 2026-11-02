package com.indomaret.assignment.service;

import com.indomaret.assignment.config.AppRulesProperties;
import com.indomaret.assignment.entity.Store;
import com.indomaret.assignment.repository.StoreRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class StoreService {

    private final StoreRepository storeRepository;
    private final AppRulesProperties appRules;

    public StoreService(StoreRepository storeRepository, AppRulesProperties appRules) {
        this.storeRepository = storeRepository;
        this.appRules = appRules;
    }

    public Page<Store> searchStores(String provinceName, Boolean includeWhitelist, Pageable pageable) {
        // Fallback to configuration-based design if parameter is not explicitly provided
        boolean shouldIncludeWhitelist = (includeWhitelist != null) 
                ? includeWhitelist 
                : appRules.getSearch().isIncludeWhitelistByDefault();

        return storeRepository.searchByProvinceOrWhitelist(provinceName, shouldIncludeWhitelist, pageable);
    }
}