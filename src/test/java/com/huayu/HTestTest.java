package com.huayu;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.pagehelper.PageInfo;
import com.huayu.entity.HTest;
import com.huayu.service.HTestServiceImpl;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class HTestTest {

	@Autowired
	private HTestServiceImpl testService;

	@Test
	@Rollback(false)
	public void insert() {
		HTest h = new HTest();
		h.setId("4");
		h.setName("4");
		testService.insert(h);
	}

	@Test
	public void getPage() {
		PageInfo<HTest> page = testService.getPage(1, 10);
		if (page != null && page.getTotal() > 0) {
			List<HTest> list = page.getList();
			for (HTest ht : list) {
				System.out.println(ht.getId() + "---" + ht.getName());
			}
		}
	}

}
