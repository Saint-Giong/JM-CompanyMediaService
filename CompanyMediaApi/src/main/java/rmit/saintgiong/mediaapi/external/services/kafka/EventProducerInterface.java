package rmit.saintgiong.mediaapi.external.services.kafka;

public interface EventProducerInterface {

    void send(String requestTopic, Object requestData);
}
