package news.storage.entities;

import com.fasterxml.jackson.annotation.*;
import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@JsonPropertyOrder({
        "id",
        "name",
        "description",
        "url",
        "category",
        "language",
        "country",
        "urlsToLogos",
        "sortBysAvailable"
})
public class Source {

    @Id
    @JsonProperty("id")
    private String id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("description")
    private String description;
    @JsonProperty("url")
    private String url;
    @JsonProperty("category")
    private String category;
    @JsonProperty("language")
    private String language;
    @JsonProperty("country")
    private String country;
    @JsonProperty("sortBysAvailable")
    private List<String> sortBysAvailable = null;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();


    public Source(String id, String name, String description, String url,
                  String category, String language, String country, List<String> sortBysAvailable) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.url = url;
        this.category = category;
        this.language = language;
        this.country = country;
        this.sortBysAvailable = sortBysAvailable;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("url")
    public String getUrl() {
        return url;
    }

    @JsonProperty("category")
    public String getCategory() {
        return category;
    }

    @JsonProperty("language")
    public String getLanguage() {
        return language;
    }

    @JsonProperty("country")
    public String getCountry() {
        return country;
    }

    @JsonProperty("sortBysAvailable")
    public List<String> getSortBysAvailable() {
        return sortBysAvailable;
    }

    @Override
    public String toString() {
        return String.format(
                "Source[ id=%s, name=%s, description=%s, url=%s, category=%s, language=%s, country=%s, sortByAvailable=%s ]",
                id, name, description, url, category, language, country, sortBysAvailable
        );
    }
}
