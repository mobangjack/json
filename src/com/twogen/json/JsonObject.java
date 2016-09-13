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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * JsonObject.
 * @author 帮杰
 *
 */
public class JsonObject extends LinkedHashMap<String, Object> {

	private static final long serialVersionUID = -4940372756345117093L;

	public JsonObject() {
		super();
	}
	
	public JsonObject(int initialCapacity) {
		super(initialCapacity);
	}
	
	public JsonObject(Map<? extends String, ? extends Object> map) {
		super(map);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(String key) {
		if (key.contains(".")) {
			return JsonPathResolver.resolve(this, key);
		}else {
			return (T) super.get(key);
		}
	}
	
	public <T> T get(String key,T defaultValue) {
		T o = get(key);
		return o==null?defaultValue:o;
	}
	
	/**
	 * This method is derived from LinkedHashMap directly and is deprecated.
	 * Please use get(String key) instead.
	 */
	@Override
	@Deprecated
	public Object get(Object key) {
		return super.get(key);
	}
	
	/**
	 * This method is derived from LinkedHashMap directly and is deprecated.
	 * Please use get(String key, T defaultValue) instead.
	 */
	@Override
	@Deprecated
	public Object getOrDefault(Object key, Object defaultValue) {
		return super.getOrDefault(key, defaultValue);
	}
	
	@Override
	public String toString() {
		return JsonWriter.write(this);
	}
	
	@Override
	public Object clone() {
		return new JsonObject(this);
	}
	
	public static JsonObject parse(String s) {
		return JsonParser.parseJsonObject(s);
	}
}
