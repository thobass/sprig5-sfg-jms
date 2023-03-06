package rocks.basset.spring5sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import rocks.basset.spring5sfgjms.config.JmsConfig;
import rocks.basset.spring5sfgjms.model.HelloWorldMessage;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate =  2000)
    public void sendMessage(){

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world")
                .build();

        jmsTemplate.convertAndSend(JmsConfig.MY_QUEUE, message);

        log.info("I'm sending a message...");
    }

    @Scheduled(fixedRate =  2000)
    public void sendAndReceiveMessage() throws JMSException {

        HelloWorldMessage message = HelloWorldMessage.builder()
                .id(UUID.randomUUID())
                .message("Hello world")
                .build();

        Message receiveMessage = jmsTemplate.sendAndReceive(JmsConfig.MY_SEND_RECEIVE_QUEUE, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                Message helloMessage = null;
                try {
                    helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                    helloMessage.setStringProperty("_type", "rocks.basset.spring5sfgjms.model.HelloWorldMessage");
                    log.info("Sending hello");
                    return helloMessage;
                } catch (JsonProcessingException e) {
                    throw new JMSException("kaboom");
                }
            }
        });

        log.info("Message sent.");
        log.info("{}", receiveMessage.getBody(String.class));
    }
}
