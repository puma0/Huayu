package com.huayu;

import java.util.List;

import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mybatis.generator.api.ShellRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class WorkFlowTest {

	@Autowired
	private ProcessEngine processEngine;

	/**部署流程定义*/
	@Test
	public void deploymentProcessDefinition() {
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service  
				.createDeployment()//创建一个部署对象  
				.name("helloworld入门程序")//添加部署的名称  
				.addClasspathResource("diagrams/helloworld.bpmn")//从classpath的资源中加载，一次只能加载一个文件  
				.addClasspathResource("diagrams/helloworld.png")//从classpath的资源中加载，一次只能加载一个文件  
				.deploy();//完成部署  
		System.out.println("部署ID：" + deployment.getId());//1  
		System.out.println("部署名称：" + deployment.getName());//helloworld入门程序    
	}

	@Test
	public void flowStart() {
		RuntimeService runtimeService = processEngine.getRuntimeService();
		//用key启动时按照最新的流程图版本定义启动  
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("HelloWorldKey");
	}

	@Test
	public void findMyPersonTask() {
		String assignee = "test3";
		List<Task> taskList = processEngine.getTaskService()//获取任务service  
				.createTaskQuery()//创建查询对象  
				.taskAssignee(assignee)//指定查询人  
				.list();
		if (taskList.size() > 0) {
			for (Task task : taskList) {
				System.out.println("代办任务ID:" + task.getId());
				System.out.println("代办任务name:" + task.getName());
				System.out.println("代办任务创建时间:" + task.getCreateTime());
				System.out.println("代办任务办理人:" + task.getAssignee());
				System.out.println("流程实例ID:" + task.getProcessInstanceId());
				System.out.println("执行对象ID:" + task.getExecutionId());
			}
		}
	}

	@Test
	public void completeTask() {
		String taskId = "2504";
		processEngine.getTaskService().complete(taskId);//完成任务  
		System.out.println("完成任务，任务ID" + taskId);
	}

	public static void main(String[] args) {
		args = new String[] { "-configfile", "src\\main\\resources\\mybatis-generator-config.xml", "-overwrite" };
		ShellRunner.main(args);
	}

}
