package com.mashu.assetpilot.service;

import com.mashu.assetpilot.common.BusinessException;
import com.mashu.assetpilot.dto.AssetCreateRequest;
import com.mashu.assetpilot.entity.Asset;
import com.mashu.assetpilot.mapper.AssetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Lombok 自动生成构造方法，final 字段会被自动注入
public class AssetService {
    private final AssetMapper assetMapper;

    public Long create(AssetCreateRequest request) {
        // 业务校验：价值大于零
        if (request.getValue().compareTo(java.math.BigDecimal.ZERO) <= 0) {
            throw new BusinessException("资产价值必须大于0！");
        }
        Asset asset = new Asset();
        BeanUtils.copyProperties(request, asset); // 把 DTO 的属性复制到 Entity
        asset.setCreatedAt(LocalDateTime.now());
        asset.setUpdatedAt(LocalDateTime.now());
        assetMapper.insert(asset);
        return asset.getId(); // 插库后 asset.getId() 能拿到自增主键
    }
    public Asset getById(Long id) {
        return assetMapper.selectById(id); // MyBatis-Plus 自带方法
    }

    public boolean deleteById(Long id) {
        return assetMapper.deleteById(id) > 0;
    }
}
