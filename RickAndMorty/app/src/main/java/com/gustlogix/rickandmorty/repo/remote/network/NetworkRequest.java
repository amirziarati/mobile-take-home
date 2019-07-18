package com.gustlogix.rickandmorty.repo.remote.network;

import java.util.ArrayList;
import java.util.List;

public class NetworkRequest {

    private String url = "";
    private List<QueryParameter> queryParameters = new ArrayList<>();
    private String method;
    private String body;

    private void setUrl(String url) {
        this.url = url.trim();
    }

    private void setQueryParameters(List<QueryParameter> queryParameters) {
        this.queryParameters = queryParameters;
    }

    private void setMethod(String method) {
        this.method = method;
    }

    String getMethod() {
        return method;
    }

    String getBody() {
        return body;
    }

    private void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (queryParameters.size() > 0) {
            sb.append(url);
            sb.append("?");
            for (QueryParameter param : queryParameters) {
                sb.append(param.toString());
                sb.append("&");
            }
        } else {
            sb.append(url);
        }
        return sb.toString();
    }

    public static class Builder {

        private NetworkRequest networkRequest;

        public Builder() {
            this.networkRequest = new NetworkRequest();
        }

        public Builder setUrl(String url) {
            networkRequest.setUrl(url);
            return this;
        }

        public Builder setQueryParameters(QueryParameter... parameters) {
            if (parameters != null) {
                List<QueryParameter> list = new ArrayList<>();
                for (QueryParameter parameter : parameters) {
                    if (parameter != null)
                        list.add(parameter);
                }
                networkRequest.setQueryParameters(list);
            }
            return this;
        }

        public Builder setMethod(String method) {
            networkRequest.setMethod(method);
            return this;
        }

        public Builder setBody(String body) {
            networkRequest.setBody(body);
            return this;
        }

        public NetworkRequest build() {
            return networkRequest;
        }
    }

    public static class QueryParameter {
        private String key;
        private String value;

        public QueryParameter(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String getKey() {
            return key;
        }

        void setKey(String key) {
            this.key = key;
        }

        String getValue() {
            return value;
        }

        void setValue(String value) {
            this.value = value;
        }

        @Override
        public String toString() {
            if (!getKey().equals("") && !getValue().equals(""))
                return getKey() + "=" + getValue();
            else
                return "";
        }
    }
}