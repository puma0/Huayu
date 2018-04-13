package com.huayu.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ProcessEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/statistics")
public class StatisticsController {

	protected static Logger log = LoggerFactory.getLogger(ProjectStartController.class);

	@Autowired
	private ProcessEngine processEngine;

	/**
	 * 统计
	 * @return
	 */
	@RequestMapping(value = "/myTask.do")
	public Map<String, Long> myTask() {
		String currentUser = ""; //当前用户
		Map<String, Long> resMap = new HashMap<String, Long>();
		//获取当前分配到"我"的任务数
		long myCount = processEngine.getTaskService()//获取任务service  
				.createTaskQuery()//创建查询对象  
				.taskAssignee(currentUser)//指定查询人  
				.count();
		resMap.put("my", myCount);

		//获取当前分配到"我所属组"的任务数
		//获取"我"所属的组列表
		List<String> groups = new ArrayList<String>();
		groups.add("市场部部长");
		long myGroup = processEngine.getTaskService().createTaskQuery().taskCandidateGroupIn(groups).count();
		resMap.put("myGroup", myGroup);
		return resMap;
	}

}
