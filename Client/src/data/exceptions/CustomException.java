package data.exceptions;

public class CustomException extends Throwable{

	private static final long serialVersionUID = 1L;
	
	private String message;
	private Exception exception;
	
	public CustomException(Exception exception, String message){
		this.message = message;
		this.exception = exception;
	}
	
	public String getErrorMessage(){
		return this.message;
	}

	public Exception getException() {
		return exception;
	}

}
