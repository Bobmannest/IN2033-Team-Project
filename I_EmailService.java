public interface I_EmailService {

	public boolean sendEmail(String to, String subject, String body);

	public boolean sendAttachment(String to, String subject, String body, File attachment);
}