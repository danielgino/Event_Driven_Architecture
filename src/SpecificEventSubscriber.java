import interfaces.EventSubscriber;

import java.util.function.BiConsumer;

public class SpecificEventSubscriber implements EventSubscriber {
    private final String name;
    private final BiConsumer<String, String> callback;

    public SpecificEventSubscriber(String name, BiConsumer<String, String> callback) {
        this.name = name;
        this.callback = callback;
    }

    @Override
    public void handleEvent(String eventType, String data) {
        System.out.println("Subscriber " + name + " received event: " + eventType);
        callback.accept(eventType, data);
    }
}
