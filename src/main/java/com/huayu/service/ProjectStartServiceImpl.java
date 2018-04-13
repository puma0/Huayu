package com.huayu.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageInfo;
import com.huayu.dao.ProjectStartMapper;
import com.huayu.entity.ProjectStart;

@Service
public class ProjectStartServiceImpl {

	@Autowired
	private ProjectStartMapper proStartMapper;

	public int deleteByPrimaryKey(Integer id) {
		return proStartMapper.deleteByPrimaryKey(id);
	}

	public int insertSelective(ProjectStart record) {
		return proStartMapper.insertSelective(record);
	}

	public PageInfo<ProjectStart> getPage(int pageNo, int pageSize) {
		return proStartMapper.getPage(pageNo, pageSize);
	}

	public ProjectStart selectByPrimaryKey(Integer id) {
		return proStartMapper.selectByPrimaryKey(id);
	}

	public int updateByPrimaryKeySelective(ProjectStart record) {
		return proStartMapper.updateByPrimaryKey(record);
	}

}
