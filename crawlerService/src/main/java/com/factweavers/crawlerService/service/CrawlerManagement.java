package com.factweavers.crawlerService.service;

import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weavers on 05/07/18.
 */

@Component
public class CrawlerManagement {

    Logger logger=Logger.getLogger(CrawlerManagement.class.getName());
    CrawlRequest crawlRequest;

    @Autowired
    HttpReader httpReader;
    @Autowired
    AceService aceService;
    @Autowired
    EmacService emacService;




    public  List<JSONObject> process(CrawlRequest crawlRequest){
        List<JSONObject> data=new ArrayList<>();
        try{


            logger.info("Processing "+crawlRequest);
            String type=crawlRequest.getType();
            if(type==null){
                logger.info("Type is null");
                return null;
            }

            List<HttpFetchRequest> httpFetchRequests=new ArrayList<>();
            if(type.equals("rss")){
                RssReader rssReader=new RssReader(crawlRequest);
                httpFetchRequests=rssReader.read();
            }
            else if(type.equals("http")){
                HttpFetchRequest httpFetchRequest=new HttpFetchRequest();
                httpFetchRequest.setLink(crawlRequest.getLink());
                httpFetchRequest.setTags("News");

                httpFetchRequests.add(httpFetchRequest);
            }
            if(httpFetchRequests==null || httpFetchRequests.size()==0){
                logger.error("Failed ");
                return null;
            }

            for(HttpFetchRequest request: httpFetchRequests){
                logger.info("Calling http crawl "+ request);
                httpReader.setHttpFetchRequest(request);
                request=httpReader.read();

                //request.setHtmlContent(null);

                //logger.info(request);
                //aceService=new AceService(request);
                request=aceService.read(request);

                if(crawlRequest.isEsEnabled()) {
                    request = emacService.read(request);
                }


                if(request.getCategories() == null){
                    logger.warn("emac content null, error "+request.getLink());
                }

                List<JSONObject> events = emacService.getEvents(request);
                //logger.info(request);

                JSONObject urlData=new JSONObject( request );
                urlData.put("events", events);
                data.add(urlData);
            }
            System.out.println("data ");
            System.out.println(data);

        }catch (Exception e){
            e.printStackTrace();
        }
        return data;
    }

    public static void main(String[] args) {
       /* CrawlRequest crawlRequest=new CrawlRequest();
        *//*crawlRequest.set_id("AWRa1a65IKesQ5yL2aDB");
        crawlRequest.setLink("https://stackoverflow.com/questions/20333147/autowired-no-qualifying-bean-of-type-found-for-dependency");
        crawlRequest.setType("http");
        crawlRequest.setFetchedTill("04-07-2018 17:38:48");
        crawlRequest.setStatus("success");*//*

        crawlRequest.setLink("https://stackoverflow.com/questions/20333147/autowired-no-qualifying-bean-of-type-found-for-dependency");
        crawlRequest.setType("http");

        CrawlerManagement crawlerManagement=new CrawlerManagement(crawlRequest);
        crawlerManagement.process();*/
    }
}
