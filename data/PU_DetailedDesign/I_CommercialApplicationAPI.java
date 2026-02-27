package PU_DetailedDesign;

/**
 * Defines operations related to handling commercial membership applications
 * within the IPOS system.
 *
 * This API allows submission of new applications, checking application status,
 * and recording approval or rejection decisions.
 */
public interface I_CommercialApplicationAPI {

	/**
	 * Submits a new commercial application.
	 *
	 * @param businessName the name of the business applying for membership
	 * @param contactEmail the contact email address of the applicant
	 * @param address the registered business address
	 * @return a unique application ID generated for the submitted application
	 */
	public String submitApplication(String businessName, String contactEmail, String address);

	/**
	 * Retrieves the current status of a submitted application.
	 *
	 * @param applicationId the unique identifier of the application
	 * @return a String describing the application status (e.g., PENDING, APPROVED, REJECTED)
	 */
	public String getApplicationStatus(String applicationId);

	/**
	 * Records a decision for a submitted application.
	 *
	 * @param applicationId the unique identifier of the application
	 * @param decision the decision made (e.g., APPROVED or REJECTED)
	 * @param decidedBy the name or identifier of the staff member making the decision
	 * @return true if the decision is recorded successfully; false otherwise
	 */
	public boolean decideApplication(String applicationId, String decision, String decidedBy);
}