/**
 *
 */
public interface I_CommercialApplicationAPI {
	/**
	 *
	 * @param businessName
	 * @param contactEmail
	 * @param address
	 * @return
	 */
	public String submitApplication(String businessName, String contactEmail, String address);

	/**
	 *
	 * @param applicationId
	 * @return
	 */
	public String getApplicationStatus(String applicationId);

	/**
	 *
	 * @param applicationId
	 * @param decision
	 * @param decidedBy
	 * @return
	 */
	public boolean decideApplication(String applicationId, String decision, String decidedBy);
}