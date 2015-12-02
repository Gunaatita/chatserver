package com.gunaatita.demo.chatserver.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StringUtils {

	public static Map<String, List<String>> getRequestParameters(String urlStr) {
        Map<String, List<String>> map = new HashMap<>();
        if (urlStr == null || urlStr.indexOf('?') == -1) {
            return map;
        }
        urlStr = urlStr.substring(urlStr.indexOf('?') + 1);
        String[] sarr = urlStr.split("\\&");
        for (String s : sarr) {
            if (s.indexOf('=') == -1) {
                continue;
            }
            String key = s.substring(0, s.indexOf('='));
            String val = s.substring(s.indexOf('=') + 1);
            List<String> list = map.get(key);
            if (list == null) {
                list = new ArrayList<>();
                map.put(key, list);
            }
            list.add(val);
        }
        return map;
    }

    public static String getStringParam(Map<String, List<String>> requestParams, String paramName) {
        if (requestParams == null) {
            return null;
        }
        List<String> list = requestParams.get(paramName);
        if (list != null && list.size() > 0) {
            String s = list.get(0);
            return s;
        }
        return null;
    }
    
    public static long getLongParam(Map<String, List<String>> requestParams, String paramName, long defaulValue) {
        if (requestParams == null) {
            return defaulValue;
        }
        List<String> list = requestParams.get(paramName);
        if (list != null && list.size() > 0) {
        	try{
        		String s = list.get(0);
        		long i = Long.parseLong(s.trim());
        		return i;
        	}catch(Exception e){
        		
        	}
        }
        return defaulValue;
    }
	
}
