package com.assisto.virtualassistant.service;

import com.assisto.virtualassistant.VinDecoderResponse;
import com.assisto.virtualassistant.model.Vehicle;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.hibernate.internal.util.config.ConfigurationHelper.extractValue;

@Service
public class VinDecoderService {

    private final RestTemplate restTemplate;

    @Value("${vindecoder.api.key}")
    private String apiKey;

    @Value("${vindecoder.secret.key}")
    private String secretKey;

    public VinDecoderService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Vehicle decodeVin(String vin) {
        String id = "decode";
        String upperVin = vin.toUpperCase();
        String controlSum = generateControlSum(upperVin, id, apiKey, secretKey);

        String url = UriComponentsBuilder.fromHttpUrl("https://api.vindecoder.eu/3.2")
                .pathSegment(apiKey, controlSum, "decode", upperVin + ".json")
                .toUriString();

        System.out.println("Request URL: " + url);
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("Response: " + response.getBody());

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            VinDecoderResponse vinDecoderResponse = objectMapper.readValue(response.getBody(), VinDecoderResponse.class);
            return mapToVehicle(vin, vinDecoderResponse);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Vehicle mapToVehicle(String vin, VinDecoderResponse vinDecoderResponse) {
        Vehicle vehicle = new Vehicle();
        vehicle.setVin(vin);

        List<VinDecoderResponse.Decode> decodeList = vinDecoderResponse.getDecode();
        for (VinDecoderResponse.Decode decode : decodeList) {
            String value = extractValue(decode.getValue());
            switch (decode.getLabel()) {
                case "Make":
                    vehicle.setMake(value);
                    break;
                case "Model":
                    vehicle.setModel(value);
                    break;
                case "Model Year":
                    vehicle.setYear(value);
                    break;
                case "Engine":
                    vehicle.setEngine(value);
                    break;
                case "Fuel Type":
                    vehicle.setFuelType(value);
                    break;
            }
        }

        return vehicle;
    }
    public String extractValue(Object value) {
        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof List) {
            List<String> valueList = (List<String>) value;
            return String.join(", ", valueList);
        }
        return "";
    }
    private String generateControlSum(String vin, String id, String apiKey, String secretKey) {
        try {
            String input = vin + "|" + id + "|" + apiKey + "|" + secretKey;
            MessageDigest messageDigest = MessageDigest.getInstance("SHA-1");
            byte[] hashBytes = messageDigest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashString = new StringBuilder();
            for (byte b : hashBytes) {
                hashString.append(String.format("%02x", b));
            }
            return hashString.substring(0, 10);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating control sum", e);
        }
    }

    public static boolean checkVIN(String vin) {
        return vin != null && vin.length() == 17;
    }

    public String buildReadableMessage(Vehicle vehicle) {
        return String.format("Marca: %s\nModel: %s\nAnul: %s\nMotor: %s\nCombustibil: %s",
                vehicle.getMake(), vehicle.getModel(), vehicle.getYear(), vehicle.getEngine(), vehicle.getFuelType());
    }
}