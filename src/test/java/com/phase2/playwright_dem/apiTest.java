package com.phase2.playwright_dem;

import java.util.HashMap;
import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.jsonpath.JsonPath;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;

public class apiTest {
	
	@Test
	public void verifyApi() {
		Playwright playwright = Playwright.create();
		APIRequestContext request = playwright.request().newContext();
		
		//post
		HashMap<Object,Object>createPayload = new HashMap<>();
		createPayload.put("userId", "123");
		createPayload.put("title", "API Testing postman");
		createPayload.put("body", "Learning API");
		APIResponse postResponse = request.post("https://jsonplaceholder.typicode.com/posts", RequestOptions.create().setData(createPayload));
		int myid = JsonPath.read(postResponse.text(), "$.id");
		System.out.println(myid);
		System.out.println(postResponse.text());
		
		//get
		APIResponse response = request.get("https://jsonplaceholder.typicode.com/posts");
		System.out.println(response.status());
		//System.out.println(response.text());
		Assert.assertTrue(response.ok());
		List<Integer> ids = JsonPath.read(response.text(), "$[*].id");
		System.out.println(ids);
		
		//get using Parameterized query
		APIResponse QPresponse = request.get("https://jsonplaceholder.typicode.com/posts/"+ids.get(4));
		System.out.println(QPresponse.text());
		
		//Put
		HashMap<Object,Object>updatePayload = new HashMap<>();
		updatePayload.put("userId", "12");
		updatePayload.put("id", "5");
		updatePayload.put("title", "API Testing postman with Playwright");
		updatePayload.put("body", "Learning API with paywright java");
		APIResponse updResponse = request.put("https://jsonplaceholder.typicode.com/posts/"+ids.get(4), RequestOptions.create().setData(updatePayload));
		System.out.println(updResponse.text());
		
		//delete
		APIResponse dltResponse = request.delete("https://jsonplaceholder.typicode.com/posts/"+ids.get(4));
		System.out.println(dltResponse.text()+ " : deleted");
	}
}
