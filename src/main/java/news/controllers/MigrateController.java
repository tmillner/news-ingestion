package news.controllers;

import news.models.Feed;
import news.models.sources.Source;
import news.storage.StorageService;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/api/external")
public class MigrateController {

    private final StorageService storageService;
    private final RestTemplate restTemplate;

    static Logger log = LoggerFactory.getLogger(MigrateController.class.getName());

    @Autowired
    public MigrateController(StorageService storageService, RestTemplateBuilder restTemplateBuilder) {
        this.storageService = storageService;
        this.restTemplate = restTemplateBuilder.build();
    }

    @PostMapping("/persist/records")
    @ResponseBody
    public ResponseEntity persistRecordsLocallyFromExternal() {
        log.debug("POST -- Persist of External content");

        if (!isNowWithinRunTime()) return ResponseEntity.accepted().body("Unable to persist records at this time");

        for (news.storage.entities.Source source : storageService.getSources()) {
            ResponseEntity<Feed> response = getExternalSourceRecords(source.getId());
            if (response.getStatusCode() != HttpStatus.OK) {
                log.warn("Unable to process source results from " + source.getId());
            } else {
                storageService.store(response.getBody());
            }
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/persist/record")
    @ResponseBody
    public ResponseEntity persistRecordLocallyFromExternal(@RequestBody Feed feed) {
        log.debug("POST -- Persist of Source-specific External content");

        String msg = storageService.store(feed);

        return ResponseEntity.ok(msg);
    }

    @GetMapping("/source/{source:.+}/records")
    @ResponseBody
    public ResponseEntity<Feed> getExternalSourceRecords(@PathVariable String source) {
        log.debug("GET -- External content");

        Feed feed;

        try {
            feed = restTemplate.getForObject(storageService.getValidNewsArticlesApi() +
                    "&source=" + source, Feed.class);
            log.info("Response is " + feed);
        } catch (Exception e) {
            throw e;
        }

        return ResponseEntity.ok(feed);
    }

    /**
     * TODO Refactor RunTime values into config
     *
     * @return
     */
    private boolean isNowWithinRunTime() {
        int hourStart = 7;
        int hourEnd = 9;

        DateTime now = DateTime.now(DateTimeZone.UTC);

        if (now.getHourOfDay() > hourStart && now.getHourOfDay() < hourEnd) {
            return true;
        }

        return false;
    }
}
