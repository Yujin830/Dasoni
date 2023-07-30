package signiel.heartsigniel.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import signiel.heartsigniel.model.chat.StompHandler;



@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
/**
 * 채팅 시스템 개발을 위핸 Config Class
 */
public class ChatConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;


    /**
     * 소켓 연결과 관련된 설정
     *
     * addEndpoint : 소켓 연결 URL
     * setAllowedOriginPatterns : 소켓 CORS 설정
     * withSocketJS : 소켓을 지원하지 않는 브라우저라면 socketJs를 사용하도록 설정.
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        
        registry.addEndpoint("/ws/chat").setAllowedOriginPatterns("*").withSockJS();
    }

    /**
     *  WebSocket 클라이언트로부터 들어오는 메시지를 처리
     *
     */
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }


    /**
     * STOMP 사용을 위한 MessageBroker 설정
     *
     * enableSimpleBroker : 메세지를 받을 때 경로 설정
     * /queue, /topic을 통해 1:1 , 1:N 설정
     * 두 API가 Perfix로 붙은 경우 messageBroker가 해당 경로를 가로 챔.
     *
     * setApplicationDestinationPrefixes : 메세지를 보낼 때 관련 경로 설정
     * 클라이언트가 메시지를 보낼 때 경로 앞에 /app이 붙어있으면 Broker로 보내짐.
     *
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/queue", "/topic");
        registry.setApplicationDestinationPrefixes("/pub");
    }
}
