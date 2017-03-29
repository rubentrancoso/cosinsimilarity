package engine.repositories;

import java.util.Collection;

import engine.entities.Article;

public interface ArticleRepositoryExt {

	public <T extends Article> Collection<T> bulkSave(Collection<T> entities);

}
