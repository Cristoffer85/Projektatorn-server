package cristoffer85.com.projektatornserver.RABBITMQ.service;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cristoffer85.com.projektatornserver.RABBITMQ.dto.MsgDto;

@Component
public class MsgProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    public void sendMsg(MsgDto msgDTO) {
        // Optionally, declare the queue if you use dynamic queues
        String queueName = "chat_" + msgDTO.getSender() + "_to_" + msgDTO.getReceiver() + "_Receiver";
        Queue queue = new Queue(queueName, false);
        rabbitAdmin.declareQueue(queue);

        // Send the DTO directly (no need to create a Map)
        rabbitTemplate.convertAndSend("chatQueue", msgDTO);
        System.out.println("Message sent to " + queue.getName() + ": " + msgDTO);
    }
}