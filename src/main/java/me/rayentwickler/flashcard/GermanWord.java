package me.rayentwickler.flashcard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class GermanWord {
	private String word;
	private String pos;
	private String article;
	private String cases;

	public GermanWord(String word, String pos, String article, String cases, String lastReviewDate,
			String nextReviewDate, int nextReviewInterval) {
		super();
		this.word = word;
		this.pos = pos;
		this.article = article;
		this.cases = cases;

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		this.lastReviewDate = LocalDate.parse(lastReviewDate, formatter);
		this.nextReviewDate = LocalDate.parse(nextReviewDate, formatter);
		this.nextReviewInterval = nextReviewInterval;
	}
	public String getCases() {
		return cases;
	}
	public void setCases(String cases) {
		this.cases = cases;
	}
	private LocalDate lastReviewDate;
	private LocalDate nextReviewDate;
	public String getWord() {
		return word;
	}
	public void setWord(String word) {
		this.word = word;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getArticle() {
		return article;
	}
	public void setArticle(String article) {
		this.article = article;
	}
	public LocalDate getLastReviewDate() {
		return lastReviewDate;
	}
	public void setLastReviewDate(LocalDate lastReviewDate) {
		this.lastReviewDate = lastReviewDate;
	}
	public LocalDate getNextReviewDate() {
		return nextReviewDate;
	}
	public void setNextReviewDate(LocalDate nextReviewDate) {
		this.nextReviewDate = nextReviewDate;
	}
	public int getNextReviewInterval() {
		return nextReviewInterval;
	}
	public void setNextReviewInterval(int nextReviewInterval) {
		this.nextReviewInterval = nextReviewInterval;
	}
	private int nextReviewInterval;
}
