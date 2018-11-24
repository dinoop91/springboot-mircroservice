package com.factweavers.crawlerService.service;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.json.JSONObject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by weavers on 05/07/18.
 */
@Component
public class HttpFetchRequest {

    String Link;
    String ParentUrl;
    String Title;
    String Author;
    String actualTimeStamp;
    String Tags;
    String SourceName;
    String partnerName;
    String RedirectedLink;
    String fetchTimeStamp;
    String htmlContent;
    boolean error;

    // ACE
    String Content;
    Set<String> AceTags;
    Set<String> ConceptTags;
    String ExtractionAlg;
    String ACE_VERSION;

    // EMAC
    boolean emacEnabled;
    JSONObject Categories;
    boolean EMACStatus;

    @Override
    public String toString() {
        return "HttpFetchRequest{" +
                "Link='" + Link + '\'' +
                ", ParentUrl='" + ParentUrl + '\'' +
                ", Title='" + Title + '\'' +
                ", Author='" + Author + '\'' +
                ", actualTimeStamp='" + actualTimeStamp + '\'' +
                ", Tags='" + Tags + '\'' +
                ", SourceName='" + SourceName + '\'' +
                ", partnerName='" + partnerName + '\'' +
                ", RedirectedLink='" + RedirectedLink + '\'' +
                ", fetchTimeStamp='" + fetchTimeStamp + '\'' +
                ", htmlContent='" + htmlContent + '\'' +
                ", error=" + error +
                ", Content='" + Content + '\'' +
                ", AceTags=" + AceTags +
                ", ConceptTags=" + ConceptTags +
                ", ExtractionAlg='" + ExtractionAlg + '\'' +
                ", ACE_VERSION='" + ACE_VERSION + '\'' +
                ", emacEnabled=" + emacEnabled +
                ", Categories=" + Categories +
                ", EMACStatus=" + EMACStatus +
                ", EMAC_VERSION='" + EMAC_VERSION + '\'' +
                ", wordCount=" + wordCount +
                ", sources=" + sources +
                '}';
    }

    String EMAC_VERSION;
    int wordCount;

    public List<Map<String, String>> getSources() {
        return sources;
    }

    public void setSources(List<Map<String, String>> sources) {
        this.sources = sources;
    }

    List<Map<String, String>>  sources;

    public boolean isEmacEnabled() {
        return emacEnabled;
    }

    public void setEmacEnabled(boolean emacEnabled) {
        this.emacEnabled = emacEnabled;
    }

    public JSONObject getCategories() {
        return Categories;
    }

    public void setCategories(JSONObject categories) {
        Categories = categories;
    }

    public Boolean getEMACStatus() {
        return EMACStatus;
    }

    public void setEMACStatus(Boolean EMACStatus) {
        this.EMACStatus = EMACStatus;
    }

    public String getEMAC_VERSION() {
        return EMAC_VERSION;
    }

    public void setEMAC_VERSION(String EMAC_VERSION) {
        this.EMAC_VERSION = EMAC_VERSION;
    }

    public int getWordCount() {
        return wordCount;
    }

    public void setWordCount(int wordCount) {
        this.wordCount = wordCount;
    }



    public String getContent() {
        return Content;
    }

    public void setContent(String content) {
        Content = content;
    }

    public Set<String> getAceTags() {
        return AceTags;
    }

    public void setAceTags(Set<String> aceTags) {
        AceTags = aceTags;
    }

    public Set<String> getConceptTags() {
        return ConceptTags;
    }

    public void setConceptTags(Set<String> conceptTags) {
        ConceptTags = conceptTags;
    }

    public String getExtractionAlg() {
        return ExtractionAlg;
    }

    public void setExtractionAlg(String extractionAlg) {
        ExtractionAlg = extractionAlg;
    }

    public String getACE_VERSION() {
        return ACE_VERSION;
    }

    public void setACE_VERSION(String ACE_VERSION) {
        this.ACE_VERSION = ACE_VERSION;
    }

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getLink() {
        return Link;
    }

    public String getSourceName() {
        return SourceName;
    }

    public void setSourceName(String sourceName) {
        SourceName = sourceName;
    }

    public String getPartnerName() {
        return partnerName;
    }

    public void setPartnerName(String partnerName) {
        this.partnerName = partnerName;
    }

    public String getRedirectedLink() {
        return RedirectedLink;
    }

    public void setRedirectedLink(String redirectedLink) {
        RedirectedLink = redirectedLink;
    }

    public String getFetchTimeStamp() {
        return fetchTimeStamp;
    }

    public void setFetchTimeStamp(String fetchTimeStamp) {
        this.fetchTimeStamp = fetchTimeStamp;
    }

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    public void setLink(String link) {
        Link = link;
    }

    public String getParentUrl() {
        return ParentUrl;
    }

    public void setParentUrl(String parentUrl) {
        ParentUrl = parentUrl;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public String getActualTimeStamp() {
        return actualTimeStamp;
    }

    public void setActualTimeStamp(String actualTimeStamp) {
        this.actualTimeStamp = actualTimeStamp;
    }

    public String getTags() {
        return Tags;
    }

    public void setTags(String tags) {
        Tags = tags;
    }


}
