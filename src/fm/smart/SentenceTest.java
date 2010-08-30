package fm.smart;

import junit.framework.TestCase;

public class SentenceTest extends TestCase {
	Lookup lookup = null;
	Transport transport = null;

	protected void setUp() throws Exception {
		super.setUp();
		lookup = LookupTest.getTestLookup();
		transport = new LookupTestTransport();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateSentence() {
		try {
			Sentence sentence = Sentence.createSentence(transport, "688953",
					lookup);
			assertNotNull(sentence.text);
			assertNotNull(sentence.id);
			assertNotNull(sentence.translation);
			assertNotNull(sentence.sound_url);
			assertNotNull(sentence.translation_sound_url);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testParseTranslationsWithNoTranslations() {
		String empty = "{sentences: [ ]}";
		try {
			Sentence.parseTranslations(transport, lookup, new Sentence(), empty);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testCreateSentenceWithNoTranslationSound() {
		try {
			Sentence sentence = Sentence.createSentence(transport, "249599",
					lookup);
			assertNotNull(sentence.text);
			assertNotNull(sentence.id);
			assertNotNull(sentence.translation);
			assertNotNull(sentence.sound_url);
			assertNull(sentence.translation_sound_url);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

	public void testCreateSentenceWithNoSound() {
		try {
			Sentence sentence = Sentence.createSentence(transport, "326647",
					lookup);
			assertNotNull(sentence.text);
			assertNotNull(sentence.id);
			assertNotNull(sentence.translation);
			assertNull(sentence.sound_url);
			assertNotNull(sentence.translation_sound_url);
		} catch (Exception e) {
			e.printStackTrace();
			fail();
		}
	}

}
