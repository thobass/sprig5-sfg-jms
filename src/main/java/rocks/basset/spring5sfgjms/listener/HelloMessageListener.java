package rocks.basset.spring5sfgjms.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import rocks.basset.spring5sfgjms.config.JmsConfig;
import rocks.basset.spring5sfgjms.model.HelloWorldMessage;

import javax.jms.Message;

@Component
@Slf4j
public class HelloMessageListener {

    @JmsListener(destination = JmsConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage helloWorldMessage, @Headers MessageHeaders headers, Message message){
        log.info("I Got a message");
        log.info("{}", helloWorldMessage);
    }
}
