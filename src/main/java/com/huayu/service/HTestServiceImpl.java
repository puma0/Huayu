package com.huayu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huayu.dao.HTestMapper;
import com.huayu.entity.HTest;

@Service
public class HTestServiceImpl {

	@Autowired
	private HTestMapper testMapper;

	public PageInfo<HTest> getPage(int pageNo, int pageSize) {
		Page<HTest> page = PageHelper.startPage(pageNo, pageSize);
		// Mybatis查询方法
		testMapper.selectAll();
		//使用PageInfo封装
		PageInfo<HTest> info = new PageInfo<HTest>(page);
		return info;
	}

	public int insert(HTest record) {
		return testMapper.insert(record);
	}

	public int insertSelective(HTest record) {
		return testMapper.insertSelective(record);
	}

}
