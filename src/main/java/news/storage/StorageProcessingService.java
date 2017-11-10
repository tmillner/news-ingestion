package news.storage;

import news.models.Feed;
import news.models.Sources;
import news.storage.entities.Article;
import news.storage.entities.ArticleRepository;
import news.storage.entities.Source;
import news.storage.entities.SourceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class StorageProcessingService implements StorageService {

    private final ArticleRepository articleRepo;
    private final SourceRepository sourceRepo;
    private final StorageProperties storageProperties;

    static Logger log = LoggerFactory.getLogger(StorageProcessingService.class.getName());

    @Autowired
    public StorageProcessingService(ArticleRepository articleRepository,
                                    SourceRepository sourceRepository,
                                    StorageProperties storageProperties) {
        this.articleRepo = articleRepository;
        this.storageProperties = storageProperties;
        this.sourceRepo = sourceRepository;
    }

    @Override
    public boolean storeLocally(ResponseEntity<Feed> response) {
        return false;
    }

    @Override
    public String store(Feed responseFeed) {
        Integer recordsProcessed = 0;

        String feedSource = responseFeed.getSource();
        List<news.models.Article> articles = responseFeed.getArticles();

        for (news.models.Article article : articles) {
            List<?> existingArticles = articleRepo.findByTitle(article.getTitle());

            log.info(existingArticles.toString());
            if (existingArticles.size() == 0) {
                List<String> keywords = KeywordCreator.createKeywords(article, storageProperties.getKeywordBlacklist());
                log.info(keywords.toString());
                articleRepo.save(
                        new Article(feedSource, article.getAuthor(), article.getTitle(), article.getDescription(),
                                article.getUrl(), article.getUrlToImage(), article.getPublishedAt(), keywords)
                );
                recordsProcessed++;
            }
        }

        return String.format("Processed %s records ", recordsProcessed);
    }

    @Override
    public boolean isValidSource(String source) {

        return true;
        /*
        for (String s : storageProperties.getSources()) {
            log.debug("SOURCE: " + s);
            if (s.equals(source)) return true;
        }
        return false;
        */
    }

    public String storeExternalSources(Sources responseSources) {
        Integer recordsProcessed = 0;

        List<news.models.sources.Source> externalSources = responseSources.getSources();

        for (news.models.sources.Source source : externalSources) {
            List<?> existingSources = sourceRepo.findByName(source.getName());

            if (existingSources.size() == 0) {
                log.info("Storing: " + source.getName() + " since it was not found in repo");

                sourceRepo.save(
                        new Source(source.getId(), source.getName(), source.getDescription(), source.getUrl(),
                                source.getCategory(), source.getLanguage(), source.getCountry(),
                                source.getSortBysAvailable())
                );
                recordsProcessed++;
            }
        }

        return String.format("Processed %s records ", recordsProcessed);
    }

    @Override
    public String getValidNewsArticlesApi() {
        return storageProperties.getValidNewsArticlesApi();
    }

    @Override
    public String getValidNewsSourcesApi() {
        return storageProperties.getValidNewsSourcesApi();
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

    @Override
    public List<Article> getArticles(String source, String from, String to) {
        List<Article> articles = articleRepo.findBySourcePublishedAtBetween(source, from, to);
        return articles;
    }

    @Override
    public List<Source> getSources() {
        List<Source> sources = sourceRepo.findAll();
        return sources;
    }

    private static class KeywordCreator {
        private static final String regexPattern = "([-A-Z]\\w+\\s?)+";
        private static Pattern pattern = Pattern.compile(regexPattern);

        public static List<String> createKeywords(news.models.Article article, List<String> blacklist) {
            List<String> keywords = new ArrayList<>();

            Matcher m = pattern.matcher(article.getTitle());
            while (m.find()) {
                boolean addKeyword = true;
                String keyword = m.group().trim();

                for (String blacklistedKeyword : blacklist) {
                    if (keyword.equalsIgnoreCase(blacklistedKeyword.trim())) {
                        addKeyword = false;
                        break;
                    }
                }
                // Store all keywords in lowercase
                if (addKeyword == true) keywords.add(keyword.toLowerCase());
            }

            return keywords;
        }
    }
}
