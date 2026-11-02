package com.indomaret.assignment.controller;

import com.indomaret.assignment.dto.BranchUpdateRequest;
import com.indomaret.assignment.entity.Branch;
import com.indomaret.assignment.service.BranchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchService branchService;

    public BranchController(BranchService branchService) {
        this.branchService = branchService;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(
            @PathVariable Long id,
            @RequestBody BranchUpdateRequest request) {
        
        Branch updatedBranch = branchService.updateBranch(
                id, request.getName(), request.getAddress(), request.getIsActive()
        );
        return ResponseEntity.ok(updatedBranch);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }
}