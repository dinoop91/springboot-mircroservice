package com.factweavers.crawlerService.service;

import com.algoTree.emac.client.Client;
import com.algoTree.texthelpers.string;
import com.factweavers.crawlerService.commons.CalculateEntityCount;
import com.factweavers.crawlerService.commons.EmacHelper;
import com.factweavers.crawlerService.commons.Helper;
import com.factweavers.crawlerService.sentiments.SentimenceInterface;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by weavers on 06/07/18.
 */
@Component
public class EmacService {

    Logger logger=Logger.getLogger(EmacService.class.getName());
    static Client client = null;
    SentimenceInterface senti=new SentimenceInterface();


    @Value("${emac.host}")
    private String emacHost;
    @Value("${emac.port}")
    private String emacPort;


    //String emacHost="harvester.octobuz.com";
	/*String emacHost="188.166.254.191";
	String emacPort="1984";*/

    CalculateEntityCount countEntities = new CalculateEntityCount();

    HttpFetchRequest httpFetchRequest;

    EmacService(HttpFetchRequest httpFetchRequest){
        this.httpFetchRequest=httpFetchRequest;
    }

    public HttpFetchRequest read(HttpFetchRequest httpFetchRequest){
        try {
            httpFetchRequest.setEmacEnabled(true);

            String aceOutput=httpFetchRequest.getContent();
            if(string.isNullOrEmpty(aceOutput)){
                logger.error("ace output is null");
                return httpFetchRequest;
            }

            if(client == null){
                logger.info("creating emac client"+emacHost+" "+emacPort);
                client = new Client(emacHost,Integer.parseInt(emacPort));
            }
            if(!client.getEMACStatus()){
                logger.error("emac is not up "+emacHost+" "+emacPort);
                httpFetchRequest.setEMACStatus(false);
                return httpFetchRequest;
            }
            String emacVersion=client.getVersion();
            //logger.info("emac version>> "+emacVersion);
            String content =EmacHelper.removeTableTags(aceOutput);
            if(string.isNullOrEmpty(content)){
                logger.error("ace content null");
                return httpFetchRequest;
            }
            String stripContent = EmacHelper.getStrippedContent(content);
            if (!string.isNullOrEmpty(stripContent)) {
                JSONObject jSEmac = new JSONObject();
                jSEmac.put("Type", "public");
                jSEmac.put("Content", stripContent);
                try {
                    String entity = client.extractEntities(jSEmac.toString());
                    if (entity != null && entity.length() != 0) {
                        entity=	countEntities.modifyAndGetJSON(entity, Helper.getTime());

                        JSONObject categoryJson=new JSONObject(entity);
                        categoryJson.put("SentimentOut", senti.getSentiment(stripContent));

                        httpFetchRequest.setEMAC_VERSION(emacVersion);
                        httpFetchRequest.setCategories(categoryJson);
                        httpFetchRequest.setWordCount(EmacHelper.getWordCount(stripContent));
                    }
                    if (entity == null) {
                        logger.error("EMAC throwing Exception ");
                    }
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            } else{
                logger.error("stripContent is null");
                return httpFetchRequest;
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return httpFetchRequest;
    }


    public List<JSONObject> getEvents(HttpFetchRequest httpFetchRequest){
        List<JSONObject> outputData=new ArrayList<JSONObject>();
        JSONObject categories=httpFetchRequest.getCategories();
        if(categories==null || !categories.has("Types")){
            logger.error("no types in categories");
            return outputData;
        }

        JSONArray sources=new JSONArray();
        JSONObject source=new JSONObject();
        source.put("sourceType", httpFetchRequest.getTags());
        source.put("title", httpFetchRequest.getTitle());
        source.put("timeStamp", httpFetchRequest.getActualTimeStamp());
        source.put("source", httpFetchRequest.getSourceName());
        source.put("link", httpFetchRequest.getLink());
        source.put("parentLink", httpFetchRequest.getParentUrl());
        String parent=Helper.getDocumentId(httpFetchRequest.getTitle(),httpFetchRequest.getLink() );
        source.put("parent", parent);
        sources.put(source);

        JSONObject types= (JSONObject) categories.get("Types");
        if(!types.has("Events")){
            logger.error("no events in "+httpFetchRequest.getLink());
            return outputData;
        }
        JSONArray events=(JSONArray) types.get("Events");
        for(int i=0; i<events.length(); i++){
            JSONObject event=(JSONObject) events.get(i);
            String eventName=event.getString("event");
            //System.out.println(eventName);
            if(eventName=="GeneralEvent" || eventName.equals("GeneralEvent"))
                continue;
            //System.out.println(eventName);
            JSONObject eventObj=event.getJSONObject(eventName);
            JSONArray eventsArr=(JSONArray) eventObj.get("events");
            for(int j=0; j<eventsArr.length(); j++){
                JSONObject eventDoc=(JSONObject) eventsArr.get(j);
                JSONObject outputEvent=new JSONObject();
                JSONArray outputEvents=new JSONArray();
                String eventText=null;
                String eventType=null;
                JSONArray entities=null;
                JSONArray subTypes=null;

                if(eventDoc.has("event"))
                    eventText=eventDoc.getString("event");
                if(eventDoc.has("Type"))
                    eventType=eventDoc.getString("Type");
                if(eventDoc.has("entities"))
                    entities=eventDoc.getJSONArray("entities");
                if(eventDoc.has("SubType"))
                    subTypes=eventDoc.getJSONArray("SubType");
                if(eventType==null || eventText==null ){
                    logger.error("eventType null");
                    continue;
                }
                if(eventType=="GeneralEvent" || eventType.equals("GeneralEvent")){
                    continue;
                }
                outputEvent.put("Entities", entities);
                outputEvent.put("Event", eventText);
                outputEvent.put("occurences", 1);
                outputEvent.put("CharLength", eventText.length());
                outputEvent.put("WordLength", eventText.split(" ").length);
                outputEvent.put("Sources", sources);
                outputEvents.put(outputEvent);

                JSONObject outputDocument=new JSONObject();
                outputDocument.put("TimeStamp",httpFetchRequest.getActualTimeStamp());
                outputDocument.put("dailyPriority", 0);
                outputDocument.put("lastUpdated", httpFetchRequest.getFetchTimeStamp());
                outputDocument.put("Occurences", 1);
                outputDocument.put("Type", eventType);
                outputDocument.put("SubType", subTypes);
                outputDocument.put("Events", outputEvents);
                outputData.add(outputDocument);
            }
        }
        return outputData;
    }


}
