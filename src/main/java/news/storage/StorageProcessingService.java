package news.storage;

import news.models.Feed;
import news.storage.entities.Article;
import news.storage.entities.ArticleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Service
public class StorageProcessingService implements StorageService {

    private final ArticleRepository repo;
    private final StorageProperties storageProperties;

    static Logger log = LoggerFactory.getLogger(StorageProcessingService.class.getName());

    @Autowired
    public StorageProcessingService(ArticleRepository articleRepository, StorageProperties storageProperties) {
        this.repo = articleRepository;
        this.storageProperties = storageProperties;
    }

    @Override
    public boolean storeLocally(ResponseEntity<Feed> response) {
        return false;
    }

    @Override
    public boolean store(ResponseEntity<Feed> response) {

        Feed feed = response.getBody();
        String feedSource = feed.getSource();
        List<news.models.Article> articles = feed.getArticles();

        for (news.models.Article article : articles) {
            List<?> existingArticles = repo.findByTitle(article.getTitle());
            // Ideally, would process the record and store keywords from source
            if (existingArticles.size() == 0) {
                repo.save(
                        new Article(feedSource, article.getAuthor(), article.getTitle(), article.getDescription(),
                                article.getUrl(), article.getUrlToImage(), article.getPublishedAt())
                );
            }
        }

        return true;
    }

    @Override
    public boolean isValidSource(String source) {

        for (String s : storageProperties.getSources()) {
            log.debug("SOURCE: " + s);
            if (s.equals(source)) return true;
        }
        return false;
    }

    @Override
    public String[] getSources() {
        return storageProperties.getSources();
    }

    @Override
    public String getValidNewsSourceApi() {
        return storageProperties.getValidNewsSourceApi();
    }

    @Override
    public boolean isValidTimestamp(String timestamp) {
        try {
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date dateTime = dateFormat.parse(timestamp);
            return true;
        } catch (ParseException e) {
            log.info("Invalid timestamp found", e);
        }
        return false;
    }

    // Maybe this is slow ???
    @Override
    public List<news.models.Article> getArticles(String source, String from, String to) {
        List<Article> articles = repo.findBySourcePublishedAtBetween(source, from, to);
        return articles.stream().map(news.models.Article::new).collect(Collectors.toList());
    }
}
