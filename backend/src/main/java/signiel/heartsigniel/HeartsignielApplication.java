package signiel.heartsigniel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing // BaseTimeEntity를 생성했다면 꼭 어노테이션 추가
@SpringBootApplication
public class HeartsignielApplication {

	public static void main(String[] args) {
		SpringApplication.run(HeartsignielApplication.class, args);
	}

}
