package com.huayu.dao.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huayu.dao.ProjectStartMapper;
import com.huayu.dao.base.DaoSupport;
import com.huayu.entity.ProjectStart;

@Repository("projectStartMapperImpl")
public class ProjectStartMapperImpl implements ProjectStartMapper {

	@Resource(name = "daoSupport")
	private DaoSupport dao;

	@Override
	public int deleteByPrimaryKey(Integer id) {
		try {
			return (int) dao.delete("ProjectStartMapper.deleteByPrimaryKey", id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int insert(ProjectStart record) {
		try {
			return (int) dao.save("ProjectStartMapper.insert", record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int insertSelective(ProjectStart record) {
		try {
			return (int) dao.save("ProjectStartMapper.insertSelective", record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public ProjectStart selectByPrimaryKey(Integer id) {
		try {
			return (ProjectStart) dao.findForObject("ProjectStartMapper.selectByPrimaryKey", id);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public int updateByPrimaryKeySelective(ProjectStart record) {
		try {
			return (int) dao.update("ProjectStartMapper.updateByPrimaryKeySelective", record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKeyWithBLOBs(ProjectStart record) {
		try {
			return (int) dao.update("ProjectStartMapper.updateByPrimaryKeyWithBLOBs", record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public int updateByPrimaryKey(ProjectStart record) {
		try {
			return (int) dao.update("ProjectStartMapper.updateByPrimaryKey", record);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public PageInfo<ProjectStart> getPage(int pageNo, int pageSize) {
		Page<ProjectStart> page = PageHelper.startPage(pageNo, pageSize);
		try {
			dao.findForList("ProjectStartMapper.selectAll", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
		PageInfo<ProjectStart> info = new PageInfo<ProjectStart>(page);
		return info;
	}

}
