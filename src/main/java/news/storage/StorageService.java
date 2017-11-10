package news.storage;

import news.models.Sources;
import news.storage.entities.Article;
import news.models.Feed;
import news.storage.entities.Source;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StorageService {

    boolean storeLocally(ResponseEntity<Feed> response);

    String store(Feed response);

    boolean isValidSource(String source);

    String getValidNewsArticlesApi();

    String getValidNewsSourcesApi();

    List<Source> getSources();

    String storeExternalSources(Sources response);

    boolean isValidTimestamp(String timestamp);

    List<Article> getArticles(String source, String from, String to);
}
