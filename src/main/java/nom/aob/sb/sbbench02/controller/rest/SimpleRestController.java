package nom.aob.sb.sbbench02.controller.rest;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import nom.aob.sb.sbbench02.feign.SelfRestClient;
import nom.aob.sb.sbbench02.model.SimpleResponse;
import nom.aob.sb.sbbench02.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Slf4j
@RestController
@RequestMapping(SimpleRestController.PATH)
public class SimpleRestController {

    private final RestTemplate restTemplate;
    private final SelfRestClient selfRestClient;

    public static final String PATH_SIMPLE = "/simple";
    public static final String PATH_PROXY = "/proxy";
    public static final String PATH = "/rest";
    public static final String SCHEME_HTTP = "http://";


    @Value("${sbbench.logstring:#{null}}")
    private String logString;

    @Value("${sbbench.rest.path.feign:feign}")
    private String feignProxyUrl;

    @Autowired
    public SimpleRestController(RestTemplate restTemplate, SelfRestClient selfRestClient) {
        this.restTemplate = restTemplate;
        this.selfRestClient = selfRestClient;
    }

    @GetMapping(PATH_SIMPLE)
    public @NonNull ResponseEntity<SimpleResponse> simpleResponse(
            @RequestHeader(value = "x-b3-traceid", required = false) String traceId) {

//        log.info("in simpleResponse: " + traceId);
        if (logString != null) {
            log.info("in simpleResponse. logString = {}.",logString);
        }

        SimpleResponse simpleResponse = Utils.newSimpleResponse(PATH_SIMPLE);

        return new ResponseEntity<>(simpleResponse, HttpStatus.OK);
    }

    @GetMapping(PATH_PROXY+"/{address}/{port}")
    public @NonNull ResponseEntity<SimpleResponse> simpleProxy(
            @RequestHeader(value = "x-b3-traceid", required = false) String traceId,
            @PathVariable("address") String address,
            @PathVariable("port") Integer port) {

//        log.info("in simpleProxy: " + traceId);
        if (logString != null) {
            log.info("in simpleProxy. logString = {}.", logString);
        }

        String resourceUrl = SCHEME_HTTP + address + ":" + port + PATH + PATH_SIMPLE;

        ResponseEntity<SimpleResponse> response
                = restTemplate.getForEntity(resourceUrl, SimpleResponse.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            SimpleResponse simpleResponse = response.getBody();
            if (simpleResponse != null) {
                simpleResponse.setPathString(PATH_PROXY + "/" + address + "/" + port);
                return new ResponseEntity<>(simpleResponse, response.getStatusCode());
            }
        }

        return response;
    }

    @GetMapping("#{'${sbbench.rest.path.feign:feign}'}")
    public @NonNull ResponseEntity<SimpleResponse> feignProxy(
            @RequestHeader(value = "x-b3-traceid", required = false) String traceId) {

//        log.info("in simpleProxy: " + traceId);
        if (logString != null) {
            log.info("in feignProxy. logString = {}.", logString);
        }


        ResponseEntity<SimpleResponse> response
                = selfRestClient.getSimple();

        if (response.getStatusCode().is2xxSuccessful()) {
            SimpleResponse simpleResponse = response.getBody();
            if (simpleResponse != null) {
                simpleResponse.setPathString(feignProxyUrl);
                return new ResponseEntity<>(simpleResponse, response.getStatusCode());
            }
        }

        return response;
    }
}
