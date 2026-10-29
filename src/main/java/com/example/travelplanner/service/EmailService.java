package com.example.travelplanner.service;

import java.util.Map;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmailService {

    @Value("${BREVO_API_KEY}")
    private String brevoApiKey;

    @Value("${EMAIL_FROM}")
    private String senderEmail;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendVerificationEmail(String recipientEmail, String token) {

        String verificationLink =
                frontendUrl + "/verify-email?token=" + token;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("api-key", brevoApiKey);

        Map<String, Object> body = Map.of(
                "sender", Map.of(
                        "name", "AI Travel Planner",
                        "email", senderEmail
                ),
                "to", List.of(
                        Map.of("email", recipientEmail)
                ),
                "subject", "Verify Your Email - AI Travel Planner",
                "htmlContent",
                "<h2>Welcome to AI Travel Planner!</h2>"
                + "<p>Please verify your email address by clicking below:</p>"
                + "<p><a href=\"" + verificationLink
                + "\">Verify My Email</a></p>"
                + "<p>This verification link expires in 24 hours.</p>"
        );

        HttpEntity<Map<String, Object>> request =
                new HttpEntity<>(body, headers);

        try {
            restTemplate.postForEntity(
                    "https://api.brevo.com/v3/smtp/email",
                    request,
                    String.class
            );
        } catch (RestClientException ex) {
            throw new IllegalStateException(
                    "Unable to send verification email", ex
            );
        }
    }
}