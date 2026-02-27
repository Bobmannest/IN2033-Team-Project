public interface I_InventoryAPI {

	public boolean withdrawInventory(String item, int amount);

	default String getInventory(String productId) {
		throw new UnsupportedOperationException();
	}

	public void createNewOrder(String orderId, String orderDetails);
}