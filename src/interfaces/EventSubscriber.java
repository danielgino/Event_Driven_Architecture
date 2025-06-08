package interfaces;

public interface EventSubscriber {
    void handleEvent(String eventType, String data);

}
