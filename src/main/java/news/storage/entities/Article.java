package news.storage.entities;

import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Maybe want to make publishedAt a time object
public class Article {

    @Id
    public String id;

    public String source;
    public String author;
    public String title;
    public String description;
    public String url;
    public String urlToImage;
    public String publishedAt;
    public List<String> keywords;
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

    public String getAuthor() {
        return author;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getUrlToImage() {
        return urlToImage;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

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
