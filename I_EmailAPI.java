public interface I_EmailAPI {

	public boolean sendEmail(String to, String subject, String body);

	public boolean sendAttachment(String to, String subject, String body, File attachment);
}