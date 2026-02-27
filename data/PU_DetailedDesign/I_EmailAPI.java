package PU_DetailedDesign;
import java.io.File;

/**
 * Defines email-related operations within the IPOS system.
 *
 * This API allows sending standard emails and emails
 * with file attachments.
 */
public interface I_EmailAPI {

	/**
	 * Sends a basic email message.
	 *
	 * @param to the recipient's email address
	 * @param subject the subject line of the email
	 * @param body the main content of the email
	 * @return true if the email is sent successfully; false otherwise
	 */
	public boolean sendEmail(String to, String subject, String body);

	/**
	 * Sends an email with a file attachment.
	 *
	 * @param to the recipient's email address
	 * @param subject the subject line of the email
	 * @param body the main content of the email
	 * @param attachment the file to be attached to the email
	 * @return true if the email is sent successfully; false otherwise
	 */
	public boolean sendAttachment(String to, String subject, String body, File attachment);
}