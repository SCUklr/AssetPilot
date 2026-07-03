package com.mashu.assetpilot.entity;
// 这是Asset实体类

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("asset") // 告诉 MyBatis-Plus 这张表叫 asset
public class Asset {
    @TableId(type = IdType.AUTO) // id 是自增主键
    private Long id;
    private String name;
    private String category;
    @TableField("asset_value")
    private BigDecimal value; // 钱/价格类字段用这个，避免 float/double 精度问题
    private String owner;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
