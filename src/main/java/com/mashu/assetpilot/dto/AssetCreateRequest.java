package com.mashu.assetpilot.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

// DTO（Data Transfer Object）就是专门用来接收请求数据的类，和数据库实体分开。
@Data
public class AssetCreateRequest {
    @NotBlank(message = "资产名称不能为空")
    private String name;

    @NotBlank(message = "资产类别不能为空")@NotBlank(message = "资产类别不能为空")
    private String category;

    @NotNull(message = "资产价值不能为空") // BigDecimal 不是字符串，用 @NotNull
    @Min(value = 1, message = "资产价值必须大于 0") // 数值必须 ≥ 1
    private BigDecimal value;

    @NotBlank(message = "使用人不能为空")
    private String owner;

    @NotBlank(message = "资产状态不能为空")
    private String status;
}
