package com.mrporter.pomangam.common.api.kakao;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.google.gson.Gson;



/**
 * KakaoApi
 * 
 * @version 1.0 [2018. 12. 10.]
 * @author Choi
 */
public class KakaoApi {
	
	protected static final String api_url = "https://alimtalk-api.bizmsg.kr";
	protected static final String api_endpoint = "/v2/sender/send";
	
	protected static final String message_type = "at";
	protected static final String phn = "821064784899";
	protected static final String profile = "4b347dc9844c7540e81851bc41a5ea9f5e985b38";
	protected static final String tmplId = "pmg_admin_1";
	
    public static void main(String[] args) {
        try {
        	
        	HashMap<String, String> body = new HashMap<>();
    		body.put("message_type", message_type);
    		body.put("phn", phn);
    		body.put("profile", profile);
    		body.put("tmplId", tmplId);
    		body.put("msg", 
    				"주문번호 : 2 (no.2359)" + System.lineSeparator() +
    				"배달시간 : 2018-12-06 (17시 00분)" + System.lineSeparator() +
    				"※ 11시 45분에 방문 예정 ※" + System.lineSeparator() +
    				"------------------------" + System.lineSeparator() +
    				"품명 : [ 빅치킨마요 ]\n" + 
    				"개수 : 1개\n추가사항 : 없음\n요청사항 : 없음");
    		

    		List<HashMap<String, String>> lom = new ArrayList<>();
    		lom.add(body);
    		Gson gson = new Gson();
    		String json = gson.toJson(lom);
    		System.out.println(json);
    		
            URL url = new URL(api_url + api_endpoint);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("userId", "mrporter");
            con.setRequestProperty("Content-type", "application/json;charset=utf-8");
            con.setRequestProperty("Accept-Charset", "UTF-8");
            
            // post request
            con.setDoOutput(true);
            
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(json);
            wr.flush();
            wr.close();
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            System.out.println(response.toString());
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	
	
	/*
	protected static final String api_url = "https://alimtalk-api.bizmsg.kr";
	protected static final String api_endpoint = "/v2/sender/send";
	
	protected static final String message_type = "at";
	protected static final String phn = "821064784899";
	protected static final String profile = "4b347dc9844c7540e81851bc41a5ea9f5e985b38";
	protected static final String tmplId = "pmg_admin_3";
	
	public static void main(String...args) {
		
		HashMap<String, String> header = new HashMap<>();
		header.put("userId", "mrporter");
		header.put("Content-Type", "application/json");
		
		HashMap<String, String> body = new HashMap<>();
		body.put("message_type", message_type);
		body.put("phn", phn);
		body.put("profile", profile);
		body.put("tmplId", tmplId);
		body.put("msg", 
				"주문번호 : 2 (no.2359)\n" +
				"배달시간 : 2018-12-06 (17시 00분)\n" +
				"※ 11시 45분에 방문 예정 ※\n" +
				"------------------------\n" +
				"품명 : [ 빅치킨마요 ] - 1개\n\n" +
				"------------------------\n" +
				"품명 : [ 치킨제육 도시락 ] - 1개\n\n" +
				"------------------------\n" +
				"품명 : [ 소불고기 카레 ] - 2개\n\n" +
				"------------------------");
		
		String result = new KakaoApi().callApi(
				api_url + api_endpoint, 
				"POST",
				header, 
				body);
		
		System.out.println("result : " + result);
	}
	
	public String callApi(String url, String method, HashMap<String, String> header, HashMap<String, String> body) {
		String response = "";
		 
		// SSL 여부
		if (url.startsWith("https://")) {
			
		    HttpRequest request = HttpRequest.get(url);
		    // Accept all certificates
		    request.trustAllCerts();
		    // Accept all hostnames
		    request.trustAllHosts();
		}
		
		if (method.toUpperCase().equals("HEAD")) {
		} else {
		    HttpRequest request = null;
		    
		    // POST/GET 설정
		    if (method.toUpperCase().equals("POST")) {
	
				request = new HttpRequest(url, "POST");
				request.readTimeout(10000);
		
				if (header != null && !header.isEmpty()) {
				    request.headers(header);
				}
				if (body != null && !body.isEmpty()) {
					
				    request.form(body, "UTF-8");
				}
				
		    } else {
				request = HttpRequest.get(url
					+ mapToQueryString(body));
				request.readTimeout(10000);
		    }
	
		    if (request.ok()) {
		    	response = request.body();
		    } else {
		    	response = "error : " + request.code() + ", message : "
		    			+ request.body();
		    }
		    request.disconnect();
		}
	
		return response;
	}
	
	
    */
    public static String mapToQueryString(Map<String, String> map) {
		StringBuilder string = new StringBuilder();
	
		if (map.size() > 0) {
		    string.append("?");
		}
	
		for (Entry<String, String> entry : map.entrySet()) {
		    string.append(entry.getKey());
		    string.append("=");
		    string.append(entry.getValue());
		    string.append("&");
		}
	
		return string.toString();
    }
}
