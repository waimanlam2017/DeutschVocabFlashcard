package me.rayentwickler.flashcard;

public class HandlerReturnValue {
	private Boolean hasReturnValue;
	private GermanWord word;
	
	public HandlerReturnValue() {
		super();
		hasReturnValue = false;
	}

	public Boolean getHasReturnValue() {
		return hasReturnValue;
	}

	public void setHasReturnValue(Boolean hasReturnValue) {
		this.hasReturnValue = hasReturnValue;
	}

	public GermanWord getWord() {
		return word;
	}

	public void setWord(GermanWord word) {
		this.word = word;
	}
	
}
