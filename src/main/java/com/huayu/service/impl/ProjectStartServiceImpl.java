package com.huayu.service.impl;

import org.activiti.engine.HistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.huayu.dao.ProjectStartMapper;
import com.huayu.entity.ProjectStart;
import com.huayu.service.IProjectStartService;

@Service
@Transactional
public class ProjectStartServiceImpl implements IProjectStartService {

	@Autowired
	private ProjectStartMapper proStartMapper;

	@Autowired
	private HistoryService historyService;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		return proStartMapper.deleteByPrimaryKey(id);
	}

	@Override
	public int insertSelective(ProjectStart record) {
		return proStartMapper.insertSelective(record);
	}

	@Override
	public PageInfo<ProjectStart> getPage(int pageNo, int pageSize) {
		return proStartMapper.getPage(pageNo, pageSize);
	}

	@Override
	public ProjectStart selectByPrimaryKey(Integer id) {
		return proStartMapper.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(ProjectStart record) {
		return proStartMapper.updateByPrimaryKey(record);
	}

}
