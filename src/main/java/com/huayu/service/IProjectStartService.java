package com.huayu.service;

import com.github.pagehelper.PageInfo;
import com.huayu.entity.ProjectStart;

public interface IProjectStartService {

	int deleteByPrimaryKey(Integer id);

	int insertSelective(ProjectStart record);

	PageInfo<ProjectStart> getPage(int pageNo, int pageSize);

	ProjectStart selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(ProjectStart record);

}
