package fm.smart;

import java.util.Vector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Item {

	public JSONObject item_node;
	public JSONObject cue_node;
	public JSONObject response_node;
	public JSONArray sentences_item;
	// public JSONArray sentences = null;
	public int number_groups = 1;
	public String[] groups;
	public String[][] children;
	public String cue_text;
	public String character;
	public CharSequence part_of_speech;
	public CharSequence author_name;
	public CharSequence type;
	public String author_icon_url;
	// public Bitmap author_image;
	public String cue_sound_url;
	public String response_sound_url;
	public Vector<Sentence> sentence_vector = new Vector<Sentence>();

	public String getId() {
		String id = null;
		try {
			id = item_node.getString("id");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return id;
	}

	public String getCueLanguage() {
		String language = null;
		try {
			language = cue_node.getJSONObject("related").getString("language");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return language;
	}

	public String getResponseLanguage() {
		String language = null;
		try {
			language = response_node.getJSONObject("related").getString(
					"language");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return language;
	}
}
