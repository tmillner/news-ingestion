package news.storage;

import news.models.Feed;
import org.springframework.http.ResponseEntity;

public interface StorageService {

    boolean storeLocally(ResponseEntity<Feed> response);

    boolean store(ResponseEntity<Feed> response);
}
