package com.indomaret.assignment.repository;

import com.indomaret.assignment.entity.Store;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {

    @Query(value = """
        SELECT s FROM Store s
        JOIN FETCH s.branch b
        JOIN FETCH b.province p
        LEFT JOIN WhitelistedStore w ON w.store.id = s.id AND w.isActive = true AND w.isDeleted = false
        WHERE (
            (:includeWhitelist = true AND w.id IS NOT NULL)
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :provinceName, '%'))
        )
        ORDER BY CASE WHEN w.id IS NOT NULL THEN 0 ELSE 1 END, s.name ASC
        """,
        countQuery = """
        SELECT COUNT(s) FROM Store s
        JOIN s.branch b
        JOIN b.province p
        LEFT JOIN WhitelistedStore w ON w.store.id = s.id AND w.isActive = true AND w.isDeleted = false
        WHERE (
            (:includeWhitelist = true AND w.id IS NOT NULL)
            OR LOWER(p.name) LIKE LOWER(CONCAT('%', :provinceName, '%'))
        )
        """)
    Page<Store> searchByProvinceOrWhitelist(
            @Param("provinceName") String provinceName,
            @Param("includeWhitelist") boolean includeWhitelist,
            Pageable pageable
    );
}