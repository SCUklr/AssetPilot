package com.mashu.assetpilot.controller;

import com.mashu.assetpilot.common.Result;
import com.mashu.assetpilot.dto.AssetCreateRequest;
import com.mashu.assetpilot.entity.Asset;
import com.mashu.assetpilot.service.AssetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
