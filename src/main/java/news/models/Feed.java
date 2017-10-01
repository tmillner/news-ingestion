package news.models;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "status",
        "source",
        "sortBy",
        "articles"
})
public class Feed {

    @JsonProperty("status")
    private String status;
    @JsonProperty("source")
    private String source;
    @JsonProperty("sortBy")
    private String sortBy;
    @JsonProperty("articles")
    private List<Article> articles = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    public Feed withStatus(String status) {
        this.status = status;
        return this;
    }

    @JsonProperty("source")
    public String getSource() {
        return source;
    }

    @JsonProperty("source")
    public void setSource(String source) {
        this.source = source;
    }

    public Feed withSource(String source) {
        this.source = source;
        return this;
    }

    @JsonProperty("sortBy")
    public String getSortBy() {
        return sortBy;
    }

    @JsonProperty("sortBy")
    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public Feed withSortBy(String sortBy) {
        this.sortBy = sortBy;
        return this;
    }

    @JsonProperty("articles")
    public List<Article> getArticles() {
        return articles;
    }

    @JsonProperty("articles")
    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public Feed withArticles(List<Article> articles) {
        this.articles = articles;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Feed withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

    @Override
    public String toString() {
        return String.format(
                "Feed[ status=%s, source=%s, sortBy=%s, articles=%s ]",
                status, source, sortBy, articles
        );
    }
}