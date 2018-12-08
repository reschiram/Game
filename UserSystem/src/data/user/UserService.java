package data.user;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import data.DeEnCode;

public class UserService {
	
	public final static String StringFormat = "ISO_8859_1";
	
	protected DeEnCode deEnCode; 
	
	public UserService(){
		this.deEnCode = new DeEnCode();
	}
	
	protected String getUserLoginInfo(User user, BigInteger mult){		
		try{
			String currentUsername = user.getUsername();
			while(currentUsername.length()<25)currentUsername+=new String(new byte[]{0}, StringFormat);
			currentUsername = new String(new BigInteger(currentUsername.getBytes(StringFormat)).multiply(mult).toByteArray(), StringFormat);
			while(currentUsername.length()<30)currentUsername+=new String(new byte[]{0}, StringFormat);
			
			String currentPassword = user.getPassword();				
			while(currentPassword.length()<25)currentPassword+=new String(new byte[]{0}, StringFormat);
			currentPassword = new String(new BigInteger(currentPassword.getBytes(StringFormat)).multiply(mult).toByteArray(), StringFormat);
			while(currentPassword.length()<30)currentPassword+=new String(new byte[]{0}, StringFormat);
			
			String currentLogin = currentUsername+currentPassword;	
			return currentLogin;
	
		}catch (UnsupportedEncodingException e) {
			System.out.println("A fatal error has occoured while trying to confirm user: "+user.getUsername());
		}
		
		return "";
	}
	
	protected BigInteger getModForLoginInfo(){
		int mod = (int) (((long)(System.currentTimeMillis()/(100*1000))));
		BigInteger mult = new BigInteger(mod+"");
		return mult;
	}

}
