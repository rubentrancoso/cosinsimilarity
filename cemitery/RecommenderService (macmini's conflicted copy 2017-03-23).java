package engine.services;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import engine.entities.Article;
import engine.repositories.ArticleRepository;

@Service
@Transactional
public class RecommenderService {

	@Autowired
	private ArticleRepository articleRepository;

	public Article recommend(Long articleName) {
		return articleRepository.findByName(articleName);
	}

}