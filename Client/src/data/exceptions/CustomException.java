package data.exceptions;

public class CustomException extends Throwable{

	private static final long serialVersionUID = 1L;
	
	private String message;
	private Exception exception;
	
	private long created;
	
	public CustomException(Exception exception, String message){
		this.message = message;
		this.exception = exception;
		this.created = System.currentTimeMillis();
	}
	
	public String getErrorMessage(){
		return this.message;
	}

	public Exception getException() {
		return exception;
	}

	public long getCreatedTime() {
		return created;
	}

}
