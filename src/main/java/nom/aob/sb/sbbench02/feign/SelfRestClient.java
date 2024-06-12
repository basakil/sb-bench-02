package nom.aob.sb.sbbench02.feign;

import nom.aob.sb.sbbench02.model.SimpleResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@FeignClient(value = "selfrest", url = "#{'${sbbench.clients.selfrest.url}'}")
public interface SelfRestClient {

    @RequestMapping(method = RequestMethod.GET, value = "/rest/simple")
    ResponseEntity<SimpleResponse> getSimple();
}
