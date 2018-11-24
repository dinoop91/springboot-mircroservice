package com.factweavers;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


/**
 * Created by Dinoop on 02/07/18.
 */




@Configuration
@Component
public class AppConfig {

    @Value("${elasticsearch.ip}")
    private String ip;

    @Value("${elasticsearch.port}")
    private int port;

    @Value("${elasticsearch.cluster}")
    private String cluster;

    @Value("${elasticsearch.inputIndex}")
    private String inputIndex;

    @Value("${elasticsearch.inputType}")
    private String inputType;



    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public String getInputIndex() {
        return inputIndex;
    }

    public void setInputIndex(String inputIndex) {
        this.inputIndex = inputIndex;
    }

    public String getInputType() {
        return inputType;
    }

    public void setInputType(String inputType) {
        this.inputType = inputType;
    }
}