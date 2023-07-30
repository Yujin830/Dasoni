package signiel.heartsigniel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import signiel.heartsigniel.model.member.Member;

@Configuration
public class RedisConfig {

    @Value("${spring.redis.host}")
    private String redisHost;

    @Value("${spring.redis.port}")
    private int redisPort;

    @Bean
    public RedisTemplate<String, Member> redisTemplate() {
        RedisTemplate<String, Member> template = new RedisTemplate<>();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        lettuceConnectionFactory.afterPropertiesSet();  // 추가된 코드
        template.setConnectionFactory(lettuceConnectionFactory);
        return template;
    }
}
