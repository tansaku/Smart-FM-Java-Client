package fm.smart;

import java.net.UnknownHostException;

public abstract class Transport {

	public Result getAuthenticatedResponse(String url, String username,
			String password) throws UnknownHostException {
		return authenticatedResponse(url, "GET", username, password);
	}

	public Result deleteAuthenticatedResponse(String url, String username,
			String password) throws UnknownHostException {
		return authenticatedResponse(url, "DELETE", username, password);
	}

	public Result postAuthenticatedResponseWithBody(String url, String body,
			String username, String password) {
		return authenticatedResponseWithBody(url, "POST", body, username,
				password);
	}

	public abstract Result authenticatedResponse(String url, String method,
			String username, String password) throws UnknownHostException;

	public abstract Result authenticatedResponseWithBody(String url,
			String method, String body, String username, String password);

}
