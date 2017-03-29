package engine.repositories;

import java.util.Collection;
import java.util.List;

import engine.entities.Similarity;

public interface SimilarityRepositoryExt {

	public List<Similarity> findSimilar(long sku, int limit);
	public <T extends Similarity> Collection<T> bulkSave(Collection<T> entities);

}
