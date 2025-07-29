package com.bajajhealth.javaQualifier;

import jakarta.annotation.PostConstruct;
import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

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
        try {
            // Step 1: Send POST request to generate webhook
            String url = "https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA";

            JSONObject requestBody = new JSONObject();
            requestBody.put("name", "Navneet Rani");
            requestBody.put("regNo", "REG00001");
            requestBody.put("email", "navneetrani147@gmail.com");

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> request = new HttpEntity<>(requestBody.toString(), headers);

            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Webhook Response: " + response.getBody());

            // Step 2: Extract webhook and token
            JSONObject parsedResponse = new JSONObject(response.getBody());
            String webhookUrl = parsedResponse.getString("webhook");
            String token = parsedResponse.getString("accessToken");

            // Step 3: SQL Query
            String finalSQL = "SELECT c.name FROM Orders o JOIN Customers c ON o.customer_id = c.customer_id GROUP BY c.name HAVING SUM(o.amount) > 10000;";

            JSONObject submission = new JSONObject();
            submission.put("finalQuery", finalSQL);

            HttpHeaders authHeaders = new HttpHeaders();
            authHeaders.setContentType(MediaType.APPLICATION_JSON);
            authHeaders.setBearerAuth(token);

            HttpEntity<String> sqlRequest = new HttpEntity<>(submission.toString(), authHeaders);
            ResponseEntity<String> result = restTemplate.postForEntity(webhookUrl, sqlRequest, String.class);

            System.out.println("Submitted Result: " + result.getBody());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
