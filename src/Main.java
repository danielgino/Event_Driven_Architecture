import interfaces.EventSubscriber;

public class Main {
    public static void main(String[] args) {
        System.out.println("Creating partitions:");
        EventBus.createPartition(0, 3);
        EventBus.createPartition(1, 3);
        OrderService service = new OrderService();

        System.out.println("\nCreating subscribers per partition and event type...");

        EventSubscriber sub1 = new SpecificEventSubscriber("Sub1", (event, data) -> {
            if (event.equals("OrderPlaced")) {
                System.out.println("Sub1 -> Received OrderPlaced for order " + data);
            }
        });

        EventSubscriber sub2 = new SpecificEventSubscriber("Sub2", (event, data) -> {
            if (event.equals("OrderConfirmed")) {
                System.out.println("Sub2 -> Confirming order " + data);
            }
        });

        EventSubscriber sub3 = new SpecificEventSubscriber("Sub3", (event, data) -> {
            System.out.println("Sub3 -> General handler for " + event + " on order " + data);
        });

        EventBus.subscribe("OrderPlaced", 0, sub1);
        EventBus.subscribe("OrderConfirmed", 1, sub2);
        EventBus.subscribe("OrderPlaced", 0, sub3);
        EventBus.subscribe("OrderConfirmed", 1, sub3);

        System.out.println("\n=== Publishing to Partition 0 ===");
        service.placeOrder(101, 0);


        System.out.println("\n===  Publishing to Partition 1 ===");
        service.placeOrder(202, 1);


        System.out.println("\n===  Viewing partition contents ===");
        EventBus.printPartitionEvents(0);
        EventBus.printPartitionEvents(1);

        System.out.println("\n===  Adding more orders to Partition 0 ===");
        service.placeOrder(303, 0);
        service.placeOrder(404, 0);
        EventBus.printPartitionEvents(0);


        System.out.println("\n===  Rolling back Partition 0 three times ===");
        EventBus.rollbackPartition(0);
        EventBus.rollbackPartition(0);
        EventBus.rollbackPartition(0);

        System.out.println(" Current event after rollback: "
                + EventBus.getCurrentEvent(0));

    }
}
