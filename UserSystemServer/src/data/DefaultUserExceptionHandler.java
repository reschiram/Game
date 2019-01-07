package data;

import data.exceptions.ClientLoginException;
import data.exceptions.ClientValidationException;
import data.exceptions.CustomException;
import data.exceptions.LoginInformationCreationException;
import data.exceptions.LoginInformationReadingException;
import data.exceptions.UserDatabaseReadingException;
import data.exceptions.handler.ErrorHandler;

public class DefaultUserExceptionHandler{	

	private static DefaultUserExceptionHandler defaultExceptionHandler;
	
	public static DefaultUserExceptionHandler getDefaultUserExceptionHandler(){
		if(defaultExceptionHandler == null) defaultExceptionHandler = new DefaultUserExceptionHandler();
		return defaultExceptionHandler;
	}

	private ErrorHandler defaultHandler_ClientValidationException;
	private ErrorHandler defaultHandler_ClientLoginException;
	private ErrorHandler defaultHandler_LoginInformationCreationException;
	private ErrorHandler defaultHandler_LoginInformationReadingException;
	private ErrorHandler defaultHandler_UserDatabaseReadingException;
	
	public DefaultUserExceptionHandler(){
		
		this.defaultHandler_ClientValidationException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof ClientValidationException){
					ClientValidationException cv = (ClientValidationException)exception;
					System.out.println(cv.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_ClientLoginException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof ClientLoginException){
					ClientLoginException cl = (ClientLoginException)exception;
					System.out.println(cl.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_LoginInformationCreationException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof LoginInformationCreationException){
					LoginInformationCreationException lic = (LoginInformationCreationException)exception;
					System.out.println(lic.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_UserDatabaseReadingException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof UserDatabaseReadingException){
					UserDatabaseReadingException udr = (UserDatabaseReadingException)exception;
					System.out.println(udr.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_LoginInformationReadingException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof LoginInformationReadingException){
					LoginInformationReadingException lir = (LoginInformationReadingException)exception;
					System.out.println(lir.getErrorMessage());
				}
			}
		};
	}
	
	public ErrorHandler getDefaultHandler_ClientValidationException() {
		return defaultHandler_ClientValidationException;
	}
	
	public ErrorHandler getDefaultHandler_ClientLoginException() {
		return defaultHandler_ClientLoginException;
	}
	
	public void setDefaultHandler_ClientValidationException(ErrorHandler defaultHandler_ClientValidationException) {
		this.defaultHandler_ClientValidationException = defaultHandler_ClientValidationException;
	}
	
	public void setDefaultHandler_ClientLoginException(ErrorHandler defaultHandler_ClientLoginException) {
		this.defaultHandler_ClientLoginException = defaultHandler_ClientLoginException;
	}

	public ErrorHandler getDefaultHandler_LoginInformationCreationException() {
		return defaultHandler_LoginInformationCreationException;
	}

	public void setDefaultHandler_LoginInformationCreationException(
			ErrorHandler defaultHandler_LoginInformationCreationException) {
		this.defaultHandler_LoginInformationCreationException = defaultHandler_LoginInformationCreationException;
	}

	public ErrorHandler getDefaultHandler_UserDatabaseReadingException() {
		return defaultHandler_UserDatabaseReadingException;
	}

	public void setDefaultHandler_UserDatabaseReadingException(ErrorHandler defaultHandler_UserDatabaseReadingException) {
		this.defaultHandler_UserDatabaseReadingException = defaultHandler_UserDatabaseReadingException;
	}

	public ErrorHandler getDefaultHandler_LoginInformationReadingException() {
		return defaultHandler_LoginInformationReadingException;
	}

	public void setDefaultHandler_LoginInformationReadingException(
			ErrorHandler defaultHandler_LoginInformationReadingException) {
		this.defaultHandler_LoginInformationReadingException = defaultHandler_LoginInformationReadingException;
	}
}
