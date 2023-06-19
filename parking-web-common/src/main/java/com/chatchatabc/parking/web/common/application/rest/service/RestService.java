package com.chatchatabc.parking.web.common.application.rest.service;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

public interface RestService {

    /**
     * Make a GET request to the given URL with the given headers and params
     *
     * @param endpoint the url
     * @param headers  the headers
     * @param params   the params
     * @return the response
     */
    String makeGetRequest(String endpoint, Map<String, String> headers, Map<String, String> params);

    /**
     * Build a URI with query parameters
     *
     * @param endpoint the url
     * @param params   the params
     * @return the uri
     */
    UriComponentsBuilder buildUriWithQueryParams(String endpoint, Map<String, String> params);

    /**
     * Map headers to request
     *
     * @param request the request
     * @param headers the headers
     */
    void mapHeadersToRequest(WebClient.RequestHeadersSpec<?> request, Map<String, String> headers);
}
