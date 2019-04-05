package me.rayentwickler.flashcard;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Flashcard extends Application implements Runnable {
	public static boolean drillFlag = false;

	@Override
	public void start(Stage primaryStage) {
		try {
			List<GermanWord> list = getFullReviewList();
			List<GermanWord> shortList = getShortReviewList(list, 12);
			Scene vocabScene = reviewVocab(shortList);

			showBeginPrompt();
			if (drillFlag == true) {
				primaryStage.setScene(vocabScene);
				primaryStage.show();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void showBeginPrompt() {
		VBox vbox = new VBox();
		Scene scene = new Scene(vbox, 1000, 1000);
		Stage stage = new Stage();
		stage.setTitle("Guten Tag");
		stage.setScene(scene);

		Label label = new Label("It's time for German Vocabulary Drill! Press OK to start");
		setLabelFontWidthHeight(label, "Georgia", 30, 300, 100);

		Button btnOK = new Button("OK!");
		setButtonFontWidthHeight(btnOK, "Georgia", 30, 200, 100);
		btnOK.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				stage.close();
				drillFlag = true;
			}
		});

		Button btnCancel = new Button("Cancel");
		setButtonFontWidthHeight(btnCancel, "Georgia", 30, 200, 100);
		btnCancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				System.exit(0);
			}
		});

		vbox.getChildren().add(label);
		vbox.getChildren().add(getStartupIcon());
		vbox.getChildren().add(btnOK);
		vbox.getChildren().add(btnCancel);
		vbox.setAlignment(Pos.CENTER);
		stage.showAndWait();
	}

	public Scene reviewVocab(List<GermanWord> list) {
		ScrollPane sp = new ScrollPane();
		Scene scene = new Scene(sp, 1000, 800);

		FlowPane flowPane = new FlowPane();
		flowPane.setOrientation(Orientation.HORIZONTAL);
		flowPane.setPrefWrapLength(800);
		VBox vbox = new VBox();
		Text title = new Text("German Word list for review");
		title.setFont(Font.font("Georgia", FontWeight.BOLD, 30));
		vbox.getChildren().add(title);
		vbox.getChildren().add(flowPane);

		for (GermanWord word : list) {
			System.out.println("Showing " + word.getWord());
			Label label = new Label(word.getArticle() + " " + word.getWord());
			setLabelFontWidthHeight(label, "Georgia", 30, 400, 100);
			if ("der".equals(word.getArticle())) {
				label.setTextFill(Color.web("#4d79ff"));
			} else if ("die".equals(word.getArticle())) {
				label.setTextFill(Color.web("#ff6699"));
			} else if ("das".equals(word.getArticle())) {
				label.setTextFill(Color.web("#ffd11a"));
			}
			flowPane.getChildren().add(label);
		}

		HBox startButtonHBox = new HBox();
		Button btn = new Button("Start Flashcard Training!");
		setButtonFontWidthHeight(btn, "Georgia", 20, 600, 100);
		startButtonHBox.getChildren().add(btn);
		startButtonHBox.setAlignment(Pos.BOTTOM_LEFT);

		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
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

			}
		});

		vbox.getChildren().add(startButtonHBox);
		sp.setContent(vbox);
		return scene;
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

	public List<GermanWord> getShortReviewList(List<GermanWord> fullList, int num) {
		Collections.shuffle(fullList);
		if (num < 0) {
			num = 1;
		} else if (num > fullList.size()) {
			num = 10;
		}
		return fullList.subList(0, num - 1);
	}

	public List<GermanWord> getFullReviewList() throws ClassNotFoundException, SQLException {
		List<GermanWord> list = new ArrayList<GermanWord>();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate today = LocalDate.now();
		String todayString = today.format(formatter);

		String sql = "select * from Vokabeln where Next_Review_Date <= " + '"' + todayString + '"';
		System.out.println(sql);

		SQLiteUtil sqllite = null;
		try {
			sqllite = new SQLiteUtil();
			sqllite.connect();
			ResultSet rs = sqllite.executeSelectSQL(sql);
			while (rs.next()) {
				GermanWord word = new GermanWord(rs.getString("Word"), rs.getString("POS"), rs.getString("Article"),
						rs.getString("cases"), rs.getString("Last_Review_Date"), rs.getString("Next_Review_Date"),
						rs.getInt("Next_Review_Interval"));
				list.add(word);
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
		} finally {
			sqllite.freeDB();
		}

		return list;
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

	public ImageView getStartupIcon() {
		Image image = null;
		image = new Image(Flashcard.class.getClassLoader().getResourceAsStream("brandenburgertor.jpg"));
		ImageView imageView = new ImageView(image);
		return imageView;

	}

	public static void main(String[] args) {
		ScheduledExecutorService service = Executors.newScheduledThreadPool(10);
		Random rand = new Random();
		long initialDelay = rand.nextInt(1);
		System.out.println("The service would start after " + initialDelay + " minutes.");
		service.schedule(new Flashcard(), initialDelay, TimeUnit.MINUTES);
	}

	@Override
	public void run() {
		try {
			String[] args = null;
			launch(args);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
