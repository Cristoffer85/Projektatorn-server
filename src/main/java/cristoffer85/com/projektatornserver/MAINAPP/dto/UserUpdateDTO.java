package cristoffer85.com.projektatornserver.MAINAPP.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
@Data
public class UserUpdateDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;
    private String email;
    private String forestanimal;
    private String soursnack;
    private String avatar;
}
