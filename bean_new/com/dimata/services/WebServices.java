/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dimata.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author IanRizky
 */
public class WebServices {
	
	public static JSONObject getAPI(String payload, String url) {
            JSONObject object = new JSONObject();
            
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpGet httpRequest = new HttpGet(url);
            HttpResponse httpResponse;
            String httpResult = "";
            try {
                httpResponse = httpClient.execute(httpRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    httpResult = EntityUtils.toString(httpResponse.getEntity());
                    object = new JSONObject(httpResult);
                }
            } catch (Exception exc) {
            }
            return object;
        }
        
        public static JSONObject postAPI(String payload, String url) {
            JSONObject object = new JSONObject();
            StringEntity entity = new StringEntity(payload,
                    ContentType.APPLICATION_FORM_URLENCODED);

            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpRequest = new HttpPost(url);
            httpRequest.setEntity(entity);
            HttpResponse httpResponse;
            String httpResult = "";
            try {
                httpResponse = httpClient.execute(httpRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    httpResult = EntityUtils.toString(httpResponse.getEntity());
                    object = new JSONObject(httpResult);
                }
            } catch (Exception exc) {
            }
            return object;
        }
        
        public static JSONObject postAPIUsingJSON(String payload, String url) {
            JSONObject object = new JSONObject();
            StringEntity entity = new StringEntity(payload,
                    ContentType.APPLICATION_JSON);
            HttpClient httpClient = HttpClientBuilder.create().build();
            HttpPost httpRequest = new HttpPost(url);
            httpRequest.setEntity(entity);
//            httpRequest.setHeader("Accept", "application/json");
            httpRequest.setHeader("Content-Type", "application/json");
            HttpResponse httpResponse;
            String httpResult = "";
            try {
                httpResponse = httpClient.execute(httpRequest);
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    httpResult = EntityUtils.toString(httpResponse.getEntity());
                    object = new JSONObject(httpResult);
                }
            } catch (Exception exc) {
            }
            return object;
        }
        
       private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    public static JSONObject getAPIWithParam(String payload, String url, String param) {
		JSONObject object = new JSONObject();

		HttpClient httpClient = HttpClientBuilder.create().build();
		String urlx = url + "?" + param;
		System.out.println("Get data from " + urlx);
		HttpGet httpRequest = new HttpGet(urlx);
		HttpResponse httpResponse;
		String httpResult = "";
		try {
			httpResponse = httpClient.execute(httpRequest);
			HttpEntity httpEntity = httpResponse.getEntity();
			if (httpEntity != null) {
				httpResult = EntityUtils.toString(httpResponse.getEntity());
				object = new JSONObject(httpResult);
			}
		} catch (Exception exc) {
			System.out.println("Err: " + exc.getMessage());
			exc.printStackTrace();
		}
		return object;
	}
    
	public static String encodeUrl(String url){
		String cvt = "";
		try {
			cvt = URLEncoder.encode(url, "UTF-8");
		} catch (Exception e) {
		}
		return cvt;
	}
 
	public static String decodeUrl(String url){
		String cvt = "";
		try {
			cvt = URLDecoder.decode(url, "UTF-8");
		} catch (Exception e) {
		}
		return cvt;
	}
	
}
