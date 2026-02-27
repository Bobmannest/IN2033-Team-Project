package PU_DetailedDesign;

/**
 * Defines payment-related operations within the IPOS system.
 *
 * This API allows processing payments, issuing refunds,
 * and retrieving the status of payments.
 */
public interface I_PaymentAPI {

	/**
	 * Processes a payment for a specific order.
	 *
	 * @param amount the amount to be charged for the order
	 * @param orderId the unique identifier of the order being paid for
	 * @return true if the payment is successful; false otherwise
	 */
	public boolean processPayment(int amount, String orderId);

	/**
	 * Issues a refund for a previously processed payment.
	 *
	 * @param paymentId the unique identifier of the payment to refund
	 * @return true if the refund is successful; false otherwise
	 */
	public boolean refundPayment(int paymentId);

	/**
	 * Retrieves the current status of a payment.
	 *
	 * @param paymentId the unique identifier of the payment
	 * @return a String describing the payment status (e.g., SUCCESS, FAILED, REFUNDED)
	 */
	public String getPaymentStatus(String paymentId);
}