package com.factweavers.crawlerService.service;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * Created by weavers on 07/07/18.
 */
@RestController
public class CrawlerController {


    @Autowired
    CrawlerManagement crawlerManagement;

    private Logger logger= LoggerFactory.getLogger(this.getClass());


    @RequestMapping(value = "/process", method = RequestMethod.POST)
    public String process(@RequestBody CrawlRequest crawlRequest) {
        try {
            logger.info("REQUEST: "+ crawlRequest);
            List<JSONObject> data = crawlerManagement.process(crawlRequest);
            logger.info("returning "+data.size());
            return data.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "error";
    }


    @RequestMapping(value = "/get/process", method = RequestMethod.GET)
    public String getContent(@RequestParam("type") String type, @RequestParam("link") String link) {
        try {
            CrawlRequest crawlRequest=new CrawlRequest();
            crawlRequest.setLink(link);
            crawlRequest.setType(type);
            System.out.println(crawlRequest);
            List<JSONObject> data = crawlerManagement.process(crawlRequest);
            System.out.println("in controller");
            System.out.println(data.size());

             return data.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
