package com.huayu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricTaskInstance;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.identity.Authentication;
import org.activiti.engine.impl.persistence.entity.GroupEntity;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.persistence.entity.UserEntity;
import org.activiti.engine.impl.pvm.PvmActivity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.impl.pvm.process.ProcessDefinitionImpl;
import org.activiti.engine.impl.pvm.process.TransitionImpl;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Comment;
import org.activiti.engine.task.Task;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.huayu.entity.ProjectStart;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/applicationContext.xml" })
public class ProjectStartTest {

	@Autowired
	private ProcessEngine processEngine;

	/**部署流程定义*/
	@Test
	public void deploymentProcessDefinition() {
		Deployment deployment = processEngine.getRepositoryService()//与流程定义和部署对象相关的Service  
				.createDeployment()//创建一个部署对象  
				.name("开工申请")//添加部署的名称  
				.addClasspathResource("diagrams/StartProcess.bpmn")//从classpath的资源中加载，一次只能加载一个文件  
				.addClasspathResource("diagrams/StartProcess.png")//从classpath的资源中加载，一次只能加载一个文件  
				.deploy();//完成部署  
		System.out.println("部署ID：" + deployment.getId());//1  
		System.out.println("部署名称：" + deployment.getName());//helloworld入门程序
		System.out.println("部署ID：" + deployment.getId());//
		System.out.println("部署名称：" + deployment.getName());//
		/** 添加用户角色组 */
		IdentityService identityService = processEngine.getIdentityService();//
		// 创建角色
		identityService.saveGroup(new GroupEntity("业务员"));
		identityService.saveGroup(new GroupEntity("市场部部长"));
		identityService.saveGroup(new GroupEntity("经营考核组"));
		// 创建用户
		identityService.saveUser(new UserEntity("张三"));
		identityService.saveUser(new UserEntity("李四"));
		identityService.saveUser(new UserEntity("李五"));
		identityService.saveUser(new UserEntity("王五"));
		// 建立用户和角色的关联关系
		identityService.createMembership("张三", "业务员");
		identityService.createMembership("李四", "市场部部长");
		identityService.createMembership("李五", "市场部部长");
		identityService.createMembership("王五", "经营考核组");
		System.out.println("添加组织机构成功");

	}

	/**流程开始**/
	@Test
	public void flowStart() {
		String id = "1";
		RuntimeService runtimeService = processEngine.getRuntimeService();
		Map<String, String> resMap = new HashMap<String, String>();
		String userId = "张三";
		Map<String, Object> variables = new HashMap<String, Object>();
		variables.put("inputUser", userId);//发起用户  
		//格式：实体类.id的形式（使用流程变量）  
		String key = ProjectStart.class.getSimpleName();
		String objId = key + "." + id;
		variables.put("objId", objId);
		//使用流程定义的key，启动流程实例，同时设置流程变量，  
		//同时向正在执行的执行对象表中的字段BUSINESS_KEY添加业务数据，同时让流程关联业务  
		try {
			runtimeService.startProcessInstanceByKey(key, objId, variables);
			resMap.put("stats", "1");
			resMap.put("msg", "提交开工申请成功！");
		} catch (Exception e) {
			resMap.put("stats", "0");
			resMap.put("msg", "提交开工申请失败！");
		}
		System.out.println(resMap.get("msg"));
	}

	/**根据用户查询任务列表**/
	@Test
	public void findMyPersonTask() {
		String assignee = "张三";
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
				System.out.println("审核意见：" + task.getProcessVariables());
			}
		}
	}

	/**获取批注内容**/
	@Test
	public void getProcessComments() {
		String taskId = "47506";
		List<Comment> historyCommnets = new ArrayList<>();
		//         1) 获取流程实例的ID
		Task task = processEngine.getTaskService().createTaskQuery().taskId(taskId).singleResult();
		ProcessInstance pi = processEngine.getRuntimeService().createProcessInstanceQuery()
				.processInstanceId(task.getProcessInstanceId()).singleResult();
		//       2）通过流程实例查询所有的(用户任务类型)历史活动   
		List<HistoricActivityInstance> hais = processEngine.getHistoryService().createHistoricActivityInstanceQuery()
				.processInstanceId(pi.getId()).activityType("userTask").list();
		//       3）查询每个历史任务的批注
		for (HistoricActivityInstance hai : hais) {
			String historytaskId = hai.getTaskId();
			List<Comment> comments = processEngine.getTaskService().getTaskComments(historytaskId);
			// 4）如果当前任务有批注信息，添加到集合中
			if (comments != null && comments.size() > 0) {
				historyCommnets.addAll(comments);
			}
		}
		//       5）返回
		if (historyCommnets != null && historyCommnets.size() > 0) {
			for (int i = 0; i < historyCommnets.size(); i++) {
				Comment comment = historyCommnets.get(i);
				System.out.println(comment.getId());
				System.out.println(comment.getFullMessage());
				System.out.println(comment.getProcessInstanceId());
				System.out.println(comment.getTaskId());
				System.out.println(comment.getType());
				System.out.println(comment.getUserId());
				System.out.println(comment.getTime());
			}
		}
	}

	/**统计当前用户及所属组任务数**/
	@Test
	public void myTask() {
		String currentUser = "李四"; //当前用户
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
		System.out.println(resMap);
	}

	/**提交任务**/
	@Test
	public void completeTask() {
		String taskId = "2506";
		processEngine.getTaskService().complete(taskId);//完成任务  
		System.out.println("完成任务，任务ID" + taskId);
	}

	/**
	 * 按组查询任务
	 */
	@Test
	public void findMyGroupTask() {
		String group = "市场部部长";
		List<Task> taskList = processEngine.getTaskService()//获取任务service  
				.createTaskQuery()//创建查询对象 
				.taskCandidateGroup(group)
				//.taskAssignee(assignee)//指定查询人  
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

	/**
	 * 申领任务
	 */
	@Test
	public void ClaimTask() {
		String taskId = "5002";
		String userId = "李四";
		processEngine.getTaskService().claim(taskId, userId);
	}

	/**
	 * 回退任务到上一个节点
	 * @param procInstId
	 * @param destTaskKey
	 * @param rejectMessage
	 */
	@Test
	public void rejectTask() {
		String taskId = "5002";
		//退回上一个节点
		TaskService taskService = processEngine.getTaskService();
		HistoryService historyService = processEngine.getHistoryService();
		RepositoryService repositoryService = processEngine.getRepositoryService();
		RuntimeService runtimeService = processEngine.getRuntimeService();
		try {
			Map<String, Object> variables;
			// 取得当前任务.当前任务节点
			HistoricTaskInstance currTask = historyService.createHistoricTaskInstanceQuery().taskId(taskId)
					.singleResult();
			// 取得流程实例，流程实例
			ProcessInstance instance = runtimeService.createProcessInstanceQuery()
					.processInstanceId(currTask.getProcessInstanceId()).singleResult();
			if (instance == null) {
				System.out.println("流程结束");
				System.out.println("出错啦！流程已经结束");
			}
			variables = instance.getProcessVariables();
			// 取得流程定义
			ProcessDefinitionEntity definition = (ProcessDefinitionEntity) ((RepositoryServiceImpl) repositoryService)
					.getDeployedProcessDefinition(currTask.getProcessDefinitionId());
			if (definition == null) {
				System.out.println("流程定义未找到");
				System.out.println("出错啦！流程定义未找到");
			}
			// 取得上一步活动
			ActivityImpl currActivity = ((ProcessDefinitionImpl) definition)
					.findActivity(currTask.getTaskDefinitionKey());

			//也就是节点间的连线
			List<PvmTransition> nextTransitionList = currActivity.getIncomingTransitions();
			// 清除当前活动的出口
			List<PvmTransition> oriPvmTransitionList = new ArrayList<PvmTransition>();
			//新建一个节点连线关系集合

			List<PvmTransition> pvmTransitionList = currActivity.getOutgoingTransitions();
			//
			for (PvmTransition pvmTransition : pvmTransitionList) {
				oriPvmTransitionList.add(pvmTransition);
			}
			pvmTransitionList.clear();

			// 建立新出口
			List<TransitionImpl> newTransitions = new ArrayList<TransitionImpl>();
			for (PvmTransition nextTransition : nextTransitionList) {
				PvmActivity nextActivity = nextTransition.getSource();
				ActivityImpl nextActivityImpl = ((ProcessDefinitionImpl) definition).findActivity(nextActivity.getId());
				TransitionImpl newTransition = currActivity.createOutgoingTransition();
				newTransition.setDestination(nextActivityImpl);
				newTransitions.add(newTransition);
			}
			// 完成任务
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(instance.getId())
					.taskDefinitionKey(currTask.getTaskDefinitionKey()).list();
			Authentication.setAuthenticatedUserId("李四");//批注人的名称  一定要写，不然查看的时候不知道人物信息
			for (Task task : tasks) {
				taskService.addComment(task.getId(), null, "开工时间有问题，不同意");
				taskService.complete(task.getId(), variables);
				historyService.deleteHistoricTaskInstance(task.getId());
			}
			// 恢复方向
			for (TransitionImpl transitionImpl : newTransitions) {
				currActivity.getOutgoingTransitions().remove(transitionImpl);
			}
			for (PvmTransition pvmTransition : oriPvmTransitionList) {
				pvmTransitionList.add(pvmTransition);
			}
			System.out.println("OK");
			System.out.println("流程结束");

		} catch (Exception e) {
			System.out.println("出错啦！流程已经结束");
		}
	}

}
