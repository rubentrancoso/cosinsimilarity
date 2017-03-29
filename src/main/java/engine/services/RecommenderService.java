package engine.services;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import engine.entities.Similarity;
import engine.repositories.SimilarityRepository;

@Service
@Transactional
public class RecommenderService {
	
	@Autowired
	private SimilarityRepository similarityRepository;

	public List<Similarity> recommend(long sku) {
		List<Similarity> list = similarityRepository.findSimilar(sku, 10);
		return list;
	}

}