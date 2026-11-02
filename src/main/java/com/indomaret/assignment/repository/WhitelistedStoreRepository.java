package com.indomaret.assignment.repository;

import com.indomaret.assignment.entity.WhitelistedStore;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WhitelistedStoreRepository extends JpaRepository<WhitelistedStore, Long> {
    Optional<WhitelistedStore> findByStoreId(Long storeId);
}