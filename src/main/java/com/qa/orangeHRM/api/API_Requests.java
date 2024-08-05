package com.qa.orangeHRM.api;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.microsoft.playwright.Page;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONArray;
import org.json.JSONObject;
import org.testng.asserts.SoftAssert;

import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class API_Requests {
    private static Page page;
    SoftAssert softAssert = new SoftAssert();

    public API_Requests(Page page) {
        this.page = page;
    }

    public String getCookies() {
        String cookies = null;
        String filePath = "applogin.json"; // Replace with your file path

        try (FileReader reader = new FileReader(filePath)) {
            // Read the entire file into a String
            StringBuilder jsonString = new StringBuilder();
            int ch;
            while ((ch = reader.read()) != -1) {
                jsonString.append((char) ch);
            }

            // Parse the JSON string
            JSONObject jsonObject = new JSONObject(jsonString.toString());
            JSONArray cookiesArray = jsonObject.getJSONArray("cookies");

            for (int i = 0; i < cookiesArray.length(); i++) {
                JSONObject cookie = cookiesArray.getJSONObject(i);
                String name = cookie.getString("name");
                String value = cookie.getString("value");

                System.out.println("Name: " + name);
                System.out.println("Value: " + value);
                cookies = name + "=" + value;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return cookies;
    }

    private RequestSpecification getRequestSpecification() {
        RequestSpecification request = RestAssured.given();
        request.header("Content-Type", "application/json");
        request.header("Cookie", getCookies());
        return request;
    }

    public String get_request(String api_url, Map<String, String> params) throws InterruptedException {
        RequestSpecification request = getRequestSpecification();
        if (params != null && !params.isEmpty()) {
            request.params(params);
        }
        Response response = request.get(api_url);
        return processResponse(response);
    }

    // Overloaded method for no parameters
    public String get_request(String api_url) throws InterruptedException {
        return get_request(api_url, null);
    }

//    public JsonPath post_request(String api_url, String requestBody) throws InterruptedException {
//        RequestSpecification request = getRequestSpecification();
//        request.body(requestBody);
//        Response response = request.post(api_url);
//        return processResponse(response);
//    }

//    public JsonPath put_request(String api_url, String requestBody) throws InterruptedException {
//        RequestSpecification request = getRequestSpecification();
//        request.body(requestBody);
//        Response response = request.put(api_url);
//        return processResponse(response);
//    }

//    public JsonPath delete_request(String api_url) throws InterruptedException {
//        RequestSpecification request = getRequestSpecification();
//        Response response = request.delete(api_url);
//        return processResponse(response);
//    }

    private String processResponse(Response response) throws InterruptedException {
        int code = response.getStatusCode();
        Thread.sleep(2000);
        softAssert.assertEquals(code, 200, "Did not return 200 status code");

        String response_data = response.getBody().asString();

        return response_data;
    }

}
