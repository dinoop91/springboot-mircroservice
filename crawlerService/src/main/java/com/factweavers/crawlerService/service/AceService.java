package com.factweavers.crawlerService.service;

import com.algoTree.acex.Article;
import com.algoTree.acex.ContentExtractor;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Created by weavers on 06/07/18.
 */

@Component
public class AceService {



    Logger logger = Logger.getLogger(AceService.class.getName());
    ContentExtractor contentExtractor = new ContentExtractor();

    @Value("${ace.extractionAlgo1}")
    private String extractionAlgo1;

    @Value("${ace.extractionAlgo2}")
    private String extractionAlgo2;

    @Value("${ace.extractionAlgo3}")
    private String extractionAlgo3;

    HttpFetchRequest httpFetchRequest;

    AceService(HttpFetchRequest httpFetchRequest){
        this.httpFetchRequest=httpFetchRequest;
    }



    public HttpFetchRequest read(HttpFetchRequest httpFetchRequest) {

        try {
            String html = httpFetchRequest.getHtmlContent();
            if (html == null) {
                logger.error("html content is null");
                return httpFetchRequest;
            }

            String[] extractionAlgorithms={extractionAlgo1, extractionAlgo2, extractionAlgo3};

            Article article=null;
            for(String extractionAlgorithm: extractionAlgorithms){
                logger.info("Extract using "+extractionAlgorithm);
                List<String> lsMultiPages = new ArrayList<String>();
                article = contentExtractor.extractContent(html, extractionAlgorithm, lsMultiPages);
                if (article.getCleanedArticleText() != null) {
                    httpFetchRequest.setExtractionAlg(extractionAlgorithm);
                    break;
                }
            }



            String aceContent = article.getCleanedArticleText();
            if (aceContent == null) {
                logger.error("ace content null");
                return httpFetchRequest;
            }
            if (httpFetchRequest.getTitle()==null) {
                httpFetchRequest.setTitle(article.getTitle());
            }
            httpFetchRequest.setContent(removeImageTags(aceContent));
            httpFetchRequest.setConceptTags(new HashSet<String>());
            httpFetchRequest.setAceTags(new HashSet<String>());
            httpFetchRequest.setACE_VERSION(article.getVersion());

            logger.info("aceContent len:" + aceContent.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return httpFetchRequest;
    }


    public String removeImageTags(String aceContent) {

        if (aceContent != null) {
            Document document = Jsoup.parse(aceContent);
            document.select("div.mol-img").remove();
            document.select("img").remove();
            document.select("p.imageCaption").remove();
            aceContent = document.toString();
        }
        return aceContent;
    }

    public static void main(String[] args) {

    }
}
