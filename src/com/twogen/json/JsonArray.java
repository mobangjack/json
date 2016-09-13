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

import java.util.ArrayList;
import java.util.Collection;

/**
 * JsonArray
 * @author 帮杰
 *
 */
public class JsonArray extends ArrayList<Object> {

	private static final long serialVersionUID = -2343319495855801294L;

	public JsonArray() {
		super();
	}
	
	public JsonArray(int initialCapacity) {
		super(initialCapacity);
	}
	
	public JsonArray(Collection<? extends Object> c) {
		super(c);
	}
	
	@SuppressWarnings("unchecked")
	public <T> T get(int index,T defaultValue) {
		return get(index)==null?defaultValue:(T) get(index);
	}
	
	@Override
	public String toString() {
		return JsonWriter.write(this);
	}

	@Override
	public Object clone() {
		return new JsonArray(this);
	}
	
	public JsonArray parse(String s) {
		return JsonParser.parseJsonArray(s);
	}
}
