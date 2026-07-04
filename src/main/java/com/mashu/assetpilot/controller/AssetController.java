package com.mashu.assetpilot.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mashu.assetpilot.common.Result;
import com.mashu.assetpilot.dto.AssetCreateRequest;
import com.mashu.assetpilot.dto.AssetPageRequest;
import com.mashu.assetpilot.dto.AssetUpdateRequest;
import com.mashu.assetpilot.entity.Asset;
import com.mashu.assetpilot.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController // 返回 JSON，不是视图
@RequestMapping("/assets") // 这个 Controller 下所有接口前缀都是 /assets
@RequiredArgsConstructor
public class AssetController {

    private final AssetService assetService;

    // @Valid：触发 DTO 里的校验注解
    // @RequestBody：把请求体 JSON 转成对象
    @PostMapping
    public Result<Long> create(@Valid @RequestBody AssetCreateRequest request) {
        Long id = assetService.create(request);
        return Result.success(id);
    }

    @GetMapping("/{id}")
    public Result<Asset> getById(@PathVariable Long id) { // @PathVariable 的作用是把 URL 路径里的值取出来，传给方法参数。
        Asset asset = assetService.getById(id); // 调用 Service 层方法
        return Result.success(asset);
    }

    @DeleteMapping("/{id}")
    public Result<Boolean> deleteById(@PathVariable Long id) { // 删除用布尔值存储
        boolean deleted = assetService.deleteById(id);
        return Result.success(deleted);
    }

    @PutMapping("/{id}")
    public Result<Asset> update(@PathVariable Long id, @Valid @RequestBody AssetUpdateRequest request) {
        Asset updatedAsset = assetService.update(id, request);
        return Result.success(updatedAsset);
    }

    /**
     * 分页查询
     * @RequestParam：把查询参数 ?pageNum=1&pageSize=10 绑定到对象
     */
    @GetMapping("/page")
    public Result<Page<Asset>> page(AssetPageRequest request) {
        Page<Asset> page = assetService.page(request);
        return Result.success(page);
    }
}
