package ai.ecma.eskizsmsprovider.payload;


import ai.ecma.eskizsmsprovider.utils.AppConstants;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EskizSendSMSDTO {

    @JsonAlias("mobile_phone")
    private String mobilePhone;

    private String message;

    private String from = AppConstants.FROM;

    @JsonAlias("callback_url")
    private String callbackUrl=AppConstants.CALLBACK_URL;


    public EskizSendSMSDTO(String mobilePhone, String message) {
        this.mobilePhone = mobilePhone;
        this.message = message;
    }
}
