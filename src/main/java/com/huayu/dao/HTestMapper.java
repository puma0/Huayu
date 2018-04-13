package com.huayu.dao;

import java.util.List;

import com.huayu.entity.HTest;

public interface HTestMapper {
	int insert(HTest record);

	int insertSelective(HTest record);

	List<HTest> selectAll();
}