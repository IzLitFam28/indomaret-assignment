package com.indomaret.assignment.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WhitelistRequest {
    private Long storeId;
    private String reason;
}