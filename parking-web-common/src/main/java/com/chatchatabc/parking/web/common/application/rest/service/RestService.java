package com.chatchatabc.parking.web.common.application.rest.service;

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
     * Generate a token
     *
     * @return the token
     */
    String generateToken();
}
