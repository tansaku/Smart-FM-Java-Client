package fm.smart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.net.UnknownHostException;

import org.json.JSONObject;

public class Lookup {
	// http://groups.google.com/group/android-developers/browse_thread/thread/e9f0eb7f8ac51846?hide_quotes=no
	// talks of DNS resolution on emulator ...
	// private final String SMARTFM_API_HTTP_ROOT = "http://65.74.140.5";
	public final static String SMARTFM_API_HTTP_ROOT = "http://api.smart.fm";
	private String api_key;
	public String username;
	public String password;

	// http://groups.google.com/group/android-developers/browse_thread/thread/b54b6321850b56eb
	// more serious issue PREFER HTTPCLIENT!!!!!!!!!!!!!!!!!!!!!

	public Result login(Transport transport, String username, String password)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(loginURL(), username,
				password);
	}

	public String loginURL() {
		return "/users.json?v=2";
	}

	public Lookup(String api_key, String username, String password) {
		this.api_key = api_key;
		this.username = username;
		this.password = password;
	}

	public Result signup(Transport transport, String username, String password,
			String password_confirmation, String email, String superuser,
			String superpassword) {
		String body;
		try {
			body = "user[username]=" + URLEncoder.encode(username, "UTF-8")
					+ "&user[password]=" + password + "&user[email]=" + email
					+ "&user[password_confirmation]=" + password_confirmation
					+ "&api_key=" + api_key;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}

		return transport.postAuthenticatedResponseWithBody(signupURL(), body,
				superuser, superpassword);
	}

	public String signupURL() {
		return "/users.json?v=2";
	}

	public Result deleteUser(Transport transport, String username,
			String superusername, String superpassword)
			throws UnknownHostException {
		return transport.deleteAuthenticatedResponse(deleteUserURL(username),
				superusername, superpassword);
	}

	public String deleteUserURL(String username) {
		return "/users/" + username + "?v=2&api_key=" + api_key;
	}

	public Result createItem(Transport transport, String cue,
			String cue_language, String character_cue_text,
			String part_of_speech, String response, String response_language,
			String character_response_text, String goal_id)
			throws UnsupportedEncodingException {

		String body = "v=2&cue[text]=" + URLEncoder.encode(cue, "UTF-8")
				+ "&cue[part_of_speech]=" + part_of_speech + "&cue[language]="
				+ cue_language + "&response[text]="
				+ URLEncoder.encode(response, "UTF-8") + "&response[language]="
				+ response_language + "&api_key=" + api_key;

		if (character_response_text != null
				&& !character_response_text.equals("")) {
			body += "&response[transliteration]="
					+ URLEncoder.encode(character_response_text, "UTF-8");
		}
		if (character_cue_text != null && !character_cue_text.equals("")) {
			body += "&cue[transliteration]="
					+ URLEncoder.encode(character_cue_text, "UTF-8");
		}

		return transport.postAuthenticatedResponseWithBody(
				createItemURL(goal_id), body, this.username, this.password);
	}

	public String createItemURL(String goal_id) {
		return "/goals/" + goal_id + "/items.json";
	}

	public Result deleteItem(Transport transport, String goal_id, String item_id)
			throws UnknownHostException {
		return transport.deleteAuthenticatedResponse(deleteItemURL(item_id),
				this.username, this.password);
	}

	public String deleteItemURL(String id) {
		return "/items/" + id + ".json?v=2";
	}

	// TODO transliteration types?
	public Result createExample(Transport transport, String sentence,
			String sentence_language, String sentence_transliteration,
			String translation_id, String item_id, String goal_id)
			throws UnsupportedEncodingException {

		String body = "v=2&sentence[text]="
				+ URLEncoder.encode(sentence, "UTF-8") + "&sentence[language]="
				+ sentence_language + "&api_key=" + api_key;

		if (!Utils.isEmpty(translation_id)) {
			body += "&translation_id=" + translation_id;
		}

		if (!Utils.isEmpty(sentence_transliteration)) {
			body += "&sentence[transliteration]="
					+ URLEncoder.encode(sentence_transliteration, "UTF-8");
		}

		return transport.postAuthenticatedResponseWithBody(
				createExampleURL(goal_id, item_id), body, this.username,
				this.password);
	}

	public String createExampleURL(String goal_id, String item_id) {
		String url = "";
		if (!Utils.isEmpty(goal_id)) {
			url += "/goals/" + goal_id;
		}
		if (!Utils.isEmpty(item_id)) {
			url += "/items/" + item_id;
		}
		return url + "/sentences.json";
	}

	public Result createExampleTranslation(Transport transport,
			String example_id, String translation_id)
			throws UnsupportedEncodingException {

		String body = "v=2&translation_id=" + translation_id + "&api_key="
				+ api_key;

		return transport.postAuthenticatedResponseWithBody(
				createExampleTranslationURL(example_id), body, this.username,
				this.password);
	}

	public String createExampleTranslationURL(String sentence_id) {
		return "/sentences/" + sentence_id + "/translations.json";
	}

	public Result deleteExample(Transport transport, String goal_id,
			String item_id, String sentence_id) throws UnknownHostException {
		return transport.deleteAuthenticatedResponse(
				deleteExampleURL(goal_id, item_id, sentence_id), this.username,
				this.password);
	}

	public String deleteExampleURL(String goal_id, String item_id,
			String sentence_id) {
		return "/goals/" + goal_id + "/items/" + item_id + "/sentences/"
				+ item_id + ".json?v=2";
	}

	public Result addSentenceToGoal(Transport transport, String goal_id,
			String item_id, String sentence_id, String sound_id) {

		String body = "id=" + sentence_id + "&api_key=" + api_key;
		if (!Utils.isEmpty(sound_id)) {
			body += "&sound_id=" + sound_id;
		}
		return transport.postAuthenticatedResponseWithBody(
				addSentenceToGoalURL(goal_id, item_id, sentence_id, sound_id),
				body, this.username, this.password);
	}

	public String addSentenceToGoalURL(String goal_id, String item_id,
			String sentence_id, String sound_id) {
		return "/goals/" + goal_id + "/items/" + item_id
				+ "/sentences.json?v=2";
	}

	public Result addItemToGoal(Transport transport, String goal_id,
			String item_id, String sound_id) {

		String body = "id=" + item_id + "&api_key=" + api_key;
		if (!Utils.isEmpty(sound_id)) {
			body += "&sound_id=" + sound_id;
		}
		return transport.postAuthenticatedResponseWithBody(
				addItemToGoalURL(goal_id), body, this.username, this.password);
	}

	public String addItemToGoalURL(String goal_id) {
		return "/goals/" + goal_id + "/items.json?v=2";
	}

	public Result deleteItemFromGoal(Transport transport, String goal_id,
			String item_id) throws UnknownHostException {
		return transport.deleteAuthenticatedResponse(
				deleteItemFromGoalURL(goal_id, item_id), this.username,
				this.password);
	}

	public String deleteItemFromGoalURL(String goal_id, String item_id) {
		return "/goals/" + goal_id + "/items/" + item_id + ".json?v=2&api_key="
				+ api_key;
	}

	public Result searchItems(Transport transport, String keyword, int page,
			String search_lang, String result_lang) throws UnknownHostException {
		return transport.getAuthenticatedResponse(
				searchItemsURL(keyword, page, search_lang, result_lang),
				this.username, this.password);
	}

	public String searchItemsURL(String keyword, int page, String search_lang,
			String result_lang) {
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "/items/matching/" + keyword + ".json?v=2&page=" + page
				+ "&include_sentences=true&language=" + search_lang
				+ "&translation_language=" + result_lang + "&api_key="
				+ api_key;
	}

	public Result item(Transport transport, String id, String search_lang,
			String result_lang) throws UnknownHostException {
		return transport.getAuthenticatedResponse(
				itemURL(id, search_lang, result_lang), this.username,
				this.password);
	}

	public String itemURL(String id, String search_lang, String result_lang) {
		return "/items/" + id + ".json?v=2&include_sentences=true"
				+ "&include_sentences=true&language=" + search_lang
				+ "&translation_language=" + result_lang + "&api_key="
				+ api_key;
	}

	public Result itemSounds(Transport transport, String id)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(itemSoundsURL(id),
				this.username, this.password);
	}

	public String itemSoundsURL(String id) {
		return "/items/" + id + "/sounds.json?v=2&api_key=" + api_key;
	}

	public Result sentence(Transport transport, String id)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(sentenceURL(id),
				this.username, this.password);
	}

	public String sentenceURL(String id) {
		return "/sentences/" + id + ".json?v=2&api_key=" + api_key;
	}

	public Result sentenceSounds(Transport transport, String id)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(sentenceSoundsURL(id),
				this.username, this.password);
	}

	public String sentenceSoundsURL(String id) {
		return "/sentences/" + id + "/sounds.json?v=2&api_key=" + api_key;
	}

	public Result sentenceImages(Transport transport, String id)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(sentenceImagesURL(id),
				this.username, this.password);
	}

	public String sentenceImagesURL(String id) {
		return "/sentences/" + id + "/images.json?v=2&api_key=" + api_key;
	}

	public Result sentenceTranslations(Transport transport, String id)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(sentenceTranslationsURL(id),
				this.username, this.password);
	}

	public String sentenceTranslationsURL(String id) {
		return "/sentences/" + id + "/translations.json?v=2&api_key=" + api_key;
	}

	public Result userGoals(Transport transport, String user)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(userGoalsURL(user),
				this.username, this.password);
	}

	public String userGoalsURL(String user) {
		return "/users/" + user + "/goals/creator.json?v=2&api_key=" + api_key;
	}

	public Result userStudyGoals(Transport transport, String user)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(userStudyGoalsURL(user),
				this.username, this.password);
	}

	public String userStudyGoalsURL(String user) {
		return "/users/" + user + "/goals.json?v=2&api_key=" + api_key;
	}

	public Result recentGoals(Transport transport) throws UnknownHostException {
		return transport.getAuthenticatedResponse(recentGoalsURL(),
				this.username, this.password);
	}

	public String recentGoalsURL() {
		return "/goals.json?v=2&api_key=" + api_key;
	}

	public Result goalItems(Transport transport, String list_id,
			String search_lang, String result_lang) throws UnknownHostException {
		return transport.getAuthenticatedResponse(
				goalItemsURL(list_id, search_lang, result_lang), this.username,
				this.password);
	}

	public String goalItemsURL(String list_id, String search_lang,
			String result_lang) {
		return "/goals/" + list_id + "/items.json?v=2&include_sentences=true"
				+ "&include_sentences=true&language=" + search_lang
				+ "&translation_language=" + result_lang + "&api_key="
				+ api_key;
	}

	public Result searchGoals(Transport transport, String keyword)
			throws UnknownHostException {
		return transport.getAuthenticatedResponse(searchGoalsURL(keyword),
				this.username, this.password);
	}

	public String searchGoalsURL(String keyword) {
		keyword = keyword.replaceAll(" ", "%20");
		return "/goals/matching/" + keyword + ".json?v=2" + "&api_key="
				+ api_key;
	}

	public JSONObject jsonCall(String api_call) {

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
		} catch (Exception e) {
			System.err.println(url.toString());
			e.printStackTrace();
		}
		JSONObject json = null;
		try {
			json = new JSONObject(result);
		} catch (Exception e) {
			System.out
					.println("Internal JSON Exception, returning null. Error message was: "
							+ e.getMessage());
			return null;
		}

		return json;
	}

	public static String convertStreamToString(InputStream is) {
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

}
