package ai.ecma.eskizsmsprovider.payload;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EskizSendSMSResDTO {

    private UUID id;
    private String message;
    private String status;

}
