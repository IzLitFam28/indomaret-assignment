package com.indomaret.assignment.repository;

import com.indomaret.assignment.entity.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {
    Optional<Branch> findByCode(String code);

    @Query(value = "SELECT * FROM branches WHERE id = :id AND is_deleted = false", nativeQuery = true)
    Optional<Branch> findByIdForUpdate(@Param("id") Long id);
}