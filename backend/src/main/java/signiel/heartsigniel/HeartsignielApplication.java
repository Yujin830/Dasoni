package signiel.heartsigniel;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import signiel.heartsigniel.model.guide.GuideRepo;

@SpringBootApplication
public class HeartsignielApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeartsignielApplication.class, args);

	}

}
