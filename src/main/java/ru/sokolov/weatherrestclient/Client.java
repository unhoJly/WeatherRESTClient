package ru.sokolov.weatherrestclient;

import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Client {
    public static void main(String[] args) {
        Random random = new Random();
        final String sensorName = "Sensor123";
        double maxTemperature = 45.0;

        registerSensor(sensorName);

        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            sendMeasurement(random.nextDouble() * maxTemperature, random.nextBoolean(), sensorName);
        }
    }

    private static void registerSensor(String sensorName) {
        Map<String, Object> jsonData = new HashMap<>();
        final String url = "http://localhost:8080/sensors/registration";

        jsonData.put("name", sensorName);
        makePostRequestWithJSONData(url, jsonData);
    }

    private static void sendMeasurement(double value, boolean raining, String sensorName) {
        Map<String, Object> jsonData = new HashMap<>();
        final String url = "http://localhost:8080/measurements/add";

        jsonData.put("value", value);
        jsonData.put("raining", raining);
        jsonData.put("sensor", Map.of("name", sensorName));
        makePostRequestWithJSONData(url, jsonData);
    }

    private static void makePostRequestWithJSONData(String url, Map<String, Object> jsonData) {
        final RestTemplate restTemplate = new RestTemplate();
        final HttpHeaders headers = new HttpHeaders();
        HttpEntity<Object> request = new HttpEntity<>(jsonData, headers);

        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            restTemplate.postForObject(url, request, String.class);
            System.out.println("Измерение успешно отправлено на сервер!");
        } catch (HttpClientErrorException e) {
            System.out.println("ОШИБКА!");
            System.out.println(e.getMessage());
        }
    }
}