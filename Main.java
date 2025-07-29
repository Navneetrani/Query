package com.bajajhealth.javaQualifier;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@SpringBootApplication
public class JavaQualifierApplication {
    public static void main(String[] args) {
        SpringApplication.run(JavaQualifierApplication.class, args);
    }
}

@Component
class AppStartupRunner {

    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    public void runAfterStartup() {
        String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

        JSONObject requestBody = new JSONObject();
        requestBody.put("name", "Tanvi Goel");
        requestBody.put("regNo", "REG12347");
        requestBody.put("email", "tanvi@example.com");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);

        System.out.println("Response: " + response.getBody());

        // You need to extract webhook and accessToken from response, solve SQL, and send it.
    }
}
