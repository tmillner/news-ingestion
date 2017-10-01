package news.controllers;

import news.models.Feed;
import news.storage.StorageProperties;
import news.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

@Controller
public class MigrateController {

    private final StorageService storageService;
    private final StorageProperties storageProperties;
    private final RestTemplate restTemplate;

    static Logger log = LoggerFactory.getLogger(MigrateController.class.getName());

    @Autowired
    public MigrateController(StorageService storageService, StorageProperties storageProperties,
                             RestTemplateBuilder restTemplateBuilder) {
        this.storageService = storageService;
        this.storageProperties = storageProperties;
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("/api/persist/records")
    @ResponseBody
    public ResponseEntity persistRecordsLocallyFromExternal() {
        log.debug("POST -- Persist of External content");

        for (String source : storageProperties.getSources()) {
            ResponseEntity<Feed> response = getExternalSourceRecords(source);
            storageService.store(response);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/api/external/source/{source:.+}/records")
    @ResponseBody
    private ResponseEntity<Feed> getExternalSourceRecords(@PathVariable String source) {
        log.debug("GET -- External content");

        Feed feed;

        try {
            feed = restTemplate.getForObject(storageProperties.getValidNewsSourceApi() +
                    "&source=" + source, Feed.class);
            log.info("Response is " + feed);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.ok(feed);
    }
}
