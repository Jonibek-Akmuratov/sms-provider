package ai.ecma.smsprovider.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class EskizTokenAndExpiredDateDTO {

    private String token;
    private LocalDate expiredDate;

}
