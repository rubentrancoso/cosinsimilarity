package engine.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import engine.entities.Similarity;
import engine.services.RecommenderService;
import message.Message;

@RestController
@EnableAutoConfiguration
public class RecommenderController {

    @Autowired
    private RecommenderService recommenderService;

    private static final Logger logger = LoggerFactory.getLogger(RecommenderController.class);

    @RequestMapping(value = "/recommend/{sku}", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	ResponseEntity<Object> recommend(@PathVariable("sku") long sku) {
		logger.info("processing request");
		HttpStatus responseCode = HttpStatus.OK;
		Object message = new Message("sucess");
        try {
        	message = recommenderService.recommend(sku);
        } catch (Exception e) {
            logger.error("Error occurred while trying to process api request", e);
            message = new Message("Error occurred while trying to process api request.");
    		responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Object>(message, responseCode);
    }

}