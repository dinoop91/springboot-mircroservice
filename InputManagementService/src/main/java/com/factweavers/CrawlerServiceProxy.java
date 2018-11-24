package com.factweavers;


import org.json.JSONObject;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Created by weavers on 10/07/18.
 */

//@FeignClient(name="crawler-service", url="localhost:9090") // sample for fiegn (without ribbon)
//@FeignClient(name="crawler-service")
@FeignClient(name="netflix-zuul-api-gateway-server")
@RibbonClient(name="crawler-service")
public interface CrawlerServiceProxy {

    //@RequestMapping(value = "/process", method = RequestMethod.POST)
    @RequestMapping(value = "/crawler-service/process", method = RequestMethod.POST)
    public String process(@RequestBody CrawlRequest crawlRequest) ;


    @RequestMapping(value = "/get/process", method = RequestMethod.GET)
    public String getContent(@RequestParam("type") String type, @RequestParam("link") String link) ;
}
