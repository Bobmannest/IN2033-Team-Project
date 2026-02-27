/**
 *
 */
public interface I_EmailAPI {
	/**
	 *
	 * @param to
	 * @param subject
	 * @param body
	 * @return
	 */
	public boolean sendEmail(String to, String subject, String body);

	/**
	 *
	 * @param to
	 * @param subject
	 * @param body
	 * @param attachment
	 * @return
	 */
	public boolean sendAttachment(String to, String subject, String body, File attachment);
}