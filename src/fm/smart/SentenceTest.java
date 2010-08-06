package fm.smart;

import junit.framework.TestCase;

public class SentenceTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	public void testCreateSentence(){
		Sentence sentence = Sentence.createSentence("688953");
		assertNotNull(sentence.text);
		assertNotNull(sentence.id);
		assertNotNull(sentence.translation);
		assertNotNull(sentence.sound_url);
		assertNotNull(sentence.translation_sound_url);
	}
	
	public void testCreateSentenceWithNoTranslationSound(){
		Sentence sentence = Sentence.createSentence("249599");
		assertNotNull(sentence.text);
		assertNotNull(sentence.id);
		assertNotNull(sentence.translation);
		assertNotNull(sentence.sound_url);
		assertNull(sentence.translation_sound_url);
	}
	
	public void testCreateSentenceWithNoSound(){
		Sentence sentence = Sentence.createSentence("326647");
		assertNotNull(sentence.text);
		assertNotNull(sentence.id);
		assertNotNull(sentence.translation);
		assertNull(sentence.sound_url);
		assertNotNull(sentence.translation_sound_url);
	}
	
	
	
	

}
