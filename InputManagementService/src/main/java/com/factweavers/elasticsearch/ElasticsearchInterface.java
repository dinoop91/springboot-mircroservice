package com.factweavers.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.factweavers.AppConfig;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.search.SearchHit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ElasticsearchInterface {


    @Autowired
    private AppConfig config;


    ElasticSearch es=new ElasticSearch();


    public void showConfigs(){
        System.out.println("IP======="+config.getIp());
    }

    //TODO - change implementation. Instead of returning all docs, use scroll id
    public List<Map<String, Object>> start() {

        System.out.println("reading inputs");
        Client client=es.createClient(config.getIp(), config.getPort(), config.getCluster());

        List<Map<String, Object>> inputs=new ArrayList<>();
        SearchResponse response = null;
        int scrollSize = 1000;
        int i = 0;
        boolean isExist=es.isIndexExist(config.getInputIndex());
        if(isExist ==false){
            System.out.println("input index does not exist:"+config.getInputIndex());
            return inputs;
        }
        while( response == null || response.getHits().hits().length != 0){
            response = client.prepareSearch(config.getInputIndex())
                    .setTypes(config.getInputType())
                    //.setQuery("{\"term\":{\"isFetched\":\"false\"}}")
                    .setSize(scrollSize)
                    .setFrom(i * scrollSize)
                    .execute()
                    .actionGet();
            for(SearchHit hit : response.getHits()){
                Map<String, Object> hitSource = hit.getSource();
                hitSource.put("_id", hit.getId());
                inputs.add(hitSource);
            }
            i++;
        }
        System.out.println(inputs.size()+" inputs found");
        return inputs;

    }

}

