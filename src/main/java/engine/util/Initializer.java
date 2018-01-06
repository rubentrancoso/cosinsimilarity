package engine.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import engine.entities.Article;
import engine.entities.Similarity;
import engine.facts.Weight;
import engine.repositories.ArticleRepository;
import engine.repositories.SimilarityRepository;

@Component
public class Initializer {

	private static final Logger logger = LoggerFactory.getLogger(Initializer.class);

	@Value("${initializer.file.output}")
	private String outputFile;

	@Value("${initializer.file.data}")
	private String dataFile;

	@Value("${hibernate.jdbc.batch_size}")
	private int batchSize;

	@Value("${initializer.lambda}")
	private boolean useLambda;

	@Value("${dotproduct.weighted}")
	private boolean weighted;

	@Value("${initializer.skip}")
	private boolean initializerSkip;
	
	@Value("${dotproduct.weigths}")
	private String dotproductWeigths;

	@Autowired
	SimilarityRepository similarityRepository;

	@Autowired
	ArticleRepository articleRepository;

	private JSONParser parser = new JSONParser();

	public void init() {
		if (initializerSkip) {
			logger.info("Skip Initialization...");
			return;
		}
		private_init();
	}
	
	public void private_init() {
		logger.info("Initializing...");
		delteOutput();
		populate();
		long total = calculate();
		if (useLambda) {
			logger.info("lambda enabled.");
			load_lambda();
		} else {
			logger.info("lambda disabled.");
			load(total);
		}
		logger.info("Initialization end.");
	}

	private void delteOutput() {
		File file = new File(outputFile);

		if (file.delete()) {
			logger.info(file.getName() + " was deleted. New one will be generated.");
		} else {
			logger.info(file.getName() + " not present.");
		}
	}

	@Transactional
	private void populate() {
		LinkedList<Article> list = new LinkedList<Article>();
		logger.info("Populating database...");
		try {
			logger.info("Picking data file : " + dataFile);
			InputStream in = getClass().getResourceAsStream("/"+ dataFile); 
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));

			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;

			@SuppressWarnings("unchecked")
			Set<String> keys = jsonObject.keySet();
			int batch_size = 100;
			int count = keys.size();

			logger.info("Adding data...");
			for (Iterator<String> iterator = keys.iterator(); iterator.hasNext();) {
				String nameKey = iterator.next();
				long sku = Long.parseLong(nameKey.split("-")[1]);
				Article article = new Article();
				list.add(article);
				count--;
				article.setSku(sku);
				JSONObject attrList = (JSONObject) jsonObject.get(nameKey);
				for (char c = 'a'; c <= 'j'; c++) {
					String attr = (String) attrList.get("att-" + c);
					Integer attrValue = Integer.parseInt(attr.split("-")[2]);
					article.setAttr(c - 'a', attrValue);
				}
				if (count % batch_size == 0) {
					articleRepository.bulkSave(list);
					list.clear();
				}
			}
			Article article = articleRepository.findBySku(10L);
			logger.info("Testing... Found article " + article.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("Flushing data...");
	}

	@Transactional
	private long calculate() {
		logger.info("calculate");
		File f = new File(outputFile);
		if (f.exists() && !f.isDirectory()) {
			try {
				logger.info("count lines");
				return Util.countLines(outputFile);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		FastWriter fastWriter = null;
		try {
			fastWriter = new FastWriter(outputFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		logger.info("Performing calculations...");
		logger.info("Weighted = " + weighted);
		if(weighted) {
			Weight.config(dotproductWeigths);
		}
		long total = articleRepository.count();
		long totalcount = (total * (total - 1)) / 2;
		long count = 0;

		Iterable<Article> list = articleRepository.findAll();
		StringBuilder sb = new StringBuilder();
		for (Iterator<Article> pivots = list.iterator(); pivots.hasNext();) {
			Article pivot = pivots.next();
			for (Iterator<Article> items = list.iterator(); items.hasNext();) {
				Article item = items.next();
				if (!pivot.equals(item)) {
					double result;
					if (weighted) {
						result = Util.weightedCosineSimilarity(pivot.getVector(), item.getVector());
					} else {
						result = Util.cosineSimilarity(pivot.getVector(), item.getVector());
					}
					sb.append(pivot.getSku()).append(",").append(item.getSku()).append(",").append(result).append("\n");
					try {
						count++;
						fastWriter.writeRecord(sb.toString());
						sb.setLength(0);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			logger.info(count + " records writen to file.");
			pivots.remove();
		}
		try {
			fastWriter.finish();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(totalcount + " comparisions expected.");
		logger.info(count + " records generated.");
		return count;
	}

	@Transactional
	private void load(long records) {
		long total = records;
		long loaded = 0;
		long start = Instant.now().toEpochMilli();
		logger.info("Load started...");
		LinkedList<Similarity> list = new LinkedList<Similarity>();
		try {
			String sCurrentLine;
			BufferedReader buffer = new BufferedReader(new FileReader(outputFile));
			while ((sCurrentLine = buffer.readLine()) != null) {
				String[] computation = sCurrentLine.split(",");
				list.add(new Similarity(Long.parseLong(computation[0]), Long.parseLong(computation[1]),	Double.parseDouble(computation[2])));
				total--;
				loaded++;
				if (total % batchSize == 0) {
					similarityRepository.bulkSave(list);
					list.clear();
					logger.info(loaded + " loaded.");

				}
			}
			buffer.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = Instant.now().toEpochMilli();
		logger.info(String.format("Load completed in %d milliseconds", (end - start)));
	}

	private void load_lambda() {
		long start = Instant.now().toEpochMilli();
		logger.info("Load started...");
		try {
			BufferedReader buffer = new BufferedReader(new FileReader(outputFile));
			buffer.lines().parallel() // Start streaming the
										// lines
					.parallel() // Convert to parallel stream
					.forEach(line -> { // Use an AtomicAdder to tally word
										// counts
						String[] computation = line.split(",");
						similarityRepository.save(new Similarity(Long.parseLong(computation[0]),
								Long.parseLong(computation[1]), Double.parseDouble(computation[2])));
					});
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		long end = Instant.now().toEpochMilli();
		logger.info(String.format("Load completed in %d milliseconds", (end - start)));
	}

	public void load() {
		long total = calculate();
		if (useLambda) {
			logger.info("lambda enabled.");
			load_lambda();
		} else {
			logger.info("lambda disabled.");
			load(total);
		}
		logger.info("Initialization end.");
	}

}

// 1 x x
// 2 x
// 3
// 3 2 1