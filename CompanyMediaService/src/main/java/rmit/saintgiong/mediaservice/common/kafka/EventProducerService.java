package rmit.saintgiong.mediaservice.common.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import rmit.saintgiong.mediaapi.external.services.kafka.EventProducerInterface;

@Component
public class EventProducerService implements EventProducerInterface {

    private static final Logger log = LoggerFactory.getLogger(EventProducerService.class);

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public EventProducerService(
            KafkaTemplate<String, Object> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void send(String requestTopic, Object requestData) {
        log.debug("Sending Avro record to topic: {}", requestTopic);
        kafkaTemplate.send(requestTopic, requestData);
        log.debug("Avro record sent successfully to topic: {}", requestTopic);
    }
}
