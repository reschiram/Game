package data.exceptions.handler;

import data.exceptions.CustomException;
import data.exceptions.UnsupportedPackageCreationException;
import data.exceptions.UnsupportedPackageException;
import data.exceptions.client.ServerNotFoundException;
import data.exceptions.server.InvalidServerClientIDException;
import data.exceptions.server.ServerPortException;

public class DefaultExceptionHandler {
	
	private static DefaultExceptionHandler defaultExceptionHandler;
	
	public static DefaultExceptionHandler getDefaultExceptionHandler(){
		if(defaultExceptionHandler == null) defaultExceptionHandler = new DefaultExceptionHandler();
		return defaultExceptionHandler;
	}
	
	private ErrorHandler defaultHandler_ServerNotFoundException;
	private ErrorHandler defaultHandler_UnsupportedPackageException;
	private ErrorHandler defaultHandler_UnsupportedPackageCreationException;
	private ErrorHandler defaultHandler_ServerPortException;
	private ErrorHandler defaultHandler_InvalidServerClientIDException;
	
	public DefaultExceptionHandler(){
		this.defaultHandler_ServerNotFoundException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof ServerNotFoundException){
					ServerNotFoundException snfe = (ServerNotFoundException)exception;
					System.out.println(snfe.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_UnsupportedPackageException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof UnsupportedPackageException){
					UnsupportedPackageException up = (UnsupportedPackageException)exception;
					System.out.println(up.getErrorMessage());
					if(up.getException() != null) up.getException().printStackTrace();
				}
			}
		};
		
		this.defaultHandler_UnsupportedPackageCreationException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof UnsupportedPackageCreationException){
					UnsupportedPackageCreationException upc = (UnsupportedPackageCreationException)exception;
					System.out.println(upc.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_ServerPortException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof ServerPortException){
					ServerPortException sp = (ServerPortException)exception;
					System.out.println(sp.getErrorMessage());
				}
			}
		};
		
		this.defaultHandler_InvalidServerClientIDException = new ErrorHandler() {			
			@Override
			public void handleError(CustomException exception) {
				if(exception instanceof InvalidServerClientIDException){
					InvalidServerClientIDException iscie = (InvalidServerClientIDException)exception;
					System.out.println(iscie.getErrorMessage());
				}
			}
		};
	}

	public ErrorHandler getDefaultHandler_ServerNotFoundException() {
		return defaultHandler_ServerNotFoundException;
	}

	public void setDefaultHandler_ServerNotFoundException(ErrorHandler defaultHandler_ServerNotFoundException) {
		this.defaultHandler_ServerNotFoundException = defaultHandler_ServerNotFoundException;
	}

	public ErrorHandler getDefaultHandler_UnsupportedPackageException() {
		return defaultHandler_UnsupportedPackageException;
	}

	public void setDefaultHandler_UnsupportedPackageException(ErrorHandler defaultHandler_UnsupportedPackageException) {
		this.defaultHandler_UnsupportedPackageException = defaultHandler_UnsupportedPackageException;
	}

	public ErrorHandler getDefaultHandler_ServerPortException() {
		return defaultHandler_ServerPortException;
	}

	public void setDefaultHandler_ServerPortException(ErrorHandler defaultHandler_ServerPortException) {
		this.defaultHandler_ServerPortException = defaultHandler_ServerPortException;
	}

	public ErrorHandler getDefaultHandler_UnsupportedPackageCreationException() {
		return defaultHandler_UnsupportedPackageCreationException;
	}

	public void setDefaultHandler_UnsupportedPackageCreationException(
			ErrorHandler defaultHandler_UnsupportedPackageCreationException) {
		this.defaultHandler_UnsupportedPackageCreationException = defaultHandler_UnsupportedPackageCreationException;
	}

	public ErrorHandler getDefaultHandler_InvalidServerClientIDException() {
		return defaultHandler_InvalidServerClientIDException;
	}

	public void setDefaultHandler_InvalidServerClientIDException(
			ErrorHandler defaultHandler_InvalidServerClientIDException) {
		this.defaultHandler_InvalidServerClientIDException = defaultHandler_InvalidServerClientIDException;
	}

}
