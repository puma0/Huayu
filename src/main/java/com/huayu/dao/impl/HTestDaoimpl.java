package com.huayu.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huayu.dao.HTestMapper;
import com.huayu.dao.base.DaoSupport;
import com.huayu.entity.HTest;

@Repository("hTestDao")
public class HTestDaoimpl implements HTestMapper {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public PageInfo<HTest> getPage(int pageNo, int pageSize) {
		Page<HTest> page = PageHelper.startPage(pageNo, pageSize);
		try {
			dao.findForList("HTestMapper.selectAll", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<HTest> info = new PageInfo<HTest>(page);
		return info;
	}

	@Override
	public int insert(HTest record) {
		try {
			return (int) dao.save("HTestMapper.insert", record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int insertSelective(HTest record) {
		return 0;
	}

}
