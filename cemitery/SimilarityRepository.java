package engine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import engine.entities.Similarity;

@Repository
public interface SimilarityRepository extends CrudRepository<Similarity, String> {

	public Similarity findById(Long id);
	public Similarity findByName(String name);

//	@Query(value = "CREATE TABLE TEST AS SELECT * FROM CSVREAD(?1)", nativeQuery = true)
//	public void loadData(String csvFile);
}