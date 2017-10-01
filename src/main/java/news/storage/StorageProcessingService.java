package news.storage;

import news.models.Feed;
import news.storage.entities.Article;
import news.storage.entities.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StorageProcessingService implements StorageService {

    private final ArticleRepository repo;

    @Autowired
    public StorageProcessingService(ArticleRepository articleRepository) {
        this.repo = articleRepository;
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
            // check data already found if not, save
            repo.save(
                    new Article(feedSource, article.getAuthor(), article.getTitle(), article.getDescription(),
                            article.getUrl(), article.getUrlToImage(), article.getPublishedAt())
            );
        }

        return true;
    }
}
