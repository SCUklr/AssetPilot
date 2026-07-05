package com.mashu.assetpilot.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mashu.assetpilot.common.BusinessException;
import com.mashu.assetpilot.dto.AssetCreateRequest;
import com.mashu.assetpilot.dto.AssetPageRequest;
import com.mashu.assetpilot.dto.AssetUpdateRequest;
import com.mashu.assetpilot.entity.Asset;
import com.mashu.assetpilot.mapper.AssetMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor // Lombok 自动生成构造方法，final 字段会被自动注入
public class AssetService {
    private final AssetMapper assetMapper;
    private final RedisTemplate<String, Object> redisTemplate;

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
        // Redis 手写改造
        String key = "asset:detail:" + id;

        // 1. 先查 Redis
        Object cached = redisTemplate.opsForValue().get(key);
        if (cached instanceof Asset) {
            return (Asset) cached;
        }

        // 2. Redis 没有，查 MySQL 数据库
        Asset asset = assetMapper.selectById(id);

        // 3. 查到了就写回 Redis， 设置 10 分钟过期
        if (asset != null) {
            redisTemplate.opsForValue().set(key, asset, Duration.ofMinutes(10));
        }
        return asset; // MyBatis-Plus 自带方法
    }

    public boolean deleteById(Long id) {
//        boolean deleted = assetMapper.deleteById(id) > 0;
//        if (deleted) {
//            redisTemplate.delete("asset:detail:" + id);
//        }
//        return deleted;
        // 更加正式的写法
        // 判断非空
        Asset asset = assetMapper.selectById(id);
        if (asset == null) {
            throw new BusinessException("资产不存在"); // 这样 id 不存在时直接返回 500 错误，语义更清晰
        }

        // 接下来才是正式删除
        assetMapper.deleteById(id);

        // Redis 同步删除
        redisTemplate.delete("asset:detail:" + id);
        return true;
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

        // 更新和删除时，必须把对应缓存清掉，否则会出现数据不一致
        redisTemplate.delete("asset:detail:" + id);

        // 5. 返回最新数据
        return assetMapper.selectById(id);
    }

    public Page<Asset> page(AssetPageRequest request) {
        // 1. 创建分页对象
        Page<Asset> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 2. 创建查询条件构造器
        QueryWrapper<Asset> wrapper = new QueryWrapper<>();

        // 3. 动态拼接条件：只传了非空的才加条件
        if (request.getName() != null && !request.getName().isEmpty()) {
            wrapper.like("name", request.getName());
        }
        if (request.getCategory() != null && !request.getCategory().isEmpty()) {
            // eq：精确匹配
            wrapper.eq("category", request.getCategory());
        }
        if (request.getOwner() != null && !request.getOwner().isEmpty()) {
            wrapper.eq("owner", request.getOwner());
        }
        if (request.getStatus() != null && !request.getStatus().isEmpty()) {
            wrapper.eq("status", request.getStatus());
        }

        // 4. 按创建时间倒序（新的在前）
        wrapper.orderByDesc("created_at");

        // 5. 调用 Mapper 的分页查询方法
        return assetMapper.selectPage(page, wrapper);
    }
}
