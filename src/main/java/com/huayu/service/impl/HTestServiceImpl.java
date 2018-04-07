package com.huayu.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.huayu.dao.HTestMapper;
import com.huayu.entity.HTest;
import com.huayu.service.IHTestService;

@Service
@Transactional
public class HTestServiceImpl implements IHTestService {

	@Autowired
	private HTestMapper testMapper;

	@Override
	public PageInfo<HTest> getPage(int pageNo, int pageSize) {
		return testMapper.getPage(pageNo, pageSize);
	}

}
