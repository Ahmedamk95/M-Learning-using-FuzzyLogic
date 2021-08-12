package com.saboo.mlearning;

/**
 * Created by Sohail on 16-04-2017.
 */

public class QueryItems {
    private String query = "";
    private String response = "";
    private String queryStatus = "";
    private boolean isReplied;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getQueryStatus() {
        return queryStatus;
    }

    public void setQueryStatus(String queryStatus) {
        this.queryStatus = queryStatus;
    }

    public boolean isReplied() {
        return isReplied;
    }

    public void setReplied(boolean replied) {
        isReplied = replied;
    }
}
