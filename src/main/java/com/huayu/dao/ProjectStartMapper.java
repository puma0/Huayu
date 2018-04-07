package com.huayu.dao;

import com.github.pagehelper.PageInfo;
import com.huayu.entity.ProjectStart;

public interface ProjectStartMapper {

	int deleteByPrimaryKey(Integer id);

	int insert(ProjectStart record);

	int insertSelective(ProjectStart record);

	PageInfo<ProjectStart> getPage(int pageNo, int pageSize);

	ProjectStart selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ProjectStart record);

	int updateByPrimaryKeyWithBLOBs(ProjectStart record);

	int updateByPrimaryKey(ProjectStart record);
}