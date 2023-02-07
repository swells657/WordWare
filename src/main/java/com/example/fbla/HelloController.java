package com.example.fbla;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;

import java.util.*;


public class HelloController {

    // initialize all global variables and fxml classes
    public int[] gameOrder = new int[5];
    List<Integer> gameOrderList = new ArrayList<>();
    public int level = 1;
    public int playerLives = 3;
    public int currentGame = 0;
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyz";
    public String decryptStr = "";
    public boolean isCorrect = false;
    @FXML
    public Label mainText;
    @FXML
    public Label countDownText;
    @FXML
    public Label playerLivesText;
    @FXML
    public TextField playerInputText;
    @FXML
    public Button startGameButton;
    @FXML
    public Button okButton;
    @FXML
    public Label gameText;
    @FXML
    public Label levelText;
    @FXML
    public VBox mainVBox;

    @FXML
    protected void initialize() {

    }

    @FXML
    protected void okButtonPress() {
        // clear ok button and text
        okButton.setVisible(false);
        gameText.setText("");
    }

    @FXML
    protected void startGameBtnClick() {
        playerLives = 3;
        okButton.setVisible(false);
        gameText.setText("");
        // randomize game order with multiple for loops
        for (int i = 0; i < 4; i++) {
            Random rand = new Random();
            int r = rand.nextInt(4);
            gameOrder[i] = r;
            for (int j = 0; j < i; j++) {
                if (gameOrder[i] == gameOrder[j]) {
                    i--;
                }
            }
        }
        // convert game order array to list for easy manipulation later on
        for (int v = 0; v < 5; v++) {
            gameOrderList.add(gameOrder[v]);
        }
        // disable start button
        startGameButton.setDisable(true);
        // switch case to check for which game should be loaded
        switch (gameOrderList.get(0)) {
            case 0 -> synonymGame();
            case 1 -> wordRearrange();
            case 2 -> finishRhyme();
            case 3 -> cipherGame();
        }
    }

    // initialize global variables for use in game
    public String givenWord;
    public String[] synonyms;
    @FXML
    protected void synonymGame() {
        isCorrect = false;
        levelText.setText(String.valueOf(level));
        playerLivesText.setText(String.valueOf(playerLives));
//        mainText.setText("Incorrect!");
        mainVBox.setStyle("-fx-background-color: #874586;");
        if (playerLives < 1) {
            playerLives = 0;
            startGameButton.setDisable(false);
            // set instructional text
            mainText.setText("GAME OVER");
            final String[] playerName = new String[1];
            gameText.setText("Scoreboard\nJohn Doe: 3\nJane Doe: 2\nJack: 2\nTom: 1");
            startGameButton.requestFocus();
            playerInputText.setPromptText("Input Name");
            playerInputText.setOnKeyPressed(ke -> {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    playerName[0] = playerInputText.getText();
                    gameText.setText(gameText.getText() + "\n" + playerName[0] + ": " + level);
                }
            });
        } else {
            // check level with switch case
            switch (level) {
                case 1 -> {
                    givenWord = "fast";
                    synonyms = new String[]{"agile", "brisk", "hot", "nimble", "quick", "rapid", "swift"};
                }
                case 2 -> {
                    givenWord = "important";
                    synonyms = new String[]{"big", "critical", "crucial", "decisive", "essential", "extensive", "far-reaching", "great", "imperative", "influential", "large", "meaningful", "necessary", "paramount", "relevant", "serious", "significant", "urgent", "vital"};
                }
                case 3 -> {
                    givenWord = "old";
                    synonyms = new String[]{"aged", "ancient", "decrepit", "elderly", "gray", "mature", "tired", "venerable"};
                }
            }
            gameText.setText("Input a synonym for " + givenWord);
            // convert synonym array to list for easy checking
            List<String> synonymList = Arrays.asList(synonyms);
            // set game timer thread with allotted time
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int counter = 10;

                @Override
                public void run() {
                    // check available time
                    if (counter > 0) {
                        // print timer and decrement by 1 per second
                        Platform.runLater(() -> countDownText.setText(String.valueOf(counter)));
                        counter--;
                        // repeatedly check player input and allocate to new variable "answer"
                        playerInputText.textProperty().addListener((observable, oldValue, newValue) -> {
                            String answer = (playerInputText.getText()).toLowerCase();
                            // check player answer against correct answers
                            if (!synonymList.contains(answer)) {
//                                mainText.setText("Incorrect!");
//                                mainVBox.setStyle("-fx-background-color: red;");
                            } else {
//                                mainText.setText("Correct!");
                                mainVBox.setStyle("-fx-background-color: #80FF72;");
                                isCorrect = true;
                            }
                        });
                    } else {
//                        mainText.setText("Incorrect!");
                        // time up case
                        Platform.runLater(() -> countDownText.setText("TIME UP!"));
                        // cancel timer and clear stage
                        timer.cancel();
                        playerInputText.clear();
                        playerInputText.requestFocus();
                        // check to go to next level
                        if (currentGame == 3) {
                            level++;
                            mainText.setText("Level " + level);
                        }
                        // check for current game and if we should loop back to start
                        if (currentGame < 4) {
                            currentGame++;
                        } else {
                            currentGame = 0;
                        }
                        System.out.println(currentGame);
                        // move to next game with switch case
                        switch (gameOrderList.get(currentGame)) {
                            case 0 -> Platform.runLater(() -> synonymGame());
                            case 1 -> Platform.runLater(() -> wordRearrange());
                            case 2 -> Platform.runLater(() -> finishRhyme());
                            case 3 -> Platform.runLater(() -> cipherGame());
                        }
                        System.out.println(currentGame);
                        if (!isCorrect) {
                            playerLives--;
                        }
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }

    String scramble = "";
    String correctWord = "";
    @FXML
    protected void wordRearrange() {
        isCorrect = false;
        levelText.setText(String.valueOf(level));
        playerLivesText.setText(String.valueOf(playerLives));
//        mainText.setText("Incorrect!");
        mainVBox.setStyle("-fx-background-color: #874586;");
        if (playerLives < 1) {
            playerLives = 0;
            startGameButton.setDisable(false);
            // set instructional text
            mainText.setText("GAME OVER");
            final String[] playerName = new String[1];
            gameText.setText("Scoreboard\nJohn Doe: 3\nJane Doe: 2\nJack: 2\nTom: 1");
            startGameButton.requestFocus();
            playerInputText.setPromptText("Input Name");
            playerInputText.setOnKeyPressed(ke -> {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    playerName[0] = playerInputText.getText();
                    gameText.setText(gameText.getText() + "\n" + playerName[0] + ": " + level);
                }
            });
        } else {
            switch (level) {
                case 1 -> {
                    scramble = "unje";
                    correctWord = "june";
                }
                case 2 -> {
                    scramble = "anoec";
                    correctWord = "ocean";
                }
                case 3 -> {
                    scramble = "roweatmlen";
                    correctWord = "watermelon";
                }
            }
            gameText.setText("Unscramble the word: " + scramble);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int counter = 10;

                @Override
                public void run() {
                    if (counter > 0) {
                        Platform.runLater(() -> countDownText.setText(String.valueOf(counter)));
                        counter--;
                        playerInputText.textProperty().addListener((observable, oldValue, newValue) -> {
                            String answer = (playerInputText.getText()).toLowerCase();
                            if (!answer.equalsIgnoreCase(correctWord)) {
//                                mainText.setText("Incorrect!");
//                                mainVBox.setStyle("-fx-background-color: red;");
                            } else {
//                                mainText.setText("Correct!");
                                mainVBox.setStyle("-fx-background-color: #80FF72;");
                                isCorrect = true;
                            }
                        });
                    } else {
//                        mainText.setText("Incorrect!");
                        Platform.runLater(() -> countDownText.setText("TIME UP!"));
                        timer.cancel();
                        playerInputText.clear();
                        playerInputText.requestFocus();
                        Platform.runLater(() -> playerLivesText.setText(String.valueOf(playerLives)));
                        if (currentGame == 3) {
                            level++;
                            mainText.setText("Level " + level);
                        }
                        if (currentGame < 4) {
                            currentGame++;
                        } else {
                            currentGame = 0;
                        }
                        System.out.println(currentGame);
                        switch (gameOrderList.get(currentGame)) {
                            case 0 -> Platform.runLater(() -> synonymGame());
                            case 1 -> Platform.runLater(() -> wordRearrange());
                            case 2 -> Platform.runLater(() -> finishRhyme());
                            case 3 -> Platform.runLater(() -> cipherGame());
                        }
                        if (!isCorrect) {
                            playerLives--;
                        }
                        return;
                    }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }

    String rhymeWord = "";
    String rhymeHint = "";
    @FXML
    protected void finishRhyme() {
        isCorrect = false;
        levelText.setText(String.valueOf(level));
        playerLivesText.setText(String.valueOf(playerLives));
//        mainText.setText("Incorrect!");
        mainVBox.setStyle("-fx-background-color: #874586;");
        if (playerLives < 1) {
            playerLives = 0;
            startGameButton.setDisable(false);
            // set instructional text
            mainText.setText("GAME OVER");
            final String[] playerName = new String[1];
            gameText.setText("Scoreboard\nJohn Doe: 3\nJane Doe: 2\nJack: 2\nTom: 1");
            startGameButton.requestFocus();
            playerInputText.setPromptText("Input Name");
            playerInputText.setOnKeyPressed(ke -> {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    playerName[0] = playerInputText.getText();
                    gameText.setText(gameText.getText() + "\n" + playerName[0] + ": " + level);
                }
            });
        } else {
            switch (level) {
                case 1 -> {
                    rhymeWord = "star";
                    rhymeHint = "Finish the Rhyme:\nImagine a pig\nIn a big purple wig\nImagine a car\nIn the shape of a";
                }

                case 2 -> {
                    rhymeWord = "fall";
                    rhymeHint = "Finish the Rhyme:\nTrue friends are by your side\nThrough it all\nTrue friends are there\nTo catch you when you";
                }

                case 3 -> {
                    rhymeWord = "light";
                    rhymeHint = "Finish the Rhyme:\nMy candle burns at both ends\nIt will not last the night\nBut ah, my foes, and oh, my friends\nIt gives a lovely";
                }
            }
            gameText.setText(rhymeHint);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int counter = 10;

                @Override
                public void run() {
                        if (counter > 0) {
                            Platform.runLater(() -> countDownText.setText(String.valueOf(counter)));
                            counter--;
                            playerInputText.textProperty().addListener((observable, oldValue, newValue) -> {
                                String answer = (playerInputText.getText()).toLowerCase();
                                if (!answer.equalsIgnoreCase(rhymeWord)) {
//                                    mainText.setText("Incorrect!");
//                                    mainVBox.setStyle("-fx-background-color: red;");
                                } else {
//                                    mainText.setText("Correct!");
                                    mainVBox.setStyle("-fx-background-color: #80FF72;");
                                    isCorrect = true;
                                }
                            });
                        } else {
//                            mainText.setText("Incorrect!");
                            Platform.runLater(() -> countDownText.setText("TIME UP!"));
                            timer.cancel();
                            playerInputText.clear();
                            playerInputText.requestFocus();
                            Platform.runLater(() -> playerLivesText.setText(String.valueOf(playerLives)));
                            if (currentGame == 3) {
                                level++;
                                mainText.setText("Level " + level);
                            }
                            if (currentGame < 4) {
                                currentGame++;
                            } else {
                                currentGame = 0;
                            }
                            System.out.println(currentGame);
                            switch (gameOrderList.get(currentGame)) {
                                case 0 -> Platform.runLater(() -> synonymGame());
                                case 1 -> Platform.runLater(() -> wordRearrange());
                                case 2 -> Platform.runLater(() -> finishRhyme());
                                case 3 -> Platform.runLater(() -> cipherGame());
                            }
                            if (!isCorrect) {
                                playerLives--;
                            }
                            return;
                        }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }

    String inputStr = "";
    int correctShift = 0;
    @FXML
    protected void cipherGame() {
        isCorrect = false;
        levelText.setText(String.valueOf(level));
        playerLivesText.setText(String.valueOf(playerLives));
//        mainText.setText("Incorrect!");
        mainVBox.setStyle("-fx-background-color: #874586;");
        if (playerLives < 1) {
            playerLives = 0;
            startGameButton.setDisable(false);
            // set instructional text
            mainText.setText("GAME OVER");
            final String[] playerName = new String[1];
            gameText.setText("Scoreboard\nJohn Doe: 3\nJane Doe: 2\nJack: 2\nTom: 1");
            startGameButton.requestFocus();
            playerInputText.setPromptText("Input Name");
            playerInputText.setOnKeyPressed(ke -> {
                if (ke.getCode().equals(KeyCode.ENTER)) {
                    playerName[0] = playerInputText.getText();
                    gameText.setText(gameText.getText() + "\n" + playerName[0] + ": " + level);
                }
            });
        } else {
            switch (level) {
                case 1 -> {
                    inputStr = "znyvpr";
                    correctShift = 13;
                }

                case 2 -> {
                    inputStr = "lxwodbrxw";
                    correctShift = 9;
                }

                case 3 -> {
                    inputStr = "aflgpauslagf";
                    correctShift = 18;
                }
            }
            gameText.setText("Find the correct offset to decipher the code: " + inputStr);
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                int counter = 10;

                @Override
                public void run() {
                        if (counter > 0) {
                            Platform.runLater(() -> countDownText.setText(String.valueOf(counter)));
                            counter--;
                            playerInputText.textProperty().addListener((observable, oldValue, newValue) -> {
                                // make blank string for decrypted user input
                                decryptStr = "";
                                int shiftKey = Integer.parseInt(playerInputText.getText());
                                // run for loop to decrypt answer based on player's input
                                for (int i = 0; i < inputStr.length(); i++) {
                                    int pos = ALPHABET.indexOf(inputStr.charAt(i));
                                    int decryptPos = (pos - shiftKey) % 26;
                                    if (decryptPos < 0) {
                                        decryptPos = ALPHABET.length() + decryptPos;
                                    }
                                    char decryptChar = ALPHABET.charAt(decryptPos);
                                    decryptStr += decryptChar;
                                }
                                gameText.setText("Find the correct offset to decipher the code: " + decryptStr);

                                if (shiftKey != correctShift) {
//                                    mainVBox.setStyle("-fx-background-color: red;");
                                } else {
                                    mainVBox.setStyle("-fx-background-color: #80FF72;");
                                    isCorrect = true;
                                }
                            });
                        } else {
//                            mainText.setText("Incorrect!");
                            Platform.runLater(() -> countDownText.setText("TIME UP!"));
                            timer.cancel();
                            playerInputText.clear();
                            playerInputText.requestFocus();
                            Platform.runLater(() -> playerLivesText.setText(String.valueOf(playerLives)));
                            if (currentGame == 3) {
                                level++;
                                mainText.setText("Level " + level);
                            }
                            if (currentGame < 4) {
                                currentGame++;
                            } else {
                                currentGame = 0;
                            }
                            System.out.println(currentGame);
                            switch (gameOrderList.get(currentGame)) {
                                case 0 -> Platform.runLater(() -> synonymGame());
                                case 1 -> Platform.runLater(() -> wordRearrange());
                                case 2 -> Platform.runLater(() -> finishRhyme());
                                case 3 -> Platform.runLater(() -> cipherGame());
                            }
                            if (!isCorrect) {
                                playerLives--;
                            }
                        }
                }
            };
            timer.scheduleAtFixedRate(task, 0, 1000);
        }
    }
}
