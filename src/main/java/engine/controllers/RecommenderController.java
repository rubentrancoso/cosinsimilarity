package engine.controllers;

import java.util.HashMap;
import java.util.List;
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

import engine.entities.Similarity;
import engine.services.RecommenderService;

@RestController
@EnableAutoConfiguration
public class RecommenderController {

    @Autowired
    private RecommenderService recommenderService;

    private static final Logger logger = LoggerFactory.getLogger(RecommenderController.class);

    @RequestMapping(value = "/recommend/{sku}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	Map<String, Object> recommend(@PathVariable("sku") long sku) {
		logger.info("processing request");

        Map<String, Object> response = new HashMap<String, Object>();
        try {
        	List<Similarity> articles = recommenderService.recommend(sku);
        	response.put("result", articles);
            response.put("status", "success");
        } catch (Exception e) {
            logger.error("Error occurred while trying to process api request", e);
            response.put("status", "fail");
        }

        return response;
    }

//    @RequestMapping(value = "/query/{sku}", produces = MediaType.APPLICATION_JSON_VALUE)
//	@ResponseBody
//	Map<String, Object> query(@PathVariable("sku") long sku) {
//		logger.info("processing request");
//
//        Map<String, Object> response = new HashMap<String, Object>();
//        try {
////        	List<Article> articles = recommenderService.recommend(sku);
////        	response.put("result", articles);
//            response.put("status", "success");
//        } catch (Exception e) {
//            logger.error("Error occurred while trying to process api request", e);
//            response.put("status", "fail");
//        }
//
//        return response;
//    }
}