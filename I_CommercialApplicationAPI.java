public interface I_CommercialApplicationAPI {

	public String submitApplication(String businessName, String contactEmail, String address);

	public String getApplicationStatus(String applicationId);

	public boolean decideApplication(String applicationId, String decision, String decidedBy);
}