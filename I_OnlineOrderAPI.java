/**
 *
 */
public interface I_OnlineOrderAPI {
	/**
	 *
	 * @param orderId
	 * @return
	 */
	public String getOrderDetails(String orderId);

	/**
	 *
	 * @param orderId
	 * @param status
	 */
	public void updateOrderStatus(String orderId, String status);
}