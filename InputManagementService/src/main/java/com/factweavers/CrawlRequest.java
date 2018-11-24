package com.factweavers;

import org.springframework.stereotype.Component;

/**
 * Created by weavers on 05/07/18.
 */
@Component
public class CrawlRequest {

    String _id;
    String link;
    String type;
    String fetchedTill;
    boolean isFetched;
    String status;

    boolean esEnabled;
    boolean isRefetch;

    @Override
    public String toString() {
        return "CrawlRequest{" +
                "_id='" + _id + '\'' +
                ", link='" + link + '\'' +
                ", type='" + type + '\'' +
                ", fetchedTill='" + fetchedTill + '\'' +
                ", isFetched=" + isFetched +
                ", status='" + status + '\'' +
                ", esEnabled=" + esEnabled +
                '}';
    }

    public boolean isRefetch() {
        return isRefetch;
    }

    public void setRefetch(boolean refetch) {
        isRefetch = refetch;
    }

    public boolean isEsEnabled() {
        return esEnabled;
    }

    public void setEsEnabled(boolean esEnabled) {
        this.esEnabled = esEnabled;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFetchedTill() {
        return fetchedTill;
    }

    public void setFetchedTill(String fetchedTill) {
        this.fetchedTill = fetchedTill;
    }

    public boolean isFetched() {
        return isFetched;
    }

    public void setFetched(boolean fetched) {
        isFetched = fetched;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
