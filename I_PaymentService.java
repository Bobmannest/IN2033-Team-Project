public interface I_PaymentService {

	public boolean processPayment(int amount, String orderId);

	public boolean refundPayment(int paymentId);

	public PaymentStatus getPaymentStatus(String paymentId);
}