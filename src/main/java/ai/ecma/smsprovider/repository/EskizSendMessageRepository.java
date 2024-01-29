package ai.ecma.smsprovider.repository;

import ai.ecma.eskizsmsprovider.entity.EskizSendMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;


@Repository
public interface EskizSendMessageRepository extends JpaRepository<EskizSendMessage, UUID> {

}
