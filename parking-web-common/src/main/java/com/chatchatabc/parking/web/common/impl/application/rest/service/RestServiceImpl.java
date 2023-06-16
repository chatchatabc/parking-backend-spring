package com.chatchatabc.parking.web.common.impl.application.rest.service;

import com.chatchatabc.parking.web.common.application.rest.service.RestService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Service
public class RestServiceImpl implements RestService {
    private final WebClient webClient;
//    private String token = null;

    public RestServiceImpl(WebClient.Builder webClientBuilder) {
        webClientBuilder.baseUrl("http://api-tf.azliot.com");
        this.webClient = webClientBuilder.build();

        // Generate Token
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
//        if (token == null) return null;

        // Build URI with query parameters
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromPath(endpoint);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            uriBuilder.queryParam(entry.getKey(), entry.getValue());
        }

        // Initialize WebClient request
        WebClient.RequestHeadersSpec<?> request = webClient.get()
                .uri(uriBuilder.build().toUriString())
                .accept(MediaType.APPLICATION_JSON);

        // Add headers to the request
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            request.header(entry.getKey(), entry.getValue());
        }

        // Execute GET request and return the response as String
        return request.retrieve()
                .bodyToMono(String.class)
                .block();
    }

    /**
     * Generate a token
     *
     * @return the token
     */
    @Override
    public String generateToken() {
        return null;
    }
}
