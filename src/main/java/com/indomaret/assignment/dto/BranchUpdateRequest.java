package com.indomaret.assignment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BranchUpdateRequest {
    private String name;
    private String address;
    private Boolean isActive;
}