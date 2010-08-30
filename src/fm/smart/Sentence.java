package fm.smart;

import java.net.UnknownHostException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sentence {
	public String id = null;
	public String text = null;
	public String image_url = null;
	public String sound_url = null;
	public String translation = null;
	public String translation_sound_url = null;

	public static Sentence createSentence(Transport transport, String id,
			Lookup lookup) throws UnknownHostException {
		Sentence sentence = null;
		try {
			sentence = new Sentence();
			sentence.id = id;

			JSONObject json = new JSONObject(lookup.sentenceSounds(transport,
					id).http_response);
			JSONArray array = json.getJSONArray("sounds");
			if (array.length() > 0) {
				JSONObject sound = array.getJSONObject(0);
				sentence.sound_url = sound.getString("url");
			}
			json = new JSONObject(lookup.sentence(transport, id).http_response);
			sentence.text = json.getString("text");

			String translations = lookup.sentenceTranslations(transport, id).http_response;
			parseTranslations(transport, lookup, sentence, translations);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sentence;
	}

	public static void parseTranslations(Transport transport, Lookup lookup,
			Sentence sentence, String translations) throws JSONException,
			UnknownHostException {
		JSONObject json;
		JSONArray array;
		json = new JSONObject(translations);
		array = json.getJSONArray("sentences");
		if (array.length() > 0) {
			JSONObject translation = array.getJSONObject(0);
			sentence.translation = translation.getString("text");
			String translation_id = translation.getString("id");

			json = new JSONObject(lookup.sentenceSounds(transport,
					translation_id).http_response);
			array = json.getJSONArray("sounds");
			if (array.length() > 0) {
				sentence.translation_sound_url = array.getJSONObject(0)
						.getString("url");
			}

			json = new JSONObject(lookup.sentenceImages(transport,
					translation_id).http_response);
			array = json.getJSONArray("images");
			if (array.length() > 0) {
				sentence.image_url = array.getJSONObject(0).getString("url");
			}
		}
	}

}
