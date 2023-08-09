package signiel.heartsigniel.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import signiel.heartsigniel.model.chat.dto.ChatMessage;
import signiel.heartsigniel.model.meeting.dto.SignalMatchingResult;
import signiel.heartsigniel.model.meeting.dto.SingleSignalRequest;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Long> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Long> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setValueSerializer(new GenericToStringSerializer<>(Long.class));
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, ChatMessage> chatMessageRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, ChatMessage> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<ChatMessage> serializer = new Jackson2JsonRedisSerializer<>(ChatMessage.class);
        serializer.setObjectMapper(new ObjectMapper());

        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean
    public RedisTemplate<String, SignalMatchingResult> signalResultRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, SignalMatchingResult> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<SignalMatchingResult> serializer = new Jackson2JsonRedisSerializer<>(SignalMatchingResult.class);
        serializer.setObjectMapper(new ObjectMapper());

        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }

    @Bean RedisTemplate<String, SingleSignalRequest> singleSignalRequestRedisTemplate(RedisConnectionFactory connectionFactory){
        RedisTemplate<String, SingleSignalRequest> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        Jackson2JsonRedisSerializer<SingleSignalRequest> serializer = new Jackson2JsonRedisSerializer<>(SingleSignalRequest.class);
        serializer.setObjectMapper(new ObjectMapper());

        template.setValueSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        return template;
    }
}
