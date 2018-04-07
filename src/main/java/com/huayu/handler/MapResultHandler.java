package com.huayu.handler;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.ibatis.session.ResultContext;
import org.apache.ibatis.session.ResultHandler;

public class MapResultHandler implements ResultHandler {

	@SuppressWarnings("rawtypes")
	private final Map mappedResults = new LinkedHashMap();

	@SuppressWarnings("unchecked")
	@Override
	public void handleResult(ResultContext context) {
		@SuppressWarnings("rawtypes")
		Map map = (Map) context.getResultObject();
		mappedResults.put(map.get("key"), map.get("value")); // xml 配置里面的property的值，对应的列
	}

	public Map getMappedResults() {
		return mappedResults;
	}

}
