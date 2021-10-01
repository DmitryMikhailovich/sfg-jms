package guru.springframework.sfgjms.listener;

import guru.springframework.sfgjms.config.JMSConfig;
import guru.springframework.sfgjms.model.HelloWorldMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import java.util.UUID;

@RequiredArgsConstructor
@Component
public class HelloMessageListener {

    private final JmsTemplate jmsTemplate;

    @JmsListener(destination = JMSConfig.MY_QUEUE)
    public void listen(@Payload HelloWorldMessage hwMessage,
                       @Headers MessageHeaders headers, Message message) {

        System.out.println("I've got a message!");

        System.out.println(hwMessage.getMessage());
    }

    @JmsListener(destination = JMSConfig.MY_SEND_RCV_QUEUE)
    public void listenForHello(@Payload HelloWorldMessage hwMessage,
                       @Headers MessageHeaders headers, Message message) throws JMSException {

        HelloWorldMessage payloadMessage = new HelloWorldMessage();
        payloadMessage.setId(UUID.randomUUID());
        payloadMessage.setMessage("World!");

        System.out.print(hwMessage.getMessage());

        jmsTemplate.convertAndSend(message.getJMSReplyTo(), payloadMessage);
    }
}
