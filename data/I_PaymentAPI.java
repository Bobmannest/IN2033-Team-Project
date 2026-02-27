package data;

/**
 *
 */
public interface I_PaymentAPI {
	/**
	 *
	 * @param amount
	 * @param orderId
	 * @return
	 */
	public boolean processPayment(int amount, String orderId);

	/**
	 *
	 * @param paymentId
	 * @return
	 */
	public boolean refundPayment(int paymentId);

	/**
	 *
	 * @param paymentId
	 * @return
	 */
	public String getPaymentStatus(String paymentId);
}