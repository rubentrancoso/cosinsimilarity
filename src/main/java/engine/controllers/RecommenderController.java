package engine.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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

    @RequestMapping(value = "/load", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	ResponseEntity<Object> load() {
		logger.info("processing request");
		HttpStatus responseCode = HttpStatus.OK;
		Object message = new Message("sucess");
        try {
        	recommenderService.load();
        } catch (Exception e) {
            logger.error("Error occurred while trying to process api request", e);
            message = new Message("Error occurred while trying to process api request.");
    		responseCode = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return new ResponseEntity<Object>(message, responseCode);
    }


}