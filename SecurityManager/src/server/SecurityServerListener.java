package server;

import data.DefaultSecurityExceptionHandler;
import data.events.ClientConnectionValidationEvent;
import data.events.ClientConnectionValidationEventListener;
import data.events.ClientLogoutEvent;
import data.events.ClientLogoutEventListener;
import data.exceptions.LoginSecurityException;
import data.exceptions.LogoutSecurityException;

public class SecurityServerListener implements ClientLogoutEventListener, ClientConnectionValidationEventListener{
	
	private ServerSecurityManager serverSecurityManager;

	public SecurityServerListener(ServerUserManager serverUserManager,	ServerSecurityManager serverSecurityManager) {
		this.serverSecurityManager = serverSecurityManager;
		
		serverUserManager.getUserEventManager().registerClientConnectionValidationEventListener(this, 1);
		serverUserManager.getUserEventManager().registerClientLogoutEventListener(this, 1);
	}

	@Override
	public void clientLogout(ClientLogoutEvent event) {
		try {
			serverSecurityManager.logout(event.getValidatedUser());
		} catch (LogoutSecurityException e) {
			DefaultSecurityExceptionHandler.getDefaultSecurityExceptionHandler().getDefaultHandler_LogoutSecurityException().handleError(e);
		}
	}

	@Override
	public void handleClientLogginIn(ClientConnectionValidationEvent event) {		
		if(event.isLoggedIn()){
			try {
				serverSecurityManager.login((ValidatedUser) event.getUser());
			} catch (LoginSecurityException e) {
				DefaultSecurityExceptionHandler.getDefaultSecurityExceptionHandler().getDefaultHandler_LoginSecurityException().handleError(e);
			}
		}
	}

}
