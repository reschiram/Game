package data.exceptions.handler;

import data.exceptions.CustomException;

public interface ErrorHandler {
	
	public void handleError(CustomException exception);

}
