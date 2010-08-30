package fm.smart;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

public class LookupTestTransport extends Transport {

	public Result authenticatedResponse(String url_string, String method,
			String username, String password) {
		URL url = null;
		String http_response = "";
		InputStream stream = null;
		int status_code = -1;
		try {

			url = new URL(Lookup.SMARTFM_API_HTTP_ROOT + url_string);
			HttpURLConnection connect = (HttpURLConnection) url
					.openConnection();
			connect.setRequestMethod(method);

			if (!Utils.isEmpty(username) && !Utils.isEmpty(password)) {
				String auth = username + ":" + password;
				byte[] bytes = auth.getBytes();

				connect.setRequestProperty("Authorization", "Basic "
						+ new String(Base64.encodeBase64(bytes)));
			}
			connect.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connect.setRequestProperty("Host", "api.smart.fm");

			Map<?, ?> array = connect.getHeaderFields();
			System.out.println(array.toString());

			stream = connect.getInputStream();
			http_response = Lookup.convertStreamToString(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					/* Reset to Default image on any error. */
					e.printStackTrace();
				}
			}
		}
		return new Result(status_code, http_response);
	}

	public Result authenticatedResponseWithBody(String url_string,
			String method, String body, String username, String password) {
		URL url = null;
		String http_response = "";
		InputStream stream = null;
		int status_code = -1;
		try {

			url = new URL(Lookup.SMARTFM_API_HTTP_ROOT + url_string);

			String auth = username + ":" + password;
			byte[] bytes = auth.getBytes();

			HttpURLConnection connect = (HttpURLConnection) url
					.openConnection();
			connect.setRequestMethod(method);
			connect.setDoOutput(true); // want to send
			connect.setRequestProperty("Authorization", "Basic "
					+ new String(Base64.encodeBase64(bytes)));
			connect.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");

			connect.setRequestProperty("Host", "api.smart.fm");
			OutputStream ost = connect.getOutputStream();
			PrintWriter pw = new PrintWriter(ost);
			pw.print(body); // here we "send" our body!
			pw.flush();
			pw.close();

			Map<?, ?> array = connect.getHeaderFields();
			System.out.println(array.toString());

			stream = connect.getInputStream();
			http_response = Lookup.convertStreamToString(stream);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {

			if (stream != null) {
				try {
					stream.close();
				} catch (IOException e) {
					/* Reset to Default image on any error. */
					e.printStackTrace();
				}
			}
		}
		return new Result(status_code, http_response);
	}

}
