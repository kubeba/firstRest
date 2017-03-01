package classes;

public class RestResponse {
	private String errorCode;
	private String message;
	private String httpCode;
	private String sqlState;
	
	public String getErrorCode() {
		return errorCode;
	}
	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getHttpCode() {
		return httpCode;
	}
	public void setHttpCode(String httpCode) {
		this.httpCode = httpCode;
	}
	public String getSqlState() {
		return sqlState;
	}
	public void setSqlState(String sqlState) {
		this.sqlState = sqlState;
	}
}
