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


/**
 * JsonParser parses JsonObject or JsonArray from JSON String.
 * @author 帮杰
 *
 */
public class JsonParser {

	/**
	 * Parse JsonObject from the given string
	 * @param s
	 * @return
	 */
	public static JsonObject parseJsonObject(String s) {
		//extract the useful part and trim
		s = trim(extract(s, "{", "}"));
		//prepare to parse
		JsonObject jsonObject = new JsonObject();
		//starting index of parsing
		int i1 = 0;
		//ending index of parsing
		int i2 = getIndextOfSeparativeComma(s, i1);
		//if i2<0,that is,if there is only one element
		if (i2<0) {
			parseKeyValue(jsonObject, s);
			return jsonObject;
		}
		//more than one element
		while (i2>0) {
			parseKeyValue(jsonObject, s.substring(i1, i2));
			i1 = i2+1;
			i2 = getIndextOfSeparativeComma(s, i1);
		}
		//last element
		parseKeyValue(jsonObject, s.substring(i1));
		return jsonObject;
	}
	
	/**
	 * Parse key-value pair.
	 * @param jsonObject
	 * @param keyValueString
	 */
	private static void parseKeyValue(JsonObject jsonObject,String keyValueString) {
		//index of :
    	int i = keyValueString.indexOf(':');
    	//get key
    	String k = extract(keyValueString.substring(0, i), "\"", "\"");
    	//parse value
    	Object v = parse(keyValueString.substring(i+1));
    	//put it to jsonObject
		jsonObject.put(k, v);
	}
	
	/**
	 * Parse JsonArray from the given string
	 * @param s
	 * @return JsonArray
	 */
	public static JsonArray parseJsonArray(String s) {
		//extract the useful part and trim
		s = trim(extract(s, "[", "]"));
		//prepare to parse
        JsonArray jsonArray = new JsonArray();
        //starting index of element(inclusive)
        int i1 = 0;
        //ending index of element(exclusive)
        int i2 = getIndextOfSeparativeComma(s, i1);
        //if i2<0,that is,if there is only one element
        if(i2<0) {
        	jsonArray.add(parse(s));
        	return jsonArray;
        }
        //more than one element
        while (i2>0) {
        	parseElement(jsonArray, s.substring(i1, i2));
    		i1 = i2+1;
    		i2 = getIndextOfSeparativeComma(s, i1);
		}
        //last element
        parseElement(jsonArray, s.substring(i1));
		return jsonArray;
	}

	private static void parseElement(JsonArray jsonArray,String elementString) {
		Object element = parse(elementString);
		jsonArray.add(element);
	}
	
	/**
	 * parse JSON-type value to java-type value.
	 * @param s JSON-type value
	 * @return Corresponding java-type value
	 */
	private static Object parse(String s) {
		s = trim(s);
		//null
		if (s.equals("null")) {
			return null;
		}
		//String
		if (s.startsWith("\"")&&s.endsWith("\"")) {
			//extract string content
			s = s.substring(1, s.length()-1);
			//strip the escaping chars
			s = stripEscapingChars(s);
			return s;
		}
		//Boolean
		if (s.equals("true")) {
			return true;
		}else if (s.equals("false")) {
			return false;
		}
		//Integer or Long
		if (s.matches("[+-]?\\d+")) {
			try {return Integer.parseInt(s);} catch (Exception ex) {}
			try {return Long.parseLong(s);} catch (Exception ex) {}
		}
		//Float or Double
		if (s.matches("[+-]?\\d+\\.\\d+")) {
			try {return Float.parseFloat(s);} catch (Exception ex) {}
			try {return Double.parseDouble(s);} catch (Exception ex) {}
		}
		//JsonObject
		if (s.startsWith("{")&&s.endsWith("}")) {
			try {return parseJsonObject(s);} catch (Exception ex) {}
		}
		//JsonArray
		if (s.startsWith("[")&&s.endsWith("]")) {
			try {return parseJsonArray(s);} catch (Exception ex) {}
		}
		//treat it as a normal String if the type is not supported
		return s;
	}
	
	/**
	 * Get the index of separative comma.
	 * @param s
	 * @param fromIndex
	 * @return
	 */
	private static int getIndextOfSeparativeComma(String s,int fromIndex) {
		int index = s.indexOf(',', fromIndex);
		if (index>0) {
			if (!isSeparativeComma(s, index)) {
				return getIndextOfSeparativeComma(s, index+4);
			}
		}
		return index;
	}
	
	/**
	 * Judge if the comma in the given index of the string is separative comma
	 * @param s
	 * @param fromIndex The starting index to consider
	 * @param index Index of the comma
	 * @return
	 */
	private static boolean isSeparativeComma(String s,int index) {
		int quote = 0;
		int openingBraket = 0;
		int closingBraket = 0;
		int openingBrace = 0;
		int closingBrace = 0;
		for (int i = 0; i < index; i++) {
			char c = s.charAt(i);
			if (c=='\"') {
				if (i==0) {
					quote++;
				}else if (s.charAt(i-1)!='\\') {
					quote++;
				}
			}
			if (c=='[') {
				openingBraket++;
			}
			if (c==']') {
				closingBraket++;
			}
			if (c=='{') {
				openingBrace++;
			}
			if (c=='}') {
				closingBrace++;
			}
		}
		return (quote%2==0)&&(openingBraket==closingBraket)&&(openingBrace==closingBrace);
	}
	
	private static String extract(String s,String o,String c) {
		int i = s.indexOf(o);
		int j = s.lastIndexOf(c);
		return s.substring(i+1, j);
	}
	
	private static String trim(String s) {
		int i;
		for (i = 0; i < s.length(); i++) {
			if (!(""+s.charAt(i)).matches("\\s")) {
				break;
			}
		}
		int j;
		for (j = s.length()-1; j > -1; j--) {
			if (!(""+s.charAt(j)).matches("\\s")) {
				break;
			}
		}
		return s.substring(i,j+1);
	}
	
	/**
	 * Strip the escaping chars
	 * @param s
	 * @return
	 */
	private static String stripEscapingChars(String s) { 
		s = s.replaceAll("\\\"", "\"")
			 .replaceAll("\\\\", "\\")
			 .replaceAll("\\b", "\b")
			 .replaceAll("\\f", "\f")
			 .replaceAll("\\n", "\n")
			 .replaceAll("\\r", "\r")
			 .replaceAll("\\t", "\t");
		return s;
	}
	
}
