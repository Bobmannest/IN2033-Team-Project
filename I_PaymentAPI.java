public interface I_PaymentAPI {

	public boolean processPayment(int amount, String orderId);

	public boolean refundPayment(int paymentId);

	public PaymentStatus getPaymentStatus(String paymentId);
}