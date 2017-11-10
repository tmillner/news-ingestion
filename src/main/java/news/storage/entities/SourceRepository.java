package news.storage.entities;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SourceRepository extends MongoRepository<Source, String> {

    List<Source> findByName(String name);

}
