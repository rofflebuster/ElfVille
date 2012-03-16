package elfville.server;

import java.util.Date;

import javax.crypto.SecretKey;

import elfville.server.model.*;

public class CurrentUserProfile {
	
	private final static long timeoutLength = 1000 * 60 * 20;

	private static Database database = Database.getInstance();

	private SecretKey key;
	private int nonce;
	private Date timeout;
	private Date lastLogin;
	
	private int currentUserId;

	public CurrentUserProfile() {
		currentUserId = -1;
	}

	//TODO: fix this shit, it broke as hell
	public boolean loggedIn() {
		return currentUserId != -1;
	}

	//TODO: fix this shit, it broke as hell
	public boolean loggedOut() {
		return !loggedIn();
	}

	public int getCurrentUserId() {
		return currentUserId;
	}

	public void setCurrentUserId(int currentUserId) {
		this.currentUserId = currentUserId;
	}

	public User getUser() {
		return Database.getInstance().userDB.findUserByModelID(currentUserId);
	}

	public Elf getElf() {
		return getUser().getElf();
	}
	
	public void setSharedKey(SecretKey key){
		this.key= key;
	}
	
	public SecretKey getSecretKey(){
		return key;
	}
	
	public void setNonce(int nonce){
		this.nonce= nonce;
	}
	
	public int getNonce(){
		return nonce;
	}
	
	public void setLastLogin(Date d){
		lastLogin= d;
	}
	
	public boolean checkLogin(Date d){
		return lastLogin.before(d);
	}
	
	public void setTimeout(){
		Date d = new Date();
		timeout.setTime(d.getTime() + timeoutLength);
	}
	
	/**public boolean checkTimeOut(){
		Date d= new Date(); 
		//check to see if this user's session has timed out.  if it has he should be logged out automatically
		if(d.after(timeout)){
			logOut();
			return true;
		}
		//if the user hasn't timed out, increase the timeout length.
		setTimeout();
		return false;
	}**/
	
	public void logOut(){
		this.currentUserId = -1;
	}

}
