public interface I_CA_InventoryAPI {

	public boolean checkStock(String itemId, int qty);

	public boolean deductStock(String itemId, int qty);

	public boolean updateStockAfterSale(String orderId);
}