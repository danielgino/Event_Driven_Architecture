import java.util.*;

public class Partition {
    private final List<String> events = new ArrayList<>();
    private int currentIndex = 0;
    private final int maxSize;

    public Partition(int maxSize) {
        this.maxSize = maxSize;
    }

    public void addEvent(String data) {
        if (events.size() < maxSize) {
            events.add(data);
        } else {
            events.set(currentIndex, data);
        }
        currentIndex = (currentIndex + 1) % maxSize;
    }

    public String getCurrentEvent() {
        if (events.isEmpty()) return null;
        int lastIndex = (currentIndex - 1 + events.size()) % events.size();
        return events.get(lastIndex);
    }

    public void rollback() {
        if (!events.isEmpty()) {
            currentIndex = (currentIndex - 1 + events.size()) % events.size();
        }
    }


    public List<String> getEvents() {
        return events;
    }
}
