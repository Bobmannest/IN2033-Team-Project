package PU_DetailedDesign;

public interface I_InventoryAPI {

	/**
	 * Reduces the stock level of a specific product in the inventory.
	 *
	 * @param item the unique identifier or name of the product
	 * @param amount the quantity to withdraw from stock (must be positive)
	 * @return true if the withdrawal succeeds; false if insufficient stock
	 */
	public boolean withdrawInventory(String item, int amount);

	/**
	 * Retrieves the current inventory information for a product.
	 *
	 * @param productId the unique identifier of the product
	 * @return a String describing the current stock level
	 */
	default String getInventory(String productId) {
		throw new UnsupportedOperationException();
	}

	/**
	 * Creates a new order record and updates inventory accordingly.
	 *
	 * @param orderId the unique identifier of the order
	 * @param orderDetails details of products and quantities included in the order
	 */
	public void createNewOrder(String orderId, String orderDetails);
}