package news.storage;

import news.storage.entities.Article;
import news.models.Feed;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface StorageService {

    boolean storeLocally(ResponseEntity<Feed> response);

    String store(Feed response);

    boolean isValidSource(String source);

    String[] getSources();

    String getValidNewsSourceApi();

    boolean isValidTimestamp(String timestamp);

    List<Article> getArticles(String source, String from, String to);
}
