package com.mashu.assetpilot.dto;

import lombok.Data;

// 分页请求 DTO
@Data
public class AssetPageRequest {

    // 页码，从 1 开始
    private Integer pageNum = 1;

    // 每页大小
    private Integer pageSize = 10;

    // 搜索条件：资产名称（模糊查询）
    private String name;

    // 搜索条件：资产类别
    private String category;

    // 搜索条件：使用人
    private String owner;

    // 搜索条件：资产状态
    private String status;
}
