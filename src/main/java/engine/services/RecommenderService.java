package engine.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import engine.entities.Similarity;
import engine.repositories.SimilarityRepository;
import engine.util.Initializer;

@Service
@Transactional
public class RecommenderService {
	
	@Autowired
	private SimilarityRepository similarityRepository;
	
	@Autowired
	Initializer initializer;

	public List<Similarity> recommend(long sku) {
		List<Similarity> list = similarityRepository.findSimilar(sku, 10);
		return list;
	}

	public void load() {
		initializer.load();
	}

}