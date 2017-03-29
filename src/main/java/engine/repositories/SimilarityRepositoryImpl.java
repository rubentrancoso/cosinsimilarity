package engine.repositories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import engine.entities.Similarity;

@Transactional 
public class SimilarityRepositoryImpl implements SimilarityRepositoryExt {

	@PersistenceContext
	private EntityManager entityManager;
	
	@Value("${hibernate.jdbc.batch_size}")
	private int batchSize;	

	@Autowired
	ArticleRepository articleRepository;
	
	@Autowired
	SimilarityRepository similarityRepository;

	@Override
	public List<Similarity> findSimilar(long sku, int limit) {
		List<Similarity> list;
		list = similarityRepository.getSimilar(sku, limit);
		return list;
	}
	
	@Override
	public <T extends Similarity> Collection<T> bulkSave(Collection<T> entities) {
		final List<T> savedEntities = new ArrayList<T>(entities.size());
		int i = 0;
		for (T t : entities) {
			savedEntities.add(persistOrMerge(t));
			i++;
			if (i % batchSize == 0) {
				// Flush a batch of inserts and release memory.
				entityManager.flush();
				entityManager.clear();
			}
		}
		return savedEntities;
	}

	private <T extends Similarity> T persistOrMerge(T t) {
		if (t.getId() == null) {
			entityManager.persist(t);
			return t;
		} else {
			return entityManager.merge(t);
		}
	}

}
