package guru.springframework.sfgjms.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import guru.springframework.sfgjms.config.JMSConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloSender {

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;

    @Scheduled(fixedRate = 2000)
    public void sendMessage() {

        HelloWorldMessage message = new HelloWorldMessage();
        message.setId(UUID.randomUUID());
        message.setMessage("Hello world!");

        jmsTemplate.convertAndSend(JMSConfig.MY_QUEUE, message);
    }

    @Scheduled(fixedRate = 2000)
    public void sendAndReceiveMessage() throws JMSException {

        HelloWorldMessage message = new HelloWorldMessage();
        message.setId(UUID.randomUUID());
        message.setMessage("Hello...");

        Message receivedMessage = jmsTemplate.sendAndReceive(JMSConfig.MY_SEND_RCV_QUEUE, session -> {
            Message helloMessage = null;
            try {
                helloMessage = session.createTextMessage(objectMapper.writeValueAsString(message));
                helloMessage.setStringProperty("_type", "guru.springframework.sfgjms.model.HelloWorldMessage");
            } catch (JsonProcessingException e) {
                throw new JMSException("pew");
            }

            return helloMessage;
        });

        System.out.println(receivedMessage.getBody(String.class));
    }
}
