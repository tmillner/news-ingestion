package news.storage.entities;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {

    List<Article> findByAuthor(String author);

    List<Article> findBySource(String source);

    List<Article> findByTitle(String title);

    // If fail remove quote on $gte/lte
    @Query("{'source': ?0, 'publishedAt': {'$gte' : ?1, '$lte' : ?2}}")
    List<Article> findBySourcePublishedAtBetween(String source, String dateStart, String dateEnd);
}
