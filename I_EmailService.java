public interface I_EmailService {

	public boolean sendEmail(String to__subject__body, String subject, String body);

	public boolean sendAttachment(String to, String subject, String body, File attachment);
}