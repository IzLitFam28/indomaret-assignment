package com.indomaret.assignment.controller;

import com.indomaret.assignment.dto.WhitelistRequest;
import com.indomaret.assignment.entity.WhitelistedStore;
import com.indomaret.assignment.service.WhitelistService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/whitelist-stores")
public class WhitelistController {

    private final WhitelistService whitelistService;

    public WhitelistController(WhitelistService whitelistService) {
        this.whitelistService = whitelistService;
    }

    @GetMapping
    public ResponseEntity<Page<WhitelistedStore>> getAllWhitelistedStores(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        
        return ResponseEntity.ok(whitelistService.getAllWhitelistedStores(PageRequest.of(page, size)));
    }

    @PostMapping
    public ResponseEntity<WhitelistedStore> addStoreToWhitelist(@RequestBody WhitelistRequest request) {
        WhitelistedStore savedEntry = whitelistService.addStoreToWhitelist(request.getStoreId(), request.getReason());
        return ResponseEntity.ok(savedEntry);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeStoreFromWhitelist(@PathVariable Long id) {
        whitelistService.removeStoreFromWhitelist(id);
        return ResponseEntity.noContent().build();
    }
}