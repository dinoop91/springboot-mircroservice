package com.factweavers.crawlerService.service;

import com.factweavers.crawlerService.commons.Helper;
import org.apache.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by weavers on 05/07/18.
 */
@Component
public class HttpReader {

    HttpFetchRequest httpFetchRequest;
    Logger logger=Logger.getLogger(HttpReader.class.getName());



    @Value("${http.retry}")
    private int retry;

    @Value("${http.maxRetry}")
    private int maxRetry;

    @Value("${http.timeOut}")
    private int timeOut;

    @Value("${http.userAgent}")
        private String userAgent;

    String link;
    String firstUrl=null;
    public String redirectUrl=null;

    HttpReader(HttpFetchRequest httpFetchRequest){
        this.httpFetchRequest=httpFetchRequest;
    }
    public void setHttpFetchRequest(HttpFetchRequest httpFetchRequest){
        this.httpFetchRequest=httpFetchRequest;
        link=httpFetchRequest.getLink();
    }


    public HttpFetchRequest read(){
        Document doc = null;
        if(firstUrl==null){
            firstUrl=link;
        }
        try {
            logger.info("reading http link: "+link);
            Connection.Response response = Jsoup.connect(link).followRedirects(true).timeout(timeOut*1000).userAgent(userAgent).execute();
            int status = response.statusCode();
            if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER) {
                redirectUrl = response.header("location");
                logger.info("Redirect to: " + redirectUrl);
                response = Jsoup.connect(redirectUrl).followRedirects(false).timeout(timeOut*1000).userAgent(userAgent).execute();
            }
            else{
                if(response.url() !=null)
                    redirectUrl=response.url().toString();
            }
            doc=response.parse();
            if(doc!=null){
                if(doc.body().text().isEmpty()){
                    String metaUrl = doc.select("meta").attr("content").replace("'","");
                    if(metaUrl.contains("URL")){
                        link=metaUrl.substring((metaUrl.indexOf("URL=")+4));
                        return read();
                    }
                }
            }
        }
        catch(Exception e){
            if(retry<maxRetry){
                if(e.getClass().getCanonicalName().equals("java.lang.IllegalArgumentException")){
                    if(link.contains("www.")){
                        link="http://"+link.substring(link.indexOf("www."));
                    }
                }
                timeOut=timeOut+5;
                retry++;
                logger.info("retry : "+retry+" timeout: "+timeOut+" sec for link "+link);
                return read();
            }
        }

        String partnerName= Helper.getDomain(link);

        httpFetchRequest.setLink(firstUrl);
        if(httpFetchRequest.getParentUrl()==null)
            httpFetchRequest.setParentUrl(firstUrl);
        httpFetchRequest.setPartnerName(partnerName);

        if(doc!=null) {
            if (httpFetchRequest.getTitle() == null){
                try {
                    String title = doc.select("title").first().text();
                    httpFetchRequest.setTitle(title);
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
            if(redirectUrl==null)
                redirectUrl=link;
            String SourceName=Helper.getDomain(redirectUrl);
            if(SourceName==null)
                SourceName=Helper.getDomain(link);
            if(SourceName==null)
                SourceName=Helper.getDomain(firstUrl);

            httpFetchRequest.setSourceName(SourceName);
            httpFetchRequest.setRedirectedLink(redirectUrl);
            httpFetchRequest.setFetchTimeStamp(Helper.getTime());
            if(httpFetchRequest.getActualTimeStamp()==null)
                httpFetchRequest.setActualTimeStamp(Helper.getTime()); // TODO- change logic
            httpFetchRequest.setHtmlContent(doc.toString());

            List<Map<String, String>> sources=new ArrayList<Map<String, String>>();
            Map<String, String> sourceRow=new HashMap<String, String>();
            sourceRow.put("source",SourceName );
            sourceRow.put("link", httpFetchRequest.getLink());
            sources.add(sourceRow);
            httpFetchRequest.setSources(sources);
        }
        else{
            logger.error("html content couldnt fetch for link "+link);
            httpFetchRequest.setError(true);
        }
        setDefaultConfiguration();
        return httpFetchRequest;
    }
    public void setDefaultConfiguration(){
        retry=0;
        timeOut=20;
        firstUrl=null;
    }

    public static void main(String args[]){
        HttpFetchRequest httpFetchRequest=new HttpFetchRequest();
        httpFetchRequest.setLink("https://stackoverflow.com/questions/20333147/autowired-no-qualifying-bean-of-type-found-for-dependency");

        HttpReader http=new HttpReader(httpFetchRequest);

        HttpFetchRequest httpFetchRequest1=http.read();
        System.out.println("result : "+httpFetchRequest1);
    }
}
