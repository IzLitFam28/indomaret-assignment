package com.indomaret.assignment.controller;

import com.indomaret.assignment.entity.Store;
import com.indomaret.assignment.service.StoreService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stores")
public class StoreController {

    private final StoreService storeService;

    public StoreController(StoreService storeService) {
        this.storeService = storeService;
    }

    @GetMapping
    public ResponseEntity<Page<Store>> searchStores(
            @RequestParam String provinceName,
            @RequestParam(required = false) Boolean includeWhitelist,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        Page<Store> result = storeService.searchStores(provinceName, includeWhitelist, PageRequest.of(page, size));
        return ResponseEntity.ok(result);
    }
}