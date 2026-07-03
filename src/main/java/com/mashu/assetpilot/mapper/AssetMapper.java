package com.mashu.assetpilot.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mashu.assetpilot.entity.Asset;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AssetMapper extends BaseMapper<Asset> {
}

