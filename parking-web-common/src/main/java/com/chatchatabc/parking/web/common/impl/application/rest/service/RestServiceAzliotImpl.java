package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.chatchatabc.parking.web.common.application.rest.service.JwtService;
import com.chatchatabc.parking.web.common.application.rest.service.RestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RestServiceAzliotImpl implements RestService {
    // Values from .env
    @Value("${azliot.api.tf-key}")
    private String tfKey;
    @Value("${azliot.api.user-key}")
    private String userKey;
    @Value("${azliot.api.password}")
    private String password;
    @Value("${azliot.api.time}")
    private String time;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;
    private final WebClient webClient;
    private String token = null;
    private final Logger log = LoggerFactory.getLogger(RestServiceAzliotImpl.class);

    public RestServiceAzliotImpl(WebClient.Builder webClientBuilder, ObjectMapper objectMapper, JwtService jwtService) {
        this.objectMapper = objectMapper;
        this.jwtService = jwtService;
        webClientBuilder.baseUrl("http://api-tf.azliot.com");
        this.webClient = webClientBuilder.build();
    }

    public String getToken() {
        return token;
    }

    @PostConstruct
    public void init() throws JsonProcessingException {
        // On application startup generate a token
        this.token = generateToken();
    }

    /**
     * Make a GET request to the given URL with the given headers and params
     *
     * @param endpoint the url
     * @param headers  the headers
     * @param params   the params
     * @return the response
     */
    @Override
    public String makeGetRequest(String endpoint, Map<String, String> headers, Map<String, String> params) {
        if (token == null) return null;

        // Build URI with query parameters
        UriComponentsBuilder uriBuilder = this.buildUriWithQueryParams(endpoint, params);

        if (headers == null) {
            headers = new HashMap<>();
            headers.put("tfKey", this.tfKey);
            headers.put("Authorization", this.token);
        }

        // Initialize WebClient request
        WebClient.RequestHeadersSpec<?> request = webClient.get()
                .uri(uriBuilder.build().toUriString())
                .accept(MediaType.APPLICATION_JSON);

        // Add headers to the request
        this.mapHeadersToRequest(request, headers);

        // Execute GET request and return the response as String
        return request.retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Build a URI with query parameters
     *
     * @param endpoint the url
     * @param params   the params
     * @return the uri
     */
    @Override
    public UriComponentsBuilder buildUriWithQueryParams(String endpoint, Map<String, String> params) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(endpoint);
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                uriBuilder.queryParam(entry.getKey(), entry.getValue());
            }
        }

        return uriBuilder;
    }

    /**
     * Map headers to request
     *
     * @param request the request
     * @param headers the headers
     */
    @Override
    public void mapHeadersToRequest(WebClient.RequestHeadersSpec<?> request, Map<String, String> headers) {
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
        }
    }

    /**
     * Login response from Azliot
     *
     * @param results
     */
    record AzliotLoginResponse(
            AzliotTokenResult results
    ) {
    }

    /**
     * Token result from Azliot
     *
     * @param token
     */
    record AzliotTokenResult(
            String token
    ) {
    }

    /**
     * Validate token every 10 minutes
     */
    @Scheduled(fixedRate = 10 * 60 * 1000)
    public void validateToken() {
        try {
            // Buffer time in seconds (10 minutes)
            int bufferTimeInSeconds = 10 * 60;

            // If token is expired or about to expire, renew it
            if (token != null) {
                // Get the expiration timestamp from the token
                Long exp = jwtService.getExpirationFromToken(this.token);

                // Check if token is expired or about to expire
                if (exp != null && Instant.now().getEpochSecond() + bufferTimeInSeconds >= exp) {
                    log.info("Token expired or about to expire. Renewing...");
                    this.generateToken();
                }
            }
        } catch (Exception e) {
            log.error("Error renewing token: " + e.getMessage());
        }
    }


    /**
     * Generate a token
     *
     * @return the token
     */
    public String generateToken() throws JsonProcessingException {
        String endpoint = "/Transfer/transferLogin";

        Map<String, String> loginParams;

        // Login Params
        loginParams = new HashMap<>();
        loginParams.put("tfKey", tfKey);
        loginParams.put("userKey", userKey);
        loginParams.put("password", password);
        loginParams.put("time", time);

        log.info("Generating Azliot token...");

        // Build URI with query parameters
        UriComponentsBuilder uriBuilder = this.buildUriWithQueryParams(endpoint, loginParams);

        // Initialize WebClient request
        WebClient.RequestHeadersSpec<?> request = webClient.get()
                .uri(uriBuilder.build().toUriString())
                .accept(MediaType.APPLICATION_JSON);

        // Execute GET request and return the response as String
        String response = request.retrieve()
                .bodyToMono(String.class)
                .block();
        AzliotLoginResponse azliotLoginResponse = objectMapper.readValue(response, AzliotLoginResponse.class);
        log.info("Azliot token generated...");
        return azliotLoginResponse.results.token;
    }
}
