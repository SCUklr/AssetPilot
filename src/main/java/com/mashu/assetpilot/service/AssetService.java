package com.mashu.assetpilot.service;

import com.mashu.assetpilot.common.BusinessException;
import com.mashu.assetpilot.dto.AssetCreateRequest;
import com.mashu.assetpilot.dto.AssetUpdateRequest;
import com.mashu.assetpilot.entity.Asset;
import com.mashu.assetpilot.mapper.AssetMapper;
import jakarta.validation.Valid;
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

    public Asset update(Long id, @Valid AssetUpdateRequest request) {
        // 1. 先查记录存不存在
        Asset asset = assetMapper.selectById(id);
        if (asset == null) {
            throw new BusinessException("资产不存在");
        }

        // 2. 复制request要修改的字段到asset
        BeanUtils.copyProperties(request, asset);

        // 3. 必须设置 id
        asset.setId(id);
        asset.setUpdatedAt(LocalDateTime.now());

        // 4. 调用Mapper层更新方法
        assetMapper.updateById(asset);

        // 5. 返回最新数据
        return assetMapper.selectById(id);
    }
}
