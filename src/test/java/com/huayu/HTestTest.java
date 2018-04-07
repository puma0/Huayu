package com.huayu;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageInfo;
import com.huayu.dao.HTestMapper;
import com.huayu.entity.HTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class HTestTest {

	@Autowired
	private HTestMapper testMapper;

	@Test
	@Rollback(false)
	public void insert() {
		HTest h = new HTest();
		h.setId("1");
		h.setName("1");
		testMapper.insert(h);
	}

	@Test
	public void getPage() {
		PageInfo<HTest> page = testMapper.getPage(1, 10);
		System.out.println(page.getList().size());
	}

}
