package com.mashu.assetpilot.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder // 用 @Builder 是为了构造事件对象时更清楚，不用写一堆 setXxx
public class AssetChangeEvent {
    private Long assetId;
    private String changeType; // UPDATE, DELETE, SCRAP
    private String assetName;
    private String newStatus;
    private BigDecimal value;
    private String operator;
    private LocalDateTime eventTime;
}
