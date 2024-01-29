package ai.ecma.eskizsmsprovider.repository;

import ai.ecma.eskizsmsprovider.entity.EskizSendMessage;
import jakarta.persistence.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EskizSendMessageRepository extends JpaRepository<EskizSendMessage, UUID> {

}
