package engine;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import engine.repositories.ArticleRepository;
import engine.util.Initializer;

@EnableAutoConfiguration
@EnableJpaRepositories (basePackageClasses = {ArticleRepository.class})
@EntityScan (basePackages = {"engine.entities"})
@SpringBootApplication(scanBasePackages={"engine"})
public class Application {

	@Autowired
	Initializer initializer;
	
    public static void main(String[] args) {
    	SpringApplication.run(Application.class, args);
    }
    
	@PostConstruct
	private void init() {
		initializer.init();
	}    

}
