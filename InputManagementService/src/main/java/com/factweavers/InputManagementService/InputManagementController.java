package com.factweavers.InputManagementService;

import com.factweavers.AppConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by weavers on 29/06/18.
 */
@RestController
public class InputManagementController {


    @Autowired
    private AppConfig config;

    @Autowired
    private InputManagementService inputManagementService;

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @GetMapping("/crawler/start")
    public String start(){
        try{

            logger.info("+++++++++++++++++crawler start function ");
            inputManagementService.start();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "started";
    }

    @GetMapping("/crawler/stop")
    public String stop(){
        try{
            inputManagementService.stop();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "stopped";
    }


    @GetMapping("/crawler/restart")
    public String restart(){
        try{
        }catch (Exception e){
            inputManagementService.restart();
            e.printStackTrace();
        }
        return "restarted";
    }


    @GetMapping("/crawler/status")
    public String status(){
        try{
            return inputManagementService.status();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "running";
    }

}
