package engine.repositories;

import static com.googlecode.cqengine.codegen.AttributeBytecodeGenerator.createAttributes;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.googlecode.cqengine.ConcurrentIndexedCollection;
import com.googlecode.cqengine.IndexedCollection;
import com.googlecode.cqengine.index.navigable.NavigableIndex;
import com.googlecode.cqengine.query.parser.sql.SQLParser;
import com.googlecode.cqengine.resultset.ResultSet;

import engine.entities.Article;
import engine.entities.Similarity;

@Component
public class SimilarityRepository {
	
	private static final Logger logger = LoggerFactory.getLogger(SimilarityRepository.class);

	IndexedCollection<Similarity> db;
	SQLParser<Similarity> parser = SQLParser.forPojoWithAttributes(Similarity.class, createAttributes(Similarity.class));
	
	public SimilarityRepository() {
		db = new ConcurrentIndexedCollection<Similarity>();
		db.addIndex(NavigableIndex.onAttribute(Similarity.ID));
		db.addIndex(NavigableIndex.onAttribute(Similarity.SIMILAR_ID));
	}
	
	public void put(long sku, long similar_sku, double similarity) {
		Similarity sim = new Similarity(sku, similar_sku, similarity);
		//logger.info(sim.toString());
		db.add(sim);
	}

	public ArrayList<Article> getSimilar(long sku, int limit) {
	    String query = "SELECT * FROM similarity WHERE ( sku=" + sku + " OR similar_sku=" + sku + ") ORDER by similarity DESC";
	    logger.info(query);
	    ResultSet<Similarity> results = parser.retrieve(db, query );
	    logger.info(results.size() + " record(s) found.");
	    int count = 0;
	    for (Similarity similarity : results) {
	    	System.out.println(similarity); 
			count ++;
			if(count == limit)
				break;
		}
		return null;
	}
}
