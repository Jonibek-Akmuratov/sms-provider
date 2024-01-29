package ai.ecma.smsprovider.entity;

import ai.ecma.eskizsmsprovider.entity.template.AbsUUIDEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class EskizSendMessage extends AbsUUIDEntity {


    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false)
    private Boolean send;

    @Column
    private UUID hash;

    @Column
    private UUID eskizId;

    @Column
    private String eskizMessage;

    @Column
    private String eskizStatus;

}
