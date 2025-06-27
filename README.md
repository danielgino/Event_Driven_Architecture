# Object Oriented Programming 2 – Assignment 4
## Event Driven Architecture with Partitioned Pub/Sub System


## Objective
This assignment focuses on implementing an Event Driven Architecture (EDA) in Java. It enhances a standard Pub/Sub mechanism by supporting:

Multiple partitions

Circular event buffers per partition

Rollback functionality to previously published events

📁 Project Structure
```
├── interfaces
│   └── EventSubscriber.java     # Subscriber interface
├── EventBus.java                # Core Pub/Sub manager with partition support
├── Partition.java               # Circular buffer per partition
├── SpecificEventSubscriber.java# Subscriber with custom callback
├── OrderService.java           # Publisher that sends events to the EventBus
└── Main.java                    # Simulation and usage demo
📚 Features Implemented
Feature	Description
✅ Event Publishing by Partition	Events are published to a specific partition.
✅ Subscriber per Event & Partition	Subscribers register for specific event types and partitions.
✅ Circular Event Buffer	Each partition holds a fixed-size buffer of recent events. New events overwrite oldest ones in a cyclic manner.
✅ Rollback Support	Rollback mechanism resets the partition's current event pointer to previous events using modular arithmetic.
✅ Logging & Output	System prints current event buffers and logs every received event per subscriber.
```
## Input

```
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
```
 ## Output
```
Creating partitions:

Creating subscribers per partition and event type...

=== Publishing to Partition 0 ===
Order 101 has been placed.
Subscriber Sub1 received event: OrderPlaced
Sub1 -> Received OrderPlaced for order 101
Subscriber Sub3 received event: OrderPlaced
Sub3 -> General handler for OrderPlaced on order 101

===  Publishing to Partition 1 ===
Order 202 has been placed.
Subscriber Sub2 received event: OrderConfirmed
Sub2 -> Confirming order 202
Subscriber Sub3 received event: OrderConfirmed
Sub3 -> General handler for OrderConfirmed on order 202

===  Viewing partition contents ===
Events in Partition 0: [101, 101]
Events in Partition 1: [202, 202]

===  Adding more orders to Partition 0 ===
Order 303 has been placed.
Subscriber Sub1 received event: OrderPlaced
Sub1 -> Received OrderPlaced for order 303
Subscriber Sub3 received event: OrderPlaced
Sub3 -> General handler for OrderPlaced on order 303
Order 404 has been placed.
Subscriber Sub1 received event: OrderPlaced
Sub1 -> Received OrderPlaced for order 404
Subscriber Sub3 received event: OrderPlaced
Sub3 -> General handler for OrderPlaced on order 404
Events in Partition 0: [303, 404, 404]

===  Rolling back Partition 0 three times ===
RollBack Partition 0 rolled back to event: 404
RollBack Partition 0 rolled back to event: 303
RollBack Partition 0 rolled back to event: 404
 Current event after rollback: 404


```
