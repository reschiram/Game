package data;

import data.exceptions.CustomException;
import data.exceptions.LoginSecurityException;
import data.exceptions.LogoutSecurityException;
import data.exceptions.SecurityPackageCreationException;
import data.exceptions.handler.ErrorHandler;

public class DefaultSecurityExceptionHandler {
	
	private static DefaultSecurityExceptionHandler defaultSecurityExceptionHandler;

	public static DefaultSecurityExceptionHandler getDefaultSecurityExceptionHandler(){
		if(defaultSecurityExceptionHandler == null) defaultSecurityExceptionHandler = new DefaultSecurityExceptionHandler();
		return defaultSecurityExceptionHandler;
	}
	
	private ErrorHandler defaultHandler_LoginSecurityException;
	private ErrorHandler defaultHandler_LogoutSecurityException;
	private ErrorHandler defaultHandler_SecurityPackageCreationException;
	
	public DefaultSecurityExceptionHandler(){
		
		this.defaultHandler_LoginSecurityException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof LoginSecurityException){
					LoginSecurityException ls = (LoginSecurityException)exception;
					System.out.println(ls.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_LogoutSecurityException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof LogoutSecurityException){
					LogoutSecurityException ls = (LogoutSecurityException)exception;
					System.out.println(ls.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_SecurityPackageCreationException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof SecurityPackageCreationException){
					SecurityPackageCreationException spc = (SecurityPackageCreationException)exception;
					System.out.println(spc.getErrorMessage());
				}
			}
		};
	}

	public ErrorHandler getDefaultHandler_LoginSecurityException() {
		return defaultHandler_LoginSecurityException;
	}

	public void setDefaultHandler_LoginSecurityException(ErrorHandler defaultHandler_LoginSecurityException) {
		this.defaultHandler_LoginSecurityException = defaultHandler_LoginSecurityException;
	}

	public ErrorHandler getDefaultHandler_LogoutSecurityException() {
		return defaultHandler_LogoutSecurityException;
	}

	public void setDefaultHandler_LogoutSecurityException(ErrorHandler defaultHandler_LogoutSecurityException) {
		this.defaultHandler_LogoutSecurityException = defaultHandler_LogoutSecurityException;
	}

	public ErrorHandler getDefaultHandler_SecurityPackageCreationException() {
		return defaultHandler_SecurityPackageCreationException;
	}

	public void setDefaultHandler_SecurityPackageCreationException(
			ErrorHandler defaultHandler_SecurityPackageCreationException) {
		this.defaultHandler_SecurityPackageCreationException = defaultHandler_SecurityPackageCreationException;
	}
}
