public class OrderService {
    public void placeOrder(int orderId, int partition) {
        String data = String.valueOf(orderId);
        System.out.println("Order " + orderId + " has been placed.");
        EventBus.publish("OrderPlaced", data, partition);
        EventBus.publish("OrderConfirmed", data, partition);
    }
}
