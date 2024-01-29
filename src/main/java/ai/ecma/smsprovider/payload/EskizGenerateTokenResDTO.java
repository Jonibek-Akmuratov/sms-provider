package ai.ecma.smsprovider.payload;


import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EskizGenerateTokenResDTO {

    private String message;

    @JsonAlias("token_type")
    private String tokenType;

    private EskizTokenResDTO data;

}

