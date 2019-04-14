package me.rayentwickler.flashcard;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class Flashcard implements Runnable {

	List<GermanWord> list;

	public Flashcard(List<GermanWord> list) {
		this.list = list;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		List<GermanWord> wrongList = new ArrayList<GermanWord>();
		for (GermanWord word : list) {
			GermanWord wrongWord = generalReviseStage(list, word);
			if (wrongWord != null) {
				wrongList.add(wrongWord);
			}
		}

		for (GermanWord word : wrongList) {
			generalReviseStage(list, word);
		}

		updateReviewedList(list);
		return;
	}

	public GermanWord generalReviseStage(List<GermanWord> list, GermanWord word) {
		VBox bigVBox = new VBox();
		Scene scene = new Scene(bigVBox, 1000, 500);
		Stage stage = new Stage();
		stage.setTitle("Flash Card");
		stage.setScene(scene);

		HBox flashcardFront = new HBox();
		Label label = new Label(word.getWord());
		setLabelFontWidthHeight(label, "Georgia", 60, 400, 100);
		flashcardFront.getChildren().add(label);
		flashcardFront.setAlignment(Pos.CENTER);

		HBox threeChoices = new HBox();
		Button btn1 = new Button("der");
		Button btn2 = new Button("die");
		Button btn3 = new Button("das");
		setButtonFontWidthHeight(btn1, "Georgia", 30, 200, 100);
		setButtonFontWidthHeight(btn2, "Georgia", 30, 200, 100);
		setButtonFontWidthHeight(btn3, "Georgia", 30, 200, 100);
		threeChoices.setAlignment(Pos.CENTER);

		HandlerReturnValue handlerReturnValue = new HandlerReturnValue();
		final EventHandler<ActionEvent> myHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(final ActionEvent event) {
				Button button = (Button) event.getSource();
				if (button.getText().equals(word.getArticle())) {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Correct!");
					alert.showAndWait();
					stage.close();
				} else {
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Information Dialog");
					alert.setHeaderText(null);
					alert.setContentText("Wrong!");
					alert.showAndWait();
					handlerReturnValue.setWord(word);
					handlerReturnValue.setHasReturnValue(true);
				}
			}
		};

		btn1.setOnAction(myHandler);
		btn2.setOnAction(myHandler);
		btn3.setOnAction(myHandler);

		threeChoices.getChildren().add(btn1);
		threeChoices.getChildren().add(btn2);
		threeChoices.getChildren().add(btn3);

		bigVBox.getChildren().add(flashcardFront);
		bigVBox.getChildren().add(threeChoices);
		bigVBox.setAlignment(Pos.CENTER);
		stage.showAndWait();

		if (handlerReturnValue.getHasReturnValue()) {
			return handlerReturnValue.getWord();
		} else {
			return null;
		}
	}

	public void setLabelFontWidthHeight(Label label, String font, int fontSize, int width, int height) {
		label.setFont(Font.font(font, FontWeight.BOLD, fontSize));
		label.setMinWidth(width);
		label.setMinHeight(height);
	}

	public void setButtonFontWidthHeight(Button btn, String font, int fontSize, int width, int height) {
		btn.setFont(Font.font(font, FontWeight.BOLD, fontSize));
		btn.setMinWidth(width);
		btn.setMinHeight(height);
	}

	public void updateReviewedList(List<GermanWord> list) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now();
		String todayString = today.format(formatter);

		SQLiteUtil sqllite = null;
		try {
			sqllite = new SQLiteUtil();
			sqllite.connect();
			for (GermanWord word : list) {
				int nextReviewInterval = word.getNextReviewInterval() * 2;
				LocalDate nextReviewDate = today.plusDays(nextReviewInterval);
				String nextReviewDateString = nextReviewDate.format(formatter);
				String sql = "Update Vokabeln set Last_Review_Date = " + "\"" + todayString + "\", "
						+ "Next_Review_Date = " + "\"" + nextReviewDateString + "\", Next_Review_Interval="
						+ Integer.toString(nextReviewInterval) + " where word = " + "\"" + word.getWord() + "\"";
				System.out.println(sql);
				sqllite.executeUpdateSQL(sql);

			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			sqllite.freeDB();
		}

	}

}
