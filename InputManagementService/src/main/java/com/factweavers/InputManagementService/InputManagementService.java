package com.factweavers.InputManagementService;


import com.factweavers.AppConfig;
import com.factweavers.CrawlRequest;
import com.factweavers.CrawlerServiceProxy;
import com.factweavers.HttpFetchRequest;
import com.factweavers.elasticsearch.ElasticSearch;
import com.factweavers.elasticsearch.ElasticsearchInterface;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weavers on 01/07/18.
 */

@Component
public class InputManagementService {

    @Autowired
    ElasticsearchInterface esInterface;

    @Autowired
    private CrawlerServiceProxy proxy;

    Status status=Status.STOPPED;

    Thread thread;


    public void start(){
        try {
            System.out.println("status : "+status);
           esInterface.showConfigs();
            if(!Status.RUNNING.equals(status)) {
                System.out.println("creating thread ");
                thread = new Thread() {
                    public void run() {
                        //execute();
                        // esInterface.start();
                        List<Map<String, Object>> inputs=new ArrayList<>();
                        Map<String,Object> map=new HashMap<>();
                        map.put("type", "http");
                        map.put("link", "http://localhost/cf/test1.html");
                        inputs.add(map);
                        Map<String,Object> map1=new HashMap<>();
                        map1.put("type", "http");
                        map1.put("link", "http://localhost/cf/japan.html");
                        inputs.add(map1);

                        for(Map<String, Object> input: inputs){
                            System.out.println(input);


                            String link=(String) input.get("link");
                            String type=(String) input.get("type");

                            CrawlRequest crawlRequest=new CrawlRequest();
                            crawlRequest.setLink(link);
                            crawlRequest.setType(type);


                            //---------------- Method-1 : WITHOUT FEIGN _ ALTERNATIVE METHOD // simple way of calling other services
                            /*ResponseEntity<List> responseEntity= new RestTemplate(). postForEntity(
                                    "http://localhost:9090/process",
                                    crawlRequest,
                                    List.class
                            );
                            System.out.println(responseEntity.getBody());*/
                            //----------------


                            //---------------- Method-2 , using simple proxy
                            String response= proxy.process(crawlRequest);
                            System.out.println(response);
                            //----------------
                            //String response= proxy.getContent(type, link);
                            //System.out.println(response);




                            System.out.println("post ");
                        }
                    }
                };
                thread.start();
                System.out.println("Thread Running " + thread.getId());
                status=Status.RUNNING;
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        try {
            System.out.println("stopping thread "+thread.getId());
            thread.stop();
            status=Status.STOPPED;
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void restart(){
        try {
            System.out.println("Restarting thread "+thread.getId());
            thread.stop();
            status=Status.STOPPED;
            start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public String status(){
        try {
            JSONObject statusJSON=new JSONObject().put("status", status);
            return statusJSON.toString();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void execute(){
        while(true){
            for(int j=0;j <1000000000; j++){
                for(int k=0;k <100; k++){
                }
            }
            System.out.println("In execute function..");
        }
    }


    public static void main(String[] args) {
        InputManagementService inputManagementService=new InputManagementService();
        System.out.println("Calling thread.start()");
        inputManagementService.start();
        System.out.println("Calling thread.interrupt()");
        inputManagementService.stop();
    }
}
