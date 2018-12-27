package com.robot.channel.proxy.utils;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.json.JSONObject;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class JSONMapperConvertor {

	
	private static ObjectMapper mapper = new ObjectMapper();
	private static JsonFactory jsonFactory = new JsonFactory();

	static {
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.setVisibility(PropertyAccessor.GETTER, Visibility.ANY);
		mapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, false);
	}
	
	public static <T> Object fromJson(String jsonAsString, Class<T> pojoClass) {
		Object object = null;
		try {
			object = mapper.readValue(jsonAsString, pojoClass);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return object;
	}

	public static Map<String, Object> convertJsonToMap(Object o) throws Exception {
		JSONObject obj = new JSONObject(JSONMapperConvertor.toJson(o, false));
		Stack<JSONObject> stObj = new Stack<JSONObject>();
		stObj.push(obj);
		Map<String, Object> resultMap = new HashMap<String, Object>();
		JSONMapperConvertor.JsonToMap(stObj, resultMap);
		return resultMap;
	}
	@SuppressWarnings({ "rawtypes"})
	private static void JsonToMap(Stack<JSONObject> stObj, Map<String, Object> resultMap) throws Exception {
		if (stObj == null || stObj.pop() == null) {
			return;
		}
		JSONObject json = stObj.pop();
		Iterator it = json.keys();
		while (it.hasNext()) {
			String key = (String) it.next();
			Object value = json.get(key);
			if (value instanceof JSONObject) {
				stObj.push((JSONObject) value);
				JsonToMap(stObj, resultMap);
			} else {
				resultMap.put(key, value);
			}
		}
	}

	public static <T> Object fromJson(FileReader fr, Class<T> pojoClass) throws JsonParseException, IOException {
		return mapper.readValue(fr, pojoClass);
	}

	@SuppressWarnings("deprecation")
	public static String toJson(Object pojo, boolean prettyPrint)
			throws JsonMappingException, JsonGenerationException, IOException {
		StringWriter sw = new StringWriter();
		JsonGenerator jg = jsonFactory.createJsonGenerator(sw);
		if (prettyPrint) {
			jg.useDefaultPrettyPrinter();
		}
		mapper.writeValue(jg, pojo);
		return sw.toString();
	}

	@SuppressWarnings("deprecation")
	public static void toJson(Object pojo, FileWriter fw, boolean prettyPrint)
			throws JsonMappingException, JsonGenerationException, IOException {
		JsonGenerator jg = jsonFactory.createJsonGenerator(fw);
		if (prettyPrint) {
			jg.useDefaultPrettyPrinter();
		}
		mapper.writeValue(jg, pojo);
	}

	public static String writeObjectAsString(Object obj) {
		String jsonstr = "";
		try {
			if (obj != null) {
				jsonstr = mapper.writeValueAsString(obj);
			}
		} catch (JsonGenerationException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return jsonstr;
	}

}