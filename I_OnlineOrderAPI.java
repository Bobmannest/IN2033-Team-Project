public interface I_OnlineOrderAPI {

	public String getOrderDetails(String orderId);

	public void updateOrderStatus(String orderId, String status);
}