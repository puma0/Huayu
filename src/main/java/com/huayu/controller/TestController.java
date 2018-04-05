package com.huayu.controller;

import java.io.InputStream;
import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/task")
public class TestController {

	@Autowired
	private ProcessEngine processEngine;

	/**部署流程定义（从inputStream）*/
	public void deploymentProcessDefinition_inputStream() {
		InputStream inputStreamBpmn = this.getClass().getResourceAsStream("start.bpmn");
		InputStream inputStreamPng = this.getClass().getResourceAsStream("start.png");
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service  
				.createDeployment()//创建一个部署对象  
				.name("开始活动")//添加部署的名称  
				.addInputStream("start.bpmn", inputStreamBpmn)//  
				.addInputStream("start.png", inputStreamPng)//  
				.deploy();//完成部署  
		System.out.println("部署ID：" + deployment.getId());//  
		System.out.println("部署名称：" + deployment.getName());//  
	}

	/**启动流程实例+判断流程是否结束+查询历史*/
	public void startProcessInstance() {
		//流程定义的key  
		String processDefinitionKey = "start";
		ProcessInstance pi = processEngine.getRuntimeService()//与正在执行的流程实例和执行对象相关的Service  
				.startProcessInstanceByKey(processDefinitionKey);//使用流程定义的key启动流程实例，key对应helloworld.bpmn文件中id的属性值，使用key值启动，默认是按照最新版本的流程定义启动  
		System.out.println("流程实例ID:" + pi.getId());//流程实例ID    101  
		System.out.println("流程定义ID:" + pi.getProcessDefinitionId());//流程定义ID   helloworld:1:4  

		/**判断流程是否结束，查询正在执行的执行对象表*/
		ProcessInstance rpi = processEngine.getRuntimeService()//  
				.createProcessInstanceQuery()//创建流程实例查询对象  
				.processInstanceId(pi.getId()).singleResult();
		//说明流程实例结束了  
		if (rpi == null) {
			/**查询历史，获取流程的相关信息*/
			HistoricProcessInstance hpi = processEngine.getHistoryService()//  
					.createHistoricProcessInstanceQuery()//  
					.processInstanceId(pi.getId())//使用流程实例ID查询  
					.singleResult();
			System.out.println(hpi.getId() + "    " + hpi.getStartTime() + "   " + hpi.getEndTime() + "   "
					+ hpi.getDurationInMillis());
		}
	}

	public void findMyPersonTask() {
		String assignee = "张三"; // TODO  
		List<Task> list = processEngine.getTaskService()// 与正在执行的任务管理相关的service  
				.createTaskQuery()// 创建任务查询对象  
				// 查询条件  
				.taskAssignee(assignee)// 指定个人任务查询，指定办理人  
				// .taskCandidateGroup("")//组任务的办理人查询  
				// .processDefinitionId("")//使用流程定义ID查询  
				// .processInstanceId("")//使用流程实例ID查询  
				// .executionId(executionId)//使用执行对象ID查询  
				/** 排序 */
				.orderByTaskCreateTime().asc()// 使用创建时间的升序排列  
				// 返回结果集  
				// .singleResult() //返回唯一的结果集  
				// .count()//返回结果集的数量  
				// .listPage(firstResult, maxResults)//分页查询  
				.list();// 返回列表  
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID：" + task.getId());
				System.out.println("任务名称:" + task.getName());
				System.out.println("任务的创建时间:" + task.getCreateTime());
				System.out.println("任务的办理人:" + task.getAssignee());
				System.out.println("流程实例ID:" + task.getProcessInstanceId());
				System.out.println("执行对象ID:" + task.getExecutionId());
				System.out.println("流程定义ID:" + task.getProcessDefinitionId());
				System.out.println("##################################################");
			}
		}
	}

}
