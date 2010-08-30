package fm.smart;

import junit.framework.TestCase;

import org.json.JSONArray;
import org.json.JSONObject;

public class LookupTest extends TestCase {

	Transport transport = null;
	Lookup lookup = null;

	protected void setUp() throws Exception {
		super.setUp();
		transport = new LookupTestTransport();
		lookup = getTestLookup();
	}

	static Lookup getTestLookup() {
		return new Lookup(API_KEY, username, password);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public static String search_lang = "ja";
	public static String result_lang = "en";

	// TODO need to ensure the following isn't checked into github
	// should read them from text file for which I check in empty version or
	// similar ...

	public static String username = "tansaku";
	public static String password = "samjoseph";

	public static String superusername = "androidapp";
	public static String superpassword = "samjoseph";

	public static final String API_KEY = "7pvmc285fxnexgwhbfddzkjn";

	public void testLogin() {

		try {
			Result result = lookup.login(transport, username, password);
			JSONObject json = new JSONObject(result.http_response);
			assertNotNull(json);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testSignup() {

		try {
			Result result = lookup.signup(transport,
					"smartfmv2javaclienttestuser", "testpassword",
					"testpassword",
					"test@smartfmv2javaclienttestuser.notarealemail.com",
					superusername, superpassword);
			JSONObject json = new JSONObject(result.http_response);
			assertNotNull(json);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// remove user - functionality not present at least in this form ...

		// try {
		// Result result = lookup.deleteUser(transport,
		// "smartfmv2javaclienttestuser");
		// JSONObject json = new JSONObject(result.http_response);
		// assertNotNull(json);
		// } catch (Exception e) {
		// e.printStackTrace();
		// fail();
		// }
	}

	// successful response 201
	// {"birthday":null,"description":null,"name":"smartfmv2javaclienttestuser","uri":"http://smart.fm/users/smartfmv2javaclienttestuser","auth_token":"d82e5c9a2d93e32f424f68c6cf573ea3","gender":null,"foaf":"http://api.smart.fm/users/smartfmv2javaclienttestuser.rdf","blog":"http://smart.fm/users/smartfmv2javaclienttestuser/journal","id":"smartfmv2javaclienttestuser","study_data":{"completed":0,"studied":0}}

	public void testAddItemToGoal() {
		try {
			Result result = lookup.addItemToGoal(transport, "279542", "375219",
					null);
			JSONObject json = new JSONObject(result.http_response);
			JSONObject cue = json.getJSONObject("cue");
			String cue_text = cue.getJSONObject("content").getString("text");
			assertNotNull(cue_text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// have to remove afterwards
		try {
			Result result = lookup.deleteItemFromGoal(transport, "279542",
					"375219");
			JSONObject json = new JSONObject(result.http_response);
			JSONObject cue = json.getJSONObject("cue");
			String cue_text = cue.getJSONObject("content").getString("text");
			assertNotNull(cue_text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testCreateItem() {
		String cue = "‚Ä‚¢‚¶‚á‚­";
		String response = "submerged pains";
		String id = null;

		try {
			Result result = lookup.createItem(transport, cue, search_lang, cue,
					Utils.POS_MAP.get("Noun"), response, result_lang, response,
					"279542");
			JSONObject json = new JSONObject(result.http_response);
			JSONObject cue_json = json.getJSONObject("cue");
			String cue_text = cue_json.getJSONObject("content").getString(
					"text");
			id = json.getString("id");
			assertNotNull(cue_text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		// have to remove afterwards
		try {
			Result result = lookup.deleteItemFromGoal(transport, "279542", id);
			JSONObject json = new JSONObject(result.http_response);
			JSONObject cue_json = json.getJSONObject("cue");
			String cue_text = cue_json.getJSONObject("content").getString(
					"text");
			assertNotNull(cue_text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testCreateExample() {
		String sentence = "”Þ‚Í–¬”‚ª‚¨‚»‚¢";
		String sentence_transliteration = "‚©‚ê‚Í‚Ý‚á‚­‚Í‚­‚ª‚¨‚»‚¢";
		String translation = "His pulse is slow.";
		String sentence_id = null;
		String translation_id = null;
		// required or we get this error
		// {"error":{"message":"Sentence can only be added to an item already in a goal.","code":400}}
		lookup.addItemToGoal(transport, "279542", "369658", null);

		// have to add translation separately --> BOGUS!

		try {
			Result result = lookup.createExample(transport, translation,
					result_lang, translation, null, null, null);
			JSONObject json = new JSONObject(result.http_response);
			String text = json.getString("text");
			translation_id = json.getString("id");
			assertNotNull(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
		try {
			Result result = lookup.createExample(transport, sentence,
					search_lang, sentence_transliteration, translation_id,
					"369658", "279542");
			JSONObject json = new JSONObject(result.http_response);
			String text = json.getString("text");
			sentence_id = json.getString("id");
			assertNotNull(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// try {
		// Result result = lookup.createExampleTranslation(transport,
		// sentence_id, translation_id);
		// JSONObject json = new JSONObject(result.http_response);
		// String text = json.getString("text");
		// sentence_id = json.getString("id");
		// assertNotNull(text);
		// } catch (Exception e) {
		// e.printStackTrace();
		// fail();
		// }

		// have to remove afterwards
		try {
			Result result = lookup.deleteExample(transport, "279542", "369658",
					sentence_id);
			JSONObject json = new JSONObject(result.http_response);
			String status = json.getString("status");
			assertEquals(status, "202");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		try {
			Result result = lookup.deleteExample(transport, "279542", "369658",
					translation_id);
			JSONObject json = new JSONObject(result.http_response);
			String status = json.getString("status");
			assertEquals(status, "202");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testAddSentenceToGoal() {
		String goal_id = "279542";
		String item_id = "369658";
		String sentence_id = "1054007";
		// required or we get this error
		// {"error":{"message":"Sentence can only be added to an item already in a goal.","code":400}}
		lookup.addItemToGoal(transport, goal_id, item_id, null);
		try {
			Result result = lookup.addSentenceToGoal(transport, goal_id,
					item_id, sentence_id, null);
			JSONObject json = new JSONObject(result.http_response);
			String text = json.getString("text");
			sentence_id = json.getString("id");
			assertNotNull(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

		// have to remove afterwards
		try {
			Result result = lookup.deleteExample(transport, goal_id, item_id,
					sentence_id);
			JSONObject json = new JSONObject(result.http_response);
			String status = json.getString("status");
			assertEquals(status, "202");
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}

	}

	public void testSearchItems() {
		try {
			Result result = lookup.searchItems(transport, "orange", 1,
					search_lang, result_lang);
			JSONObject json = new JSONObject(result.http_response);

			JSONArray items = json.getJSONArray("items");
			assertNotNull(items);
			int number_results = json.getInt("totalResults");
			assertTrue(number_results > 0);

			int start_index = json.getInt("startIndex");
			assertTrue(start_index == 0);
			int items_per_page = json.getInt("itemsPerPage");
			assertTrue(items_per_page > 0);

			String cue_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("cue")
					.getJSONObject("related").getString("language"));
			assertEquals(cue_language, "Japanese");
			String response_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("response")
					.getJSONObject("related").getString("language"));
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

		} catch (Exception e) {
			fail();
			e.printStackTrace();
		}
	}

	public void testSearchGoals() {
		try {
			Result result = lookup.searchGoals(transport, "japanese");
			JSONObject json = new JSONObject(result.http_response);
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testItem() {
		try {
			Result result = lookup.item(transport, "375219", search_lang,
					result_lang);
			JSONObject json = new JSONObject(result.http_response);
			JSONObject cue = json.getJSONObject("cue");
			String cue_text = cue.getJSONObject("content").getString("text");
			assertNotNull(cue_text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testItemSounds() {
		try {
			Result result = lookup.itemSounds(transport, "375219");
			JSONObject json = new JSONObject(result.http_response);
			String url = json.getJSONArray("sounds").getJSONObject(0)
					.getString("url");
			assertNotNull(url);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testSentence() {
		try {
			Result result = lookup.sentence(transport, "688953");
			JSONObject json = new JSONObject(result.http_response);
			String text = json.getString("text");
			assertNotNull(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testSentenceSounds() {
		try {
			Result result = lookup.sentenceSounds(transport, "688953");
			JSONObject json = new JSONObject(result.http_response);
			String url = json.getJSONArray("sounds").getJSONObject(0)
					.getString("url");
			assertNotNull(url);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testSentenceImages() {
		try {
			Result result = lookup.sentenceImages(transport, "688953");
			JSONObject json = new JSONObject(result.http_response);
			String url = json.getJSONArray("images").getJSONObject(1)
					.getString("url");
			assertNotNull(url);

		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testSentenceTranslations() {
		try {
			Result result = lookup.sentenceTranslations(transport, "688953");
			JSONObject json = new JSONObject(result.http_response);
			String text = json.getJSONArray("sentences").getJSONObject(0)
					.getString("text");
			assertNotNull(text);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testUserGoals() {
		try {
			Result result = lookup.userGoals(transport, "tansaku");
			JSONObject json = new JSONObject(result.http_response);
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testUserStudyGoals() {
		try {
			Result result = lookup.userStudyGoals(transport, "tansaku");
			JSONObject json = new JSONObject(result.http_response);
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testRecentGoals() {
		try {
			Result result = lookup.recentGoals(transport);
			JSONObject json = new JSONObject(result.http_response);
			JSONArray goals = json.getJSONArray("goals");
			String description = goals.getJSONObject(1)
					.getString("description");
			assertNotNull(description);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testGoalItems() {
		try {
			Result result = lookup.goalItems(transport, "57692", search_lang,
					result_lang);
			JSONObject json = new JSONObject(result.http_response);
			JSONArray items = json.getJSONArray("items");
			assertNotNull(items);

			String cue_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("cue")
					.getJSONObject("related").getString("language"));
			assertEquals(cue_language, "Japanese");
			String response_language = Utils.INV_LANGUAGE_MAP.get(items
					.getJSONObject(1).getJSONObject("response")
					.getJSONObject("related").getString("language"));
			assertEquals(response_language, "English");

			String cue = items.getJSONObject(1).getJSONObject("cue")
					.getJSONObject("content").getString("text");
			assertNotNull(cue);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	//
	// // interesting - running this outside of Android, seems like the
	// saxparser
	// // uses qName instead of localname
	// // and we end up needing openSearch: prefixes ...
	// public void testSearchItemsV2() {
	// HashMap<String, Vector<Node>> map = lookup
	// .searchItems("orange", 1, "1");
	//
	// Vector<Node> items = map.get("item");
	// assertNotNull(items);
	// int number_results = Integer.parseInt(map
	// .get("openSearch:totalResults").firstElement().contents);
	// assertTrue(number_results > 0);
	// int start_index = Integer.parseInt(map.get("openSearch:startIndex")
	// .firstElement().contents);
	// int items_per_page = Integer.parseInt(map
	// .get("openSearch:itemsPerPage").firstElement().contents);
	// String query_string = map.get("openSearch:Query").firstElement().atts
	// .get("searchTerms").toString();
	// String cue_language = Utils.INV_LANGUAGE_MAP.get(items.firstElement()
	// .get("cue").firstElement().atts.get("language").toString());
	// String response_language = Utils.INV_LANGUAGE_MAP.get(items
	// .firstElement().get("responses").firstElement().get("response")
	// .firstElement().atts.get("language").toString());
	// }

}
