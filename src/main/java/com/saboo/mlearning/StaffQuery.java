package com.saboo.mlearning;

/**
 * Created by Sohail on 29-04-2017.
 */

public class StaffQuery {
    private String queryBy;
    private String query;
    private String queryStatus;
    private String queryID;
    private String queryResponse;
    private boolean isResponded;

    public String getQueryBy() {
        return queryBy;
    }

    public void setQueryBy(String queryBy) {
        this.queryBy = queryBy;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public String getQueryID() {
        return queryID;
    }

    public void setQueryID(String queryID) {
        this.queryID = queryID;
    }

    public String getQueryResponse() {
        return queryResponse;
    }

    public void setQueryResponse(String queryResponse) {
        this.queryResponse = queryResponse;
    }

    public boolean isResponded() {
        return isResponded;
    }

    public void setResponded(boolean responded) {
        isResponded = responded;
    }
}
