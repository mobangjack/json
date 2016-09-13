/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (mobangjack@foxmail.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.twogen.json;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;



/**
 * JsonWriter writes java object to JSON string.
 * @author 帮杰
 *
 */
@SuppressWarnings("rawtypes")
public class JsonWriter {

	private String datePatern = "yyyy-MM-dd hh:mm:ss";
	
	public JsonWriter() {
	}
	
	public String getDatePatern() {
		return datePatern;
	}

	public void setDatePatern(String datePatern) {
		this.datePatern = datePatern;
	}

	public void write(StringBuilder sb,Object value) {
		if (value==null) {
			writeNull(sb);
		}else if (value instanceof Boolean) {
			writeBoolean(sb, (Boolean)value);
		}else if (value instanceof Number) {
			writeNumber(sb, (Number)value);
		}else if (value instanceof String) {
			writeString(sb, (String)value);
		}else if (value instanceof Date) {
			writeDate(sb, (Date)value);
		}else if (value instanceof Map) {
			writeJsonObject(sb, (Map)value);
		}else if (value instanceof List) {
			writeJsonArray(sb, (List)value);
		}else {
			writeObject(sb, value);
		}
	}
	
	protected void writeNull(StringBuilder sb) {
		sb.append("null");
	}
	
	protected void writeBoolean(StringBuilder sb,Boolean value) {
		sb.append(value);
	}
	
	protected void writeNumber(StringBuilder sb,Number value) {
		sb.append(value);
	}
	
	protected void writeString(StringBuilder sb, String value) {
		String s = escape(value);
		sb.append("\""+s+"\"");
	}

	protected void writeDate(StringBuilder sb, Date value) {
		String s = new SimpleDateFormat(getDatePatern()).format(value);
		writeString(sb, s);
	}

	protected void writeJsonObject(StringBuilder sb, Map value) {
		sb.append("{");
		Set keySet = value.keySet();
		for(Object key: keySet){
			sb.append("\"" + key + "\":");
			write(sb, value.get(key));
			sb.append(",");
		}
		sb.replace(sb.length()-1, sb.length(), "}");
	}

	protected void writeJsonArray(StringBuilder sb, List value) {
		sb.append("[");
		for (Object object : value) {
			write(sb, object);
			sb.append(",");
		}
		sb.replace(sb.length()-1, sb.length(), "]");
	}

	protected void writeObject(StringBuilder sb, Object value) {
		sb.append("{");
		Method[] methods = value.getClass().getDeclaredMethods();
		for (Method method : methods) {
			String name = method.getName();
			if ((name.startsWith("get")||name.startsWith("is"))&&method.getParameterCount()==0) {
				try {
					int i = name.startsWith("is") ? 2 : 3;
					int j = i+1;
					String key = name.substring(i,j).toLowerCase()+name.substring(j);
					Object object = method.invoke(value);
					sb.append("\"" + key + "\":");
					write(sb, object);
					sb.append(",");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		sb.replace(sb.length()-1, sb.length(), "}");
	}
	
	/**
	 * escape "，\，\b，\f，\n，\r，\t & 4 HEX digits
	 * @param s
	 * @return
	 */
	protected static String escape(String s) { 
	    StringBuilder sb = new StringBuilder(s.length()+20); 
	    for (int i=0; i<s.length(); i++) { 
	        char c = s.charAt(i); 
	        switch (c) { 
	        case '\"': 
	            sb.append("\\\""); 
	            break; 
	        case '\\': 
	            sb.append("\\\\"); 
	            break; 
	        case '\b': 
	            sb.append("\\b"); 
	            break; 
	        case '\f': 
	            sb.append("\\f"); 
	            break; 
	        case '\n': 
	            sb.append("\\n"); 
	            break; 
	        case '\r': 
	            sb.append("\\r"); 
	            break; 
	        case '\t': 
	            sb.append("\\t"); 
	            break; 
	        default: 
	        	if ((c >= '\u0000' && c <= '\u001F') || (c >= '\u007F' && c <= '\u009F') || (c >= '\u2000' && c <= '\u20FF')) {
					String str = Integer.toHexString(c);
					sb.append("\\u");
					for(int k=0; k<4-str.length(); k++) {
						sb.append('0');
					}
					sb.append(str.toUpperCase());
				}else {
					sb.append(c); 
				}
	        } 
	    } 
	    return sb.toString(); 
	 }
	
	private static final JsonWriter DEFAULT = new JsonWriter();
	
	public static String write(Object object) {
		StringBuilder sb = new StringBuilder();
		DEFAULT.write(sb, object);
		return sb.toString();
	}
}
