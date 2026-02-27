package PU_DetailedDesign;

/**
 * Defines operations related to managing online orders within the IPOS system.
 *
 * This API allows retrieval of order details and updating
 * the status of online orders.
 */
public interface I_OnlineOrderAPI {

	/**
	 * Retrieves the details of a specific online order.
	 *
	 * @param orderId the unique identifier of the order
	 * @return a String containing the details of the order
	 */
	public String getOrderDetails(String orderId);

	/**
	 * Updates the status of an existing online order.
	 *
	 * @param orderId the unique identifier of the order
	 * @param status the new status of the order (e.g., RECEIVED, DISPATCHED, DELIVERED)
	 */
	public void updateOrderStatus(String orderId, String status);
}