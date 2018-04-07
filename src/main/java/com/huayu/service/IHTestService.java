package com.huayu.service;

import com.github.pagehelper.PageInfo;
import com.huayu.entity.HTest;

public interface IHTestService {

	public PageInfo<HTest> getPage(int pageNo, int pageSize);

}
