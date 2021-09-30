package guru.springframework.sfgjms.sender;

import guru.springframework.sfgjms.config.JMSConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {

        HelloWorldMessage message = new HelloWorldMessage();
        message.setId(UUID.randomUUID());
        message.setMessage("Hello world!");

        jmsTemplate.convertAndSend(JMSConfig.MY_QUEUE, message);
    }
}
