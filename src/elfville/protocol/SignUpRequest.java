package elfville.protocol;

public class SignUpRequest extends Request {
	private static final long serialVersionUID = 1L;
	public String username;
	// public String password;
	public String description;

	public SignUpRequest(String u) {
		username = u;
	}
}
