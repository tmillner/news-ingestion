package news.controllers;

import news.storage.StorageService;
import news.storage.entities.Article;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Controller
@RequestMapping("/api/search/repo")
public class RepoController {

    private final StorageService storageService;
    private final RestTemplate restTemplate;

    static Logger log = LoggerFactory.getLogger(RepoController.class.getName());

    @Autowired
    public RepoController(StorageService storageService, RestTemplateBuilder restTemplateBuilder) {
        this.storageService = storageService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @GetMapping("/{source}/{from}/{to:.+}")
    @ResponseBody
    public ResponseEntity getRepoSourceRecords(@PathVariable String source,
                                               @PathVariable String from,
                                               @PathVariable String to) {
        log.debug("GET -- Repo content");

        if (!storageService.isValidSource(source)) return ResponseEntity.notFound().build();
        if (!storageService.isValidTimestamp(from) || !storageService.isValidTimestamp(to)) {
            return ResponseEntity.badRequest().build();
        }
        // Need to make sure a user doesn't send a request > 31 days
        DateTime dtFrom = DateTime.parse(from);
        DateTime dtTo = DateTime.parse(to);

        Integer requestedDiff = Days.daysBetween(dtFrom, dtTo).getDays();
        if (requestedDiff > 31) {
            return ResponseEntity.badRequest().body("Sorry, range must be <= 31 days");
        }
        List<Article> articles = storageService.getArticles(source, from, to);
        log.info(articles.toString());

        return ResponseEntity.ok(articles);
    }
}
