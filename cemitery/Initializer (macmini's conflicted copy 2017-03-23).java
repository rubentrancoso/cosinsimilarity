package engine.util;

import java.io.File;
import java.io.FileReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import engine.entities.Article;
import engine.entities.Similarity;
import engine.repositories.ArticleRepository;
import engine.repositories.SimilarityRepository;

@Component
public class Initializer {
	
	private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	SimilarityRepository similarityRepository;
	
	private JSONParser parser = new JSONParser();
	
	public void init() {
		logger.info("Initializing...");
		populate();
		calculate();
		logger.info("Initialization end.");
	}
	
	@Transactional
	private void calculate() {
		LinkedList<Similarity> sim = new LinkedList<Similarity>();
		logger.info("Performing calculations...");
		long total = articleRepository.count();
		long totalcount = (total * (total - 1))/2;
		long count = totalcount;
		Iterable<Article> list = articleRepository.findAll();
		for (Iterator<Article> pivots = list.iterator(); pivots.hasNext(); ) {
			Article pivot = pivots.next();
			for(Iterator<Article> items = list.iterator(); items.hasNext(); ) {
				Article item = items.next();
				if(!pivot.equals(item)) {
					count--;
					double result = Util.cosineSimilarity(pivot.getVector(), item.getVector());
					Similarity similarity = new Similarity();
					similarity.setName(pivot.getName());
					similarity.setTarget(item.getName());
					similarity.setSimilarity(result);
					sim.add(similarity);
					if(count%1000000==0) {
						logger.info("saving...");
						similarityRepository.save(sim);
						sim.clear();
					}
				}
			}
			pivots.remove();
			logger.info("partial count: " + count);
		}
		logger.info(totalcount + " comparisions performed.");
	}
	
	@Transactional
	private void populate() {
		logger.info("Populating database...");
		try {
			File file = new ClassPathResource("shortdata.json").getFile();
			Object obj = parser.parse(new FileReader(file));
			JSONObject jsonObject = (JSONObject) obj;

			@SuppressWarnings("unchecked")
			Set<String> keys = jsonObject.keySet();
			
			logger.info("Adding data...");
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String nameKey = iterator.next();
				Long name = Long.parseLong(nameKey.split("-")[1]);
				Article article = new Article();
				article.setName(name);
				JSONObject attrList = (JSONObject) jsonObject.get(nameKey);
				for(char c='a';c<='j';c++) {
					String attr = (String) attrList.get("att-" + c);
					Integer attrValue = Integer.parseInt(attr.split("-")[2]);
					article.setAttr(c-'a', attrValue);
				}
				articleRepository.save(article);
			}
			Article article = articleRepository.findByName(10L);
			logger.info("Testing... Found article " + article.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Flushing data...");		
	}

}
