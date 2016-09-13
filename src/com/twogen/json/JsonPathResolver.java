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
 * JsonPathResolver can resolve JSON path and extract the target data from a JsonObject directly.
 * This is helpful when what you need in a JsonObject is just a piece of data.<br>
 * @author 帮杰
 *
 */
public class JsonPathResolver {

	private static <T> T resolveNode(JsonObject currentNode, String p) {
		if (p.matches("\\w+\\[\\d+\\]")) {
			String key = p.substring(0,p.indexOf('['));
			JsonArray jsonArray = currentNode.get(key);
			int index = Integer.parseInt(p.substring(p.indexOf('[')+1, p.lastIndexOf(']')));
			return jsonArray.get(index,null);
		}else {
			return currentNode.get(p);
		}
	}
	
	public static <T> T resolve(JsonObject jsonObject,String path) {
		String[] array = path.replaceAll("\\s", "").split("\\.");
		JsonObject currentNode = jsonObject;
		int index = 0;
		while (index < array.length-1) {
			String p = array[index];
			currentNode = resolveNode(currentNode, p);
			index++;
		}
		return resolveNode(currentNode, array[index]);
	}
	
}
