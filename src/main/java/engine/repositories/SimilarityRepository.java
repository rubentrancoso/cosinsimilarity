package engine.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import engine.entities.Similarity;

@Repository
public interface SimilarityRepository extends CrudRepository<Similarity, Long>, SimilarityRepositoryExt {
	
	public Similarity findBySku(long sku);

	@Query(value = "SELECT * FROM similarity WHERE ( sku = ?1 OR similar_sku = ?1 ) ORDER BY similarity DESC LIMIT ?2", nativeQuery = true)
	public List<Similarity> getSimilar(long sku, int limit);
	
	
}