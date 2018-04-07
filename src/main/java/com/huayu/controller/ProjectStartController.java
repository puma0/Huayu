package com.huayu.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.PvmTransition;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.task.Task;
import org.activiti.image.ProcessDiagramGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huayu.entity.ProjectStart;
import com.huayu.service.IProjectStartService;

/**
 * 开工申请控制器
 * @author liliang
 *
 */
@RestController
@RequestMapping(value = "/start")
public class ProjectStartController {

	protected static Logger log = LoggerFactory.getLogger(ProjectStartController.class);

	@Autowired
	IProjectStartService projectStartService;
	@Autowired
	ProcessEngine processEngine;
	@Autowired
	RuntimeService runtimeService;
	@Autowired
	ProcessEngineConfiguration processEngineConfiguration;
	@Autowired
	HistoryService historyService;
	@Autowired
	RepositoryService repositoryService;

	/**
	 * 新增开工申请
	 * @param start 开工申请对象
	 * @return
	 */
	@RequestMapping(value = "/add.do")
	public Map<String, String> add(ProjectStart start) {
		Map<String, String> resMap = new HashMap<String, String>();
		int res = projectStartService.insertSelective(start);
		if (res > 0) {
			resMap.put("stats", "1");
			resMap.put("msg", "新增开工申请成功！");
		} else {
			resMap.put("stats", "0");
			resMap.put("msg", "新增开工申请失败！");
		}
		return resMap;
	}

	/**
	 * 修改开工申请
	 * @param start 开工申请对象
	 * @return
	 */
	@RequestMapping(value = "/update.do")
	public Map<String, String> update(ProjectStart start) {
		Map<String, String> resMap = new HashMap<String, String>();
		int res = projectStartService.updateByPrimaryKeySelective(start);
		if (res > 0) {
			resMap.put("stats", "1");
			resMap.put("msg", "修改开工申请成功！");
		} else {
			resMap.put("stats", "0");
			resMap.put("msg", "修改开工申请失败！");
		}
		return resMap;
	}

	/**
	 * 删除开工申请
	 * @param start 开工申请对象
	 * @return
	 */
	@RequestMapping(value = "/delete.do")
	public Map<String, String> delete(Integer id) {
		Map<String, String> resMap = new HashMap<String, String>();
		int res = projectStartService.deleteByPrimaryKey(id);
		if (res > 0) {
			resMap.put("stats", "1");
			resMap.put("msg", "删除开工申请成功！");
		} else {
			resMap.put("stats", "0");
			resMap.put("msg", "删除开工申请失败！");
		}
		return resMap;
	}

	/**
	 * 提交开工申请
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/submitStart.do")
	public Map<String, String> submitStart(Integer id) {
		Map<String, String> resMap = new HashMap<String, String>();
		String userId = "123";

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
		return resMap;
	}

	/**
	 * 查看开工申请列表
	 * @param
	 * @return
	 */
	@RequestMapping(value = "/findMyTask.do")
	public List<Task> findMyTask() {
		String assignee = "123";
		List<Task> list = processEngine.getTaskService()//与正在执行的任务管理相关的Service  
				.createTaskQuery()//创建任务查询对象  
				/**查询条件（where部分）*/
				.taskAssignee(assignee)//指定个人任务查询，指定办理人  
				//                      .taskCandidateUser(candidateUser)//组任务的办理人查询  
				//                      .processDefinitionId(processDefinitionId)//使用流程定义ID查询  
				//                      .processInstanceId(processInstanceId)//使用流程实例ID查询  
				//                      .executionId(executionId)//使用执行对象ID查询  
				/**排序*/
				.orderByTaskCreateTime().asc()//使用创建时间的升序排列  
				/**返回结果集*/
				//                      .singleResult()//返回惟一结果集  
				//                      .count()//返回结果集的数量  
				//                      .listPage(firstResult, maxResults);//分页查询  
				.list();//返回列表  
		if (list != null && list.size() > 0) {
			for (Task task : list) {
				System.out.println("任务ID:" + task.getId());
				System.out.println("任务名称:" + task.getName());
				System.out.println("任务的创建时间:" + task.getCreateTime());
				System.out.println("任务的办理人:" + task.getAssignee());
				System.out.println("流程实例ID：" + task.getProcessInstanceId());
				System.out.println("执行对象ID:" + task.getExecutionId());
				System.out.println("流程定义ID:" + task.getProcessDefinitionId());
				System.out.println("########################################################");
			}
		}
		return list;
	}

	/*
	* 删除流程定义
	*/
	@RequestMapping(value = "/deleteProcess.do")
	public Map<String, String> deleteProcessDefinition(String deploymentId) {
		Map<String, String> resMap = new HashMap<String, String>();
		//使用部署ID，完成删除
		//String deploymentId = "1";
		/*
		 * 不带级联的删除
		 * 只能删除没有启动的流程，如果流程启动，就会抛出异常
		 */
		//        processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
		//                        .deleteDeployment(deploymentId);
		/*
		 * 能级联的删除
		 * 能删除启动的流程，会删除和当前规则相关的所有信息，正在执行的信息，也包括历史信息
		 */
		try {
			processEngine.getRepositoryService()//与流程定义和部署对象相关的Service
					.deleteDeployment(deploymentId, true);
			resMap.put("stats", "1");
			resMap.put("msg", "删除流程成功！");
		} catch (Exception e) {
			resMap.put("stats", "0");
			resMap.put("msg", "删除流程失败！");
			log.error(e.getMessage());
		}
		return resMap;
	}

	@RequestMapping("queryProcess.do")
	public void queryProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String processInstanceId = request.getParameter("ProcessInstanceId");
		//获取历史流程实例  
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery()
				.processInstanceId(processInstanceId).singleResult();
		//获取流程图  
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		processEngineConfiguration = processEngine.getProcessEngineConfiguration();
		Context.setProcessEngineConfiguration((ProcessEngineConfigurationImpl) processEngineConfiguration);

		ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
		ProcessDefinitionEntity definitionEntity = (ProcessDefinitionEntity) repositoryService
				.getProcessDefinition(processInstance.getProcessDefinitionId());

		List<HistoricActivityInstance> highLightedActivitList = historyService.createHistoricActivityInstanceQuery()
				.processInstanceId(processInstanceId).list();
		//高亮环节id集合  
		List<String> highLightedActivitis = new ArrayList<String>();

		//高亮线路id集合  
		List<String> highLightedFlows = getHighLightedFlows(definitionEntity, highLightedActivitList);

		for (HistoricActivityInstance tempActivity : highLightedActivitList) {
			String activityId = tempActivity.getActivityId();
			highLightedActivitis.add(activityId);
		}

		//中文显示的是口口口，设置字体就好了  
		InputStream imageStream = diagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitis,
				highLightedFlows, "宋体", "宋体", null, 1.0);
		//单独返回流程图，不高亮显示  
		//        InputStream imageStream = diagramGenerator.generatePngDiagram(bpmnModel);  
		// 输出资源内容到相应对象  
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}

	}

	/**  
	* 获取需要高亮的线  
	* @param processDefinitionEntity  
	* @param historicActivityInstances  
	* @return  
	*/
	private List<String> getHighLightedFlows(ProcessDefinitionEntity processDefinitionEntity,
			List<HistoricActivityInstance> historicActivityInstances) {

		List<String> highFlows = new ArrayList<String>();// 用以保存高亮的线flowId  
		for (int i = 0; i < historicActivityInstances.size() - 1; i++) {// 对历史流程节点进行遍历  
			ActivityImpl activityImpl = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i).getActivityId());// 得到节点定义的详细信息  
			List<ActivityImpl> sameStartTimeNodes = new ArrayList<ActivityImpl>();// 用以保存后需开始时间相同的节点  
			ActivityImpl sameActivityImpl1 = processDefinitionEntity
					.findActivity(historicActivityInstances.get(i + 1).getActivityId());
			// 将后面第一个节点放在时间相同节点的集合里  
			sameStartTimeNodes.add(sameActivityImpl1);
			for (int j = i + 1; j < historicActivityInstances.size() - 1; j++) {
				HistoricActivityInstance activityImpl1 = historicActivityInstances.get(j);// 后续第一个节点  
				HistoricActivityInstance activityImpl2 = historicActivityInstances.get(j + 1);// 后续第二个节点  
				if (activityImpl1.getStartTime().equals(activityImpl2.getStartTime())) {
					// 如果第一个节点和第二个节点开始时间相同保存  
					ActivityImpl sameActivityImpl2 = processDefinitionEntity
							.findActivity(activityImpl2.getActivityId());
					sameStartTimeNodes.add(sameActivityImpl2);
				} else {
					// 有不相同跳出循环  
					break;
				}
			}
			List<PvmTransition> pvmTransitions = activityImpl.getOutgoingTransitions();// 取出节点的所有出去的线  
			for (PvmTransition pvmTransition : pvmTransitions) {
				// 对所有的线进行遍历  
				ActivityImpl pvmActivityImpl = (ActivityImpl) pvmTransition.getDestination();
				// 如果取出的线的目标节点存在时间相同的节点里，保存该线的id，进行高亮显示  
				if (sameStartTimeNodes.contains(pvmActivityImpl)) {
					highFlows.add(pvmTransition.getId());
				}
			}
		}
		return highFlows;
	}

}
