package fm.smart;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sentence {
	public String id = null;
	public String text = null;
	public String image_url = null;
	// TODO android specific
	// public Bitmap image = null;
	public String sound_url = null;
	public String translation = null;
	public String translation_sound_url = null;

	public static Sentence createSentence(String id) {
		Sentence sentence = null;
		try {
			sentence = new Sentence();
			sentence.id = id;

			JSONObject json = Lookup.sentenceSounds(id);
			JSONArray array = json.getJSONArray("sounds");
			if (array.length() > 0) {
				JSONObject sound = array.getJSONObject(0);
				sentence.sound_url = sound.getString("url");
				sentence.text = sound.getString("text");
			}else{
				json = Lookup.sentence(id);
				sentence.text = json.getString("text");
			}

			json = Lookup.sentenceTranslations(id);
			JSONObject translation = json.getJSONArray("sentences")
					.getJSONObject(0);
			sentence.translation = translation.getString("text");
			String translation_id = translation.getString("id");

			json = Lookup.sentenceSounds(translation_id);
			array = json.getJSONArray("sounds");
			if (array.length() > 0) {
				sentence.translation_sound_url = array.getJSONObject(0)
						.getString("url");
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sentence;
	}

	public Sentence(String id, String text, String image_vector,
			String sound_vector, String translation,
			String translation_sound_vector) {
		this.id = id;
		this.text = text;
		if (image_vector != null) {
			try {
				this.image_url = image_vector;
			} catch (Exception e) {
				e.printStackTrace();
				loadSentenceImageDirectly();
			}
			// Log.d("DEBUG-URI", "image_url: " + this.image_url);

		}
		if (Utils.isEmpty(this.image_url)) {
			loadSentenceImageDirectly();
		}
		if (image_url != null) {
			// TODO removed temporarily not sure where this should go
			// image = Main.getRemoteImage(this.image_url, null);
		}
		// Log.d("DEBUG-URI", "image-url: " + this.image_url);

		if (sound_vector != null) {
			try {
				this.sound_url = sound_vector;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		this.translation = translation;
		// Log.d("DEBUG-SENTENCE", this.translation);
		if (Utils.isEmpty(this.translation)
				|| Utils.equals(this.translation, "no translation available")) {
			loadSentenceTranslationDirectly();
			// Log.d("DEBUG-SENTENCE", this.translation);
		}
		if (translation_sound_vector != null) {
			try {
				this.translation_sound_url = translation_sound_vector;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private Sentence() {
		// private factory shell
	}

	private void loadSentenceTranslationDirectly() {
		try {
			Lookup lookup = new Lookup();
			JSONObject sentence = lookup.sentence(this.id);

			this.translation = sentence.getJSONArray("transliterations")
					.getJSONObject(1).getString("text");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void loadSentenceImageDirectly() {
		try {
			Lookup lookup = new Lookup();
			JSONObject sentence = lookup.sentence(this.id);
			// TODO not sure what this should be now ...
			this.image_url = sentence.getString("square_image");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
