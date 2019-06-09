package com.mrporter.pomangam.common.util;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * ApiClientUtil
 * 
 * @version 1.0 [2019. 3. 7.]
 * @author Choi
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mrporter.pomangam.order.vo.ApiResultBean;

public class ApiClientUtils {

    private String apiUrl;
    private RestTemplate restTemplate;
    private ObjectMapper objectMapper;

    public ApiClientUtils() {
        restTemplate = new RestTemplate();
        objectMapper = new ObjectMapper();
    }

    public ApiClientUtils(String domain, boolean isSsl) {
        this();
        apiUrl = (isSsl ? "https" : "http") + "://" + domain;
    }

    public ApiClientUtils(String domain, int apiPort, boolean isSsl) {
        this();
        apiUrl = (isSsl ? "https" : "http") + "://" + domain + ":" + apiPort;
    }

    public void sendByPost(Map<String, String> header, Map<String, Object> body, String subUrl) {
        String bodyAsString;
        try {
            List list = new ArrayList<>();
            list.add(body);
            bodyAsString = objectMapper.writeValueAsString(list);
        } catch (IOException e) {
            return;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(new MediaType("application", "json", Charset.forName("UTF-8")));
        if(header != null && !header.isEmpty()) {
            header.forEach((k, v)-> {
                headers.add(k, v);
            });
        }
        
        new Thread(() -> {
        	restTemplate.postForEntity(
			        (apiUrl + subUrl),
			        new HttpEntity(bodyAsString, headers),
			        String.class);
        }).start();
        
    }
}
