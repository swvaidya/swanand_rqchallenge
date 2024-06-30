package com.example.rqchallenge.externalapi;

import java.util.Collections;
import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RestClient {
	private final RestTemplate restTemplate;
	
	@Retryable(retryFor = HttpClientErrorException.TooManyRequests.class,
            maxAttempts = 10,
            backoff = @Backoff(delay = 10000, multiplier = 1.5, maxDelay = 60000, random = true))
	public <T> ResponseEntity<T> execute(String url, HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<T> responseClass) {
		try {
			return restTemplate.exchange(url, httpMethod, httpEntity, responseClass);
		} catch (HttpClientErrorException.TooManyRequests exmany) {
			log.error("Failed with too many requests error");
			throw exmany;
		} catch (Exception ex) {
			log.error(String.format("Exception for url %s:", url), ex);
			throw ex;
		}
	}
	
	/**
     * @return HttpEntity with only headers
     */
	public static HttpEntity getHttpEntity() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(headers);
    }

    /**
     * @param input
     * @return HttpEntity with header and input payload body
     */
    public static HttpEntity getHttpEntity(Map<String, Object> input) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        return new HttpEntity(input, headers);
    }
}
