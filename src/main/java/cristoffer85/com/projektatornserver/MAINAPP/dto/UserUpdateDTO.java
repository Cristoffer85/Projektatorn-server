package cristoffer85.com.projektatornserver.MAINAPP.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.YearMonth;

@Data
public class UserUpdateDTO {
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM")
    private YearMonth birthdate;
    private String email;
    private String forestanimal;
    private String soursnack;
    private String avatar;
}
