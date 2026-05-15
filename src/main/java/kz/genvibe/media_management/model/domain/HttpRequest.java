package kz.genvibe.media_management.model.domain;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Builder
@Getter
public class HttpRequest {

    private String url;
    private HttpMethod method;
    private HttpHeaders headers;
    private Object body;

}