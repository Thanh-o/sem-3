package org.example.realestate;

import org.example.realestate.RealEstateCrawlerService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CrawlerController {

    private final RealEstateCrawlerService crawlerService;

    public CrawlerController(RealEstateCrawlerService crawlerService) {
        this.crawlerService = crawlerService;
    }

    @GetMapping("/crawl")
    public String crawl(@RequestParam String url) {
        crawlerService.crawlData(url);
        return "Crawling completed!";
    }
}