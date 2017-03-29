package engine.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import engine.entities.Article;

@Repository
public interface ArticleRepository extends CrudRepository<Article, Long> {

	public Article findById(Long id);
	public Article findByName(Long name);

}