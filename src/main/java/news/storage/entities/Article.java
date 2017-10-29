package news.storage.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
        "author",
        "title",
        "description",
        "url",
        "urlToImage",
        "publishedAt"
})
public class Article {

    @Id
    public String id;

    public String source;
    @JsonProperty("author")
    public String author;
    @JsonProperty("title")
    public String title;
    @JsonProperty("description")
    public String description;
    @JsonProperty("url")
    public String url;
    @JsonProperty("urlToImage")
    public String urlToImage;
    @JsonProperty("publishedAt")
    public String publishedAt;
    @JsonProperty("keywords")
    public List<String> keywords;
    @JsonIgnore
    public Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Article(String source, String author, String title, String description,
                   String url, String urlToImage, String publishedAt, List<String> keywords) {
        this.source = source;
        this.author = author;
        this.title = title;
        this.description = description;
        this.url = url;
        this.urlToImage = urlToImage;
        this.publishedAt = publishedAt;
        this.keywords = keywords;
    }

    public String getId() {
        return id;
    }

    @JsonProperty("author")
    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("urlToImage")
    public String getUrlToImage() {
        return urlToImage;
    }

    @JsonProperty("publishedAt")
    public String getPublishedAt() {
        return publishedAt;
    }

    @JsonProperty("keywords")
    public List<String> getKeywords() {
        return keywords;
    }

    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }

    @Override
    public String toString() {
        return String.format(
                "Article[ id=%s, source=%s, author=%s, title=%s, description=%s, url=%s, publishedAt=%s, keywords=%s ]",
                id, source, author, title, description, url, publishedAt, keywords
        );
    }
}
