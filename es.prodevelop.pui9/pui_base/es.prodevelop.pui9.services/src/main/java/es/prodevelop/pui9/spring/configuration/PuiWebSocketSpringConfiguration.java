package es.prodevelop.pui9.spring.configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.converter.ByteArrayMessageConverter;
import org.springframework.messaging.converter.CompositeMessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.util.ObjectUtils;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import es.prodevelop.pui9.data.converters.PuiGsonMessageConverter;
import es.prodevelop.pui9.eventlistener.PuiEventLauncher;
import es.prodevelop.pui9.eventlistener.event.WebSocketConnectionEvent;
import es.prodevelop.pui9.eventlistener.event.WebSocketDisconnectionEvent;
import es.prodevelop.pui9.eventlistener.event.WebSocketPingEvent;
import es.prodevelop.pui9.eventlistener.event.WebSocketSubscriptionEvent;
import es.prodevelop.pui9.eventlistener.event.WebSocketUnsubscriptionEvent;
import es.prodevelop.pui9.spring.configuration.annotations.PuiSpringConfiguration;

/**
 * Spring configuration for the PUI WebSockets messaging service. It registers
 * an endpoint for the application named <b>/pui9websockets</b>, used by Stomp
 * clients to manage the connection and subscriptions.<br>
 * <br>
 * It also enables a Message Broker and registers a prefix for this broker to
 * filter all the requests. This prefix is <b>/topic</b>
 * 
 * @author Marc Gil - mgil@prodevelop.es
 */
@PuiSpringConfiguration
@EnableWebSocketMessageBroker
public class PuiWebSocketSpringConfiguration implements WebSocketMessageBrokerConfigurer {

	private static final String PING_MESSAGE = "ping";

	private final Logger logger = LogManager.getLogger(this.getClass());

	@Autowired
	private PuiEventLauncher eventLauncher;

	@Bean
	@Primary
	public CompositeMessageConverter brokerMessageConverter() {
		List<MessageConverter> converters = new ArrayList<>();
		boolean registerDefaults = configureMessageConverters(converters);
		if (registerDefaults) {
			converters.add(new StringMessageConverter());
			converters.add(new ByteArrayMessageConverter());
			converters.add(new PuiGsonMessageConverter());
		}
		return new CompositeMessageConverter(converters);
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/pui9websockets").setAllowedOriginPatterns("*");
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic");
	}

	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		ChannelInterceptor interceptor = new ChannelInterceptor() {

			@Override
			public Message<?> preSend(Message<?> message, MessageChannel channel) {
				// store or remove the Session ID in the list depending on the
				// message is for Connecting or Disconnecting
				StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
				StompCommand command = accessor.getCommand();
				if (command == null) {
					return message;
				}

				String sessionId = accessor.getSessionId();
				String user = accessor.getFirstNativeHeader("user");
				String topic = accessor.getFirstNativeHeader("topic");
				Map<String, List<String>> headers = accessor.toNativeHeaderMap();
				if (ObjectUtils.isEmpty(sessionId)) {
					return message;
				}

				switch (command) {
				case CONNECT:
					logger.debug("User {} connected", user);
					eventLauncher.fireAsync(new WebSocketConnectionEvent(sessionId, user));
					break;
				case DISCONNECT:
					if (ObjectUtils.isEmpty(user)) {
						logger.debug("Unknown user disconnected");
					} else {
						logger.debug("User {} disconnected", user);
					}
					eventLauncher.fireAsync(new WebSocketDisconnectionEvent(sessionId, user));
					break;
				case SUBSCRIBE:
					logger.debug("Subscription: {} --> {}", user, topic);
					eventLauncher.fireAsync(new WebSocketSubscriptionEvent(sessionId, user, topic, headers));
					break;
				case UNSUBSCRIBE:
					logger.debug("Unubscription: {} --> {}", user, topic);
					eventLauncher.fireAsync(new WebSocketUnsubscriptionEvent(sessionId, user, topic, headers));
					break;
				case SEND:
					String text = new String((byte[]) message.getPayload());
					if (Objects.equals(text, PING_MESSAGE)) {
						eventLauncher.fireAsync(new WebSocketPingEvent(sessionId, user));
					}
					break;
				default:
					break;
				}

				return message;
			}
		};
		registration.interceptors(interceptor);
	}

}