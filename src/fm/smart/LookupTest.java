package fm.smart;

import java.util.HashMap;
import java.util.Vector;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LookupTest extends TestCase {

	private Lookup lookup;

	protected void setUp() throws Exception {
		this.lookup = new Lookup();
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testItem() {
		try {
			JSONObject json = lookup.item("375219");
			JSONObject cue = json.getJSONObject("cue");
			String cue_text = cue.getJSONObject("content").getString("text");
			assertNotNull(cue_text);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testSentence() {
		try {
			JSONObject json = lookup.sentence("688953");
			String text = json.getString("text");
			assertNotNull(text);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testUserGoals() {
		try {
			JSONObject json = lookup.userGoals("tansaku");
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testUserStudyGoals() {
		try {
			JSONObject json = lookup.userStudyGoals("tansaku");
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testRecentGoals() {
		try {
			JSONObject json = lookup.recentGoals();
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGoalItems() {
		try {
			JSONObject json = lookup.goalItems("57692");
			JSONArray items = json.getJSONArray("items");
			assertNotNull(items);

			String cue_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("cue").getJSONObject(
							"related").getString("language"));
			assertEquals(cue_language, "Japanese");
			String response_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("response").getJSONObject(
							"related").getString("language"));
			assertEquals(response_language, "English");

			String cue = items.getJSONObject(1).getJSONObject("cue")
					.getJSONObject("content").getString("text");
			assertNotNull(cue);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testSearchGoals() {
		try {
			JSONObject json = lookup.searchGoals("test");
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testSearchItemsV2Json() {
		try {
			JSONObject json = lookup.searchItemsJson("orange", 1, "2");

			JSONArray items = json.getJSONArray("items");
			assertNotNull(items);
			int number_results = json.getInt("totalResults");
			assertTrue(number_results > 0);

			int start_index = json.getInt("startIndex");
			assertTrue(start_index == 0);
			int items_per_page = json.getInt("itemsPerPage");
			assertTrue(items_per_page > 0);

			String cue_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("cue").getJSONObject(
							"related").getString("language"));
			assertEquals(cue_language, "Japanese");
			String response_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("response").getJSONObject(
							"related").getString("language"));
			assertEquals(response_language, "English");

			String cue = items.getJSONObject(1).getJSONObject("cue")
					.getJSONObject("content").getString("text");
			assertNotNull(cue);

			// so java class structure could be created to store all the data,
			// and we could test that ...
			// or we could just be passing back JSON array, as I am doing with
			// the dynamically constructed XML nodes
			// that latter would require fewer changes, but some java class
			// hierarchy would probably be better if
			// things were all being persisted in an SQL backend - of course we
			// could just be persisting json flat files

			// advantage of SQL backend would be local search, but can't see
			// myself using that. Storing json flat files
			// would be like having an client side internet cache - would allow
			// display of previous searches when offline
			// would be nice to be able to expire the cache appropriately

			// having java classes would mean more of the test library would be
			// re-used in the actual android app, and would
			// still work with json flat files ... either way should fork
			// project to 1.1 for json upgrade ...

			// BE CAREFUL before testing all the different calls in here we
			// should check that they work within Android framework
			// and with built in Android JSON library

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// interesting - running this outside of Android, seems like the saxparser
	// uses qName instead of localname
	// and we end up needing openSearch: prefixes ...
	public void testSearchItemsV2() {
		HashMap<String, Vector<Node>> map = lookup
				.searchItems("orange", 1, "1");

		Vector<Node> items = map.get("item");
		assertNotNull(items);
		int number_results = Integer.parseInt(map
				.get("openSearch:totalResults").firstElement().contents);
		assertTrue(number_results > 0);
		int start_index = Integer.parseInt(map.get("openSearch:startIndex")
				.firstElement().contents);
		int items_per_page = Integer.parseInt(map
				.get("openSearch:itemsPerPage").firstElement().contents);
		String query_string = map.get("openSearch:Query").firstElement().atts
				.get("searchTerms").toString();
		String cue_language = Utils.INV_LANGUAGE_MAP.get(items.firstElement()
				.get("cue").firstElement().atts.get("language").toString());
		String response_language = Utils.INV_LANGUAGE_MAP.get(items
				.firstElement().get("responses").firstElement().get("response")
				.firstElement().atts.get("language").toString());
	}

}
