import interfaces.EventSubscriber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventBus {
    private static final Map<String, Map<Integer, List<EventSubscriber>>> subscribers = new HashMap<>();
    private static final Map<Integer, Partition> partitions = new HashMap<>();

    public static void createPartition(int partitionId, int bufferSize) {
        partitions.put(partitionId, new Partition(bufferSize));
    }

    public static void subscribe(String eventType, int partition, EventSubscriber subscriber) {
        subscribers
                .computeIfAbsent(eventType, k -> new HashMap<>())
                .computeIfAbsent(partition, k -> new ArrayList<>())
                .add(subscriber);
    }

    public static void publish(String eventType, String data, int partition) {
        Partition part = partitions.get(partition);
        if (part != null) {
            part.addEvent(data);
        }

        Map<Integer, List<EventSubscriber>> partitionSubscribers = subscribers.get(eventType);
        if (partitionSubscribers != null) {
            List<EventSubscriber> subs = partitionSubscribers.get(partition);
            if (subs != null) {
                for (EventSubscriber sub : subs) {
                    sub.handleEvent(eventType, data);
                }
            }
        }
    }

    public static void rollbackPartition(int partitionId) {
        Partition part = partitions.get(partitionId);
        if (part != null) {
            part.rollback();
            String rolledBackEvent = part.getCurrentEvent();
            System.out.println("RollBack Partition " + partitionId + " rolled back to event: " + rolledBackEvent);
        }
    }

    public static void printPartitionEvents(int partitionId) {
        Partition part = partitions.get(partitionId);
        if (part != null) {
            System.out.println("Events in Partition " + partitionId + ": " + part.getEvents());
        }
    }

    public static String getCurrentEvent(int partitionId) {
        Partition part = partitions.get(partitionId);
        if (part != null) {
            return part.getCurrentEvent();
        }
        return null;
    }

}
