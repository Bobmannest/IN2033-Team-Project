/**
 *
 */
public interface I_InventoryAPI {
	/**
	 *
	 * @param item
	 * @param amount
	 * @return
	 */
	public boolean withdrawInventory(String item, int amount);

	/**
	 *
	 * @param productId
	 * @return
	 */
	default String getInventory(String productId) {
		throw new UnsupportedOperationException();
	}

	/**
	 *
	 * @param orderId
	 * @param orderDetails
	 */
	public void createNewOrder(String orderId, String orderDetails);
}