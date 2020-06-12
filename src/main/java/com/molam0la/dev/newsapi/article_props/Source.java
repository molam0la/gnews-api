package com.molam0la.dev.newsapi.article_props;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Source {

    private @JsonProperty String name;
    private @JsonProperty String url;

    public Source(String name, String url) {
        this.name = name;
        this.url = url;
    }

    public Source() {
        super();
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
