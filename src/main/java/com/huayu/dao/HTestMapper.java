package com.huayu.dao;

import com.github.pagehelper.PageInfo;
import com.huayu.entity.HTest;

public interface HTestMapper {
	int insert(HTest record);

	int insertSelective(HTest record);

	PageInfo<HTest> getPage(int pageNo, int pageSize);
}