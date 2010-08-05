package fm.smart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Vector;

import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class Lookup extends XMLHandler {

	private static final String SMARTFM_API_HTTP_ROOT = "http://api.smart.fm";
	private String search_lang = "ja";
	private String result_lang = "en";

	public JSONObject searchItemsJson(String keyword, int page, String version) {
		String api_call = "/items/matching/" + keyword + ".json?v=" + version
				+ "&page=" + page + "&include_sentences=true&language="
				+ search_lang + "&translation_language=" + result_lang;
		return jsonCall(api_call);
	}

	public JSONObject item(String id) {
		String api_call = "/items/" + id + ".json?v=2&include_sentences=true";
		return jsonCall(api_call);
	}

	public JSONObject sentence(String id) {
		String api_call = "/sentences/" + id + ".json?v=2";
		return jsonCall(api_call);
	}

	public JSONObject userGoals(String user) {
		String api_call = "/users/" + user + "/goals/creator.json?v=2";
		return jsonCall(api_call);
	}

	public JSONObject userStudyGoals(String user) {
		String api_call = "/users/" + user + "/goals.json?v=2";
		return jsonCall(api_call);
	}

	public JSONObject recentGoals() {
		String api_call = "/goals.json";
		return jsonCall(api_call);
	}

	public JSONObject goalItems(String list_id) {
		String api_call = "/goals/" + list_id
				+ "/items.json?v=2&include_sentences=true";
		return jsonCall(api_call);
	}

	public JSONObject searchGoals(String keyword) {
		keyword = keyword.replaceAll(" ", "%20");
		String api_call = "/goals/matching/" + keyword + ".json?v=2";
		return jsonCall(api_call);
	}

	private JSONObject jsonCall(String api_call) {
		URL url = null;
		try {
			url = new URL(SMARTFM_API_HTTP_ROOT + api_call);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String result = null;
		try {
			result = convertStreamToString(url.openStream());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json;
	}

	private static String convertStreamToString(InputStream is) {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
		 */
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	public HashMap<String, Vector<Node>> searchItems(String keyword, int page,
			String version) {
		if (version == null) {
			version = "1";
		}
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
			keyword = keyword.replaceAll("\\++", "%20");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String api_call = "/items/matching/" + keyword + ".xml?v=" + version
				+ "&page=" + page + "&include_sentences=true&language="
				+ search_lang + "&translation_language=" + result_lang;
		parseURL(api_call);
		return this.index;
	}

	private void parseURL(String api_call) throws FactoryConfigurationError {
		try {
			/* Create a URL we want to load some xml-data from. */

			URL url = new URL(SMARTFM_API_HTTP_ROOT + api_call);

			SAXParserFactory spf = SAXParserFactory.newInstance();
			SAXParser sp = spf.newSAXParser();

			/* Get the XMLReader of the SAXParser we created. */
			XMLReader xr = sp.getXMLReader();
			/* Create a new ContentHandler and apply it to the XML-Reader */

			xr.setContentHandler(this);

			/* Parse the xml-data from our URL. */
			InputSource is = new InputSource(url.openStream());
			// is.setEncoding("UTF8");
			xr.parse(is);
			/* Parsing has finished. */

		} catch (Exception e) {
			// Log any Error to the GUI.
			e.printStackTrace();
		}
	}
}
