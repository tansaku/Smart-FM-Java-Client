package fm.smart;

public class Result {
	public int status_code;
	public String http_response;

	public Result(int status_code, String http_response) {
		this.status_code = status_code;
		this.http_response = http_response; 
	}

	public String getTitle() {
		return null;
	}

	public String getMessage() {
		return null;
	}
}
