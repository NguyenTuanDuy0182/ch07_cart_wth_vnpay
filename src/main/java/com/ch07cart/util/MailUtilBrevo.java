package com.ch07cart.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class MailUtilBrevo {

    // TO-DO: Điền API key từ Brevo vào đây
    private static final String BREVO_API_KEY = "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";

    // Tên người gửi và email đã xác minh trong Brevo
    private static final String SENDER_NAME = "CDList - NgTDuy";
    private static final String SENDER_EMAIL = "ngtduy4240@gmail.com";

    public static void sendMail(String toEmail, String toName, String subject, String textContent, String htmlContent)
            throws Exception {

        JsonObject payload = new JsonObject();

        JsonObject sender = new JsonObject();
        sender.addProperty("name", SENDER_NAME);
        sender.addProperty("email", SENDER_EMAIL);
        payload.add("sender", sender);

        JsonArray toArray = new JsonArray();
        JsonObject toObj = new JsonObject();
        toObj.addProperty("email", toEmail);
        if (toName != null && !toName.isEmpty()) {
            toObj.addProperty("name", toName);
        }
        toArray.add(toObj);
        payload.add("to", toArray);

        payload.addProperty("subject", subject);

        if (htmlContent != null && !htmlContent.isEmpty()) {
            payload.addProperty("htmlContent", htmlContent);
        }
        if (textContent != null && !textContent.isEmpty()) {
            payload.addProperty("textContent", textContent);
        }

        String jsonPayload = payload.toString();

        HttpClient client = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                .header("accept", "application/json")
                .header("api-key", BREVO_API_KEY)
                .header("content-type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 201 && response.statusCode() != 200 && response.statusCode() != 202) {
            throw new Exception(
                    "Lỗi khi gửi email qua Brevo. Status: " + response.statusCode() + ", Body: " + response.body());
        }
    }

    public static void sendMail(String toEmail, String toName, String subject, String body, boolean bodyIsHTML)
            throws Exception {
        if (bodyIsHTML) {
            sendMail(toEmail, toName, subject, null, body);
        } else {
            sendMail(toEmail, toName, subject, body, null);
        }
    }

    public static void sendMail(String toEmail, String toName, String subject, String textContent)
            throws Exception {
        sendMail(toEmail, toName, subject, textContent, null);
    }
}
