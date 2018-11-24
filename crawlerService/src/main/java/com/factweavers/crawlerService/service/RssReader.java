package com.factweavers.crawlerService.service;

import com.factweavers.crawlerService.commons.Helper;
import com.sun.syndication.feed.synd.SyndEntry;
import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.io.SyndFeedInput;
import com.sun.syndication.io.XmlReader;
import org.apache.log4j.Logger;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * Created by weavers on 05/07/18.
 */
public class RssReader {


    Logger logger=Logger.getLogger(RssReader.class.getName());

    CrawlRequest crawlRequest;
    String link;
    String fetchedTill;

    RssReader(CrawlRequest crawlRequest){
        this.crawlRequest=crawlRequest;
        link=crawlRequest.getLink();
        fetchedTill=crawlRequest.getFetchedTill();
    }



    public List<HttpFetchRequest> read()  {
        List<HttpFetchRequest> httpFetchRequests=new ArrayList<>();
        try{
            logger.info("reading rss link "+link);
            URL url = new URL(link);
            HttpURLConnection httpcon = (HttpURLConnection)url.openConnection();
            // Reading the feed
            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(httpcon));
            List entries = feed.getEntries();
            Iterator itEntries = entries.iterator();

            while (itEntries.hasNext()) {
                SyndEntry entry = (SyndEntry) itEntries.next();

                String actualTimestamp=null;
                if(entry.getPublishedDate() != null){
                    actualTimestamp=entry.getPublishedDate().toString();
                    actualTimestamp= Helper.formatDate(actualTimestamp);
                }
                boolean isBefore=false;
                if(fetchedTill==null || fetchedTill=="null" || fetchedTill.equals("null")){

                }else{
                    isBefore= Helper.isDateBefore(actualTimestamp, fetchedTill);
                }
                if(isBefore==true){
                    continue;
                }

                HttpFetchRequest httpFetchRequest=new HttpFetchRequest();
                httpFetchRequest.setLink(entry.getLink());
                httpFetchRequest.setParentUrl(link);
                httpFetchRequest.setTitle(entry.getTitle());
                httpFetchRequest.setAuthor(entry.getAuthor());
                httpFetchRequest.setActualTimeStamp(actualTimestamp);

                System.out.println(httpFetchRequest);
                httpFetchRequests.add(httpFetchRequest);
            }
        }catch(Exception e){
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return httpFetchRequests;
    }

    public static void main(String[] args) {

        CrawlRequest crawlRequest=new CrawlRequest();
        crawlRequest.setLink("http://online.wsj.com/xml/rss/3_7198.xml");
        crawlRequest.setType("rss");

        RssReader rssReader=new RssReader(crawlRequest);
        rssReader.read();

    }

}
