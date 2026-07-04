package com.mashu.assetpilot.dto;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class AssetUpdateRequest {
    private String name;
    private String category;
    private BigDecimal value;
    private String owner;
    private String status;
}
