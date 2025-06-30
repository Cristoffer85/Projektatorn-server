package cristoffer85.com.projektatornserver.RABBITMQ.dto;

import lombok.Data;

@Data
public class MsgDto {
    private String sender;
    private String receiver;
    private String content;
}