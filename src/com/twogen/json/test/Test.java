/**
 * Copyright (c) 2011-2015, Mobangjack 莫帮杰 (jsonObject@foxmail.com).
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
package com.twogen.json.test;

import com.twogen.json.JsonArray;
import com.twogen.json.JsonObject;
import com.twogen.json.JsonParser;

/**
 * Test
 * @author 帮杰
 *
 */
public class Test {

	private JsonObject jsonObject;
	private String json;
	
	public Test() {
		creatJsonObject();
	    json = jsonObject.toString();
	}
	
	private void creatJsonObject() {
		
		JsonObject review = new JsonObject();
		review.put("time", "2015-11-14 00:00:00");
		review.put("author", "Tom");
		review.put("content", "hello");
		
		JsonArray reviews = new JsonArray();
		reviews.add(review);
		
        JsonObject article1 = new JsonObject();
        article1.put("time", "2015-11-14 00:00:00");
        article1.put("author", "Tom");
        article1.put("title", "My family");
        article1.put("content", "not finished yet.");
        article1.put("reviews", reviews);
        
        JsonObject article2 = new JsonObject();
        article2.put("time", "2015-11-15 00:00:00");
        article2.put("author", "John");
        article2.put("title", "My college life");
        article2.put("content", "not finished yet.");
        article2.put("reviews", reviews);
        
        JsonArray articles = new JsonArray();
        articles.add(article1);
        articles.add(article2);
        
        JsonObject topic1 = new JsonObject();
        topic1.put("banner", "What's your opinion about gambling?");
        topic1.put("articles", articles);
        
        JsonObject topic2 = new JsonObject();
        topic2.put("banner", "What's your opinion about gambling?");
        topic2.put("articles", articles);
        
        JsonArray topics = new JsonArray();
        topics.add(topic1);
        topics.add(topic2);
        
        jsonObject = new JsonObject();
        jsonObject.put("topics", topics);
	}
	
	public JsonObject getJsonObject() {
	    return jsonObject;
	}
	
	public String getJsonString() {
		return json;
	}
	
	public void testSerializationBenchMark(int times) {
		System.out.println("-----------------Test Serialization BenchMark------------------");
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			creatJsonObject();
			jsonObject.toString();
		}
		long t2 = System.currentTimeMillis();
		long time = t2-t1;
		System.out.println("Doing serialzing for "+times+" times took "+time+" ms");
	}
	
	public void testDeserializeBenchMark(int times) {
		System.out.println("-----------------Test Deserialization BenchMark------------------");
		long t1 = System.currentTimeMillis();
		for (int i = 0; i < times; i++) {
			JsonParser.parseJsonObject(json);
		}
		long t2 = System.currentTimeMillis();
		long time = t2-t1;
		System.out.println("Doing deserialzing for "+times+" times took "+time+" ms.");
	}
	
	public void testJsonPath() {
		String path = "topics[0].articles[0].reviews[0]";
		System.out.println("path:"+path);
		System.out.println("result:"+jsonObject.get(path));
	}
	
	public static void test() {
		Test test = new Test();
		println(test.getJsonString());
		test.testJsonPath();
		test.testSerializationBenchMark(10000);
		test.testDeserializeBenchMark(10000);
	}
	
	public static void main(String[] args) {
		test();
	}
	
	public static void println(Object o) {
		System.out.println(o);
	}
}
