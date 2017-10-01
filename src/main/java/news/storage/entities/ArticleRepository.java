package news.storage.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ArticleRepository extends MongoRepository<Article, String> {

    List<Article> findByAuthor(String author);

    List<Article> findBySource(String source);

    List<Article> findByTitle(String title);

    // Want to find articles in range (Check usage)
    List<Article> findByPublishedAt(String dateStart, String dateEnd);

}
