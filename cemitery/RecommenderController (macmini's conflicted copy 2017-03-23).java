package engine.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import engine.entities.Article;
import engine.services.RecommenderService;

@RestController
@EnableAutoConfiguration
public class RecommenderController {

    @Autowired
    private RecommenderService recommenderService;

    private static final Logger logger = LoggerFactory.getLogger(RecommenderController.class);

    @RequestMapping(value = "/recommend/{articleName}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Map<String, String> execute(@PathVariable("articleName") Long articleName) {
		logger.info("processing request");

        Map<String, String> response = new HashMap<String, String>();
        try {
        	Article article = recommenderService.recommend(articleName);
        	response.put("name", Long.toString(article.getName()));
            response.put("status", "success");
        } catch (Exception e) {
            logger.error("Error occurred while trying to process api request", e);
            response.put("status", "fail");
        }

        return response;
    }
}