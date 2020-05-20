package view;

import entities.Field;
import entities.Generator;
import entities.Neighbors;
import view.exceptions.AppException;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;
import view.utilities.I18N;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import javafx.scene.image.ImageView;

public class Controller {
    @FXML
    public Label fileName;

    public GridPane table;
    public Button buttonGenerate;
    public Button buttonLoad;
    public Label labelPuzzle;
    public Menu menuFile;
    public Menu menuLanguage;
    public Menu menuHelp;
    public MenuItem exit;
    public MenuItem menuLoadFile;
    public MenuItem help;
    public MenuItem about;
    public RadioMenuItem russian;
    public RadioMenuItem english;
    public Label countRows;
    public Label countColumns;
    public TextField textFieldColumns;
    public TextField textFieldRows;
    public Button buttonCheck;

    private Stage stage;
    File file;
    Field field;

    public void bind() {
        buttonGenerate.textProperty().bind(I18N.createStringBinding("button.generate"));
        buttonCheck.textProperty().bind(I18N.createStringBinding("button.check"));
        countRows.textProperty().bind(I18N.createStringBinding("label.countRows"));
        countColumns.textProperty().bind(I18N.createStringBinding("label.countColumns"));
        buttonLoad.textProperty().bind(I18N.createStringBinding("button.load"));
        labelPuzzle.textProperty().bind(I18N.createStringBinding("label.puzzle"));
        menuFile.textProperty().bind(I18N.createStringBinding("menu.file"));
        menuLanguage.textProperty().bind(I18N.createStringBinding("menu.language"));
        menuHelp.textProperty().bind(I18N.createStringBinding("menu.help"));
        exit.textProperty().bind(I18N.createStringBinding("exit"));
        menuLoadFile.textProperty().bind(I18N.createStringBinding("button.load"));
        help.textProperty().bind(I18N.createStringBinding("menu.help"));
        about.textProperty().bind(I18N.createStringBinding("about"));

        russian.textProperty().bind(I18N.createStringBinding("russian"));
        english.textProperty().bind(I18N.createStringBinding("english"));
    }

    public void textFieldProperties() {
        textFieldRows.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldRows.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
        textFieldColumns.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                textFieldColumns.setText(newValue.replaceAll("[^\\d]", ""));
            }
        });
    }

    @FXML
    public void loadFile(ActionEvent actionEvent) {
        final FileChooser fileChooser = new FileChooser();
        file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try {
                field = getField(file);
                fileName.setText(I18N.get("uploadedFile") + "\n" + file.getName());
            } catch (FileNotFoundException e) {
                showError(new AppException(I18N.get("FILE_NOT_LOADED"), e));
                e.printStackTrace();
            } catch (Exception e) {
                showError(new AppException(I18N.get("INVALID_FORMAT") + I18N.get("INPUT_FILE_FORMAT"), e));
                e.printStackTrace();
            }
        }
        loadFromFile();
    }

    private Field getField(File file) throws FileNotFoundException {
        Scanner s = new Scanner(file);
        int countRows = s.nextInt();
        int countColumns = s.nextInt();
        Field field = new Field(countRows, countColumns);

        Integer[][] board = field.getField();

        for (int i = 0; i < countRows; i++) {
            for (int j = 0; j < countColumns; j++) {
                String value = s.next();
                switch (value) {
                    case "ball":
                        board[i][j] = -1;
                        break;
                    case "empty":
                        board[i][j] = null;
                        break;
                    default:
                        board[i][j] = Integer.valueOf(value);
                        break;
                }
            }
        }

        return field;
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadFromFile() {
        if (file == null) {
            showError(new AppException(I18N.get("FILE_NOT_UPLOADED"), new Exception()));
            return;
        }
        try {
            field = getField(file);
            generatePuzzle();
        } catch (FileNotFoundException e) {
            showError(new AppException(I18N.get("FILE_NOT_LOADED"), e));
            e.printStackTrace();
        }
    }

    public void generateField(ActionEvent actionEvent) {
        try {
            int countRows, countColumns;
            try {
                countRows = Integer.parseInt(textFieldRows.getText());
                countColumns = Integer.parseInt(textFieldColumns.getText());
            } catch (Exception t) {
                throw new AppException(I18N.get("INVALID_VALUES"));
            }
            if (countRows % 2 != 1 || countColumns % 2 != 1 || !(countRows <= 9 && countRows >= 3 && countColumns <= 13 && countColumns >= 3)) {
                throw new AppException(I18N.get("INVALID_VALUES"));
            }
            deleteFile(actionEvent);
            field = Generator.generateField(countRows, countColumns);
            generatePuzzle();
        } catch (AppException e) {
            showError(e);
        }

    }

    private void deletePreviousPuzzle() {
        table.setGridLinesVisible(false);
        table.getColumnConstraints().clear();
        table.getRowConstraints().clear();
        table.getChildren().clear();
        table.setGridLinesVisible(true);
    }

    public void generatePuzzle() {
        deletePreviousPuzzle();
        Integer[][] board = field.getField();

        for (int i = 0; i < field.getCountRows(); i++) {
            for (int j = 0; j < field.getCountColumns(); j++) {
                if (board[i][j] == null) {
                    ObservableList<String> items = FXCollections.observableArrayList("");
                    for (int t = 1; t <= 8; t++) {
                        String item = Integer.toString(t);
                        items.add(item);
                    }
                    ComboBox<String> node = new ComboBox<>(items);
                    node.setPrefWidth(68);
                    node.setPrefHeight(68);
                    node.setCellFactory(new Callback<>() {
                        @Override
                        public ListCell<String> call(ListView<String> param) {
                            return new ListCell<>() {
                                @Override
                                public void updateItem(String item, boolean empty) {
                                    super.updateItem(item, empty);
                                    if (item != null) {
                                        setText(item);
                                        setFont(Font.font(this.getFont().getName(), 30.0)); //set your desired size
                                    } else {
                                        setText(null);
                                    }
                                }
                            };
                        }
                    });
                    node.setButtonCell(new ListCell() {
                        @Override
                        protected void updateItem(Object item, boolean empty) {
                            super.updateItem(item, empty);
                            if (empty || item == null) {
                                setStyle("-fx-font-size:22");
                            } else {
                                setStyle("-fx-font-size:22");
                                setText(item.toString());
                            }
                        }
                    });
                    node.setBackground(new Background(new BackgroundFill(Color.INDIANRED, CornerRadii.EMPTY, Insets.EMPTY)));
                    node.getStyleClass().add("center-aligned");
                    GridPane.setHalignment(node, HPos.CENTER);
                    GridPane.setValignment(node, VPos.CENTER);
                    GridPane.setMargin(node, new Insets(5, 5, 5, 5));
                    table.add(node, j, i);
                } else {
                    if (board[i][j] == -1) {
                        Image image = new Image("/view/res/ball-48.png");
                        ImageView imageView = new ImageView(image);
                        GridPane.setHalignment(imageView, HPos.CENTER);
                        GridPane.setValignment(imageView, VPos.CENTER);
                        table.add(imageView, j, i);
                    } else {
                        Label label = new Label();
                        label.setFont(Font.font(30));
                        label.setText(Integer.toString(board[i][j]));
                        GridPane.setHalignment(label, HPos.CENTER);
                        GridPane.setValignment(label, VPos.CENTER);
                        GridPane.setMargin(label, new Insets(12, 25, 12, 25));
                        table.add(label, j, i);
                    }
                }
            }
        }
    }

    private void showError(AppException e) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(600);
        alert.setTitle(I18N.get("error"));
        alert.setHeaderText(null);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }

    public void helpAction() {
        informationDialog(I18N.get("HELP_MESSAGE") + I18N.get("INPUT_FILE_FORMAT"), I18N.get("menu.help"));
    }

    public void infoProgramme() {
        informationDialog(I18N.get("INFO_PROGRAMME"), I18N.get("about"));
    }

    public void informationDialog(String message, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.getDialogPane().setMinWidth(600);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void exit(ActionEvent actionEvent) {
        Platform.exit();
        System.exit(0);
    }

    public void deleteFile(ActionEvent actionEvent) {
        fileName.setText(I18N.get("label.fileName"));
        field = null;
        file = null;
    }

    public void translateToRussian(ActionEvent actionEvent) {
        I18N.setLocale(new Locale("ru", ""));
        if (file == null) {
            fileName.setText(I18N.get("label.fileName"));
        } else {
            fileName.setText(I18N.get("uploadedFile") + "\n" + file.getName());
        }
    }

    public void translateToEnglish(ActionEvent actionEvent) {
        I18N.setLocale(Locale.ENGLISH);
        if (file == null) {
            fileName.setText(I18N.get("label.fileName"));
        } else {
            fileName.setText(I18N.get("uploadedFile") + "\n" + file.getName());
        }
    }

    public void checkField(ActionEvent actionEvent) {
        if (field != null) {
            boolean rightPuzzle = true;
            for (int i = 1; i < field.getCountRows(); i += 2) {
                for (int j = 1; j < field.getCountColumns(); j += 2) {
                    List<Integer> list = getNumbersNearBall(i, j);
                    for (int n = 1; n < 9; n++) {
                        rightPuzzle &= list.contains(n);
                    }
                }
            }

            String message;
            if (rightPuzzle) {
                message = I18N.get("RIGHT_PUZZLE_MESSAGE");
            } else {
                message = I18N.get("WRONG_PUZZLE_MESSAGE");
            }
            informationDialog(message, I18N.get("RESULT"));
        }
    }

    private List<Integer> getNumbersNearBall(int rowBall, int columnBall) {
        List<Integer> numbers = new ArrayList<>();
        List<Pair<Integer, Integer>> neighbors = Neighbors.getNeighbors(field, rowBall, columnBall);
        for (Pair<Integer, Integer> p : neighbors) {
            int row = p.getKey();
            int column = p.getValue();
            if (field.getField()[row][column] != null) {
                numbers.add(field.getField()[row][column]);
            } else {
                String s = ((ComboBox<String>) getNodeByRowColumnIndex(row, column, table)).getSelectionModel().getSelectedItem();
                int n = 0;
                if (s != null && !s.equals("")) {
                    n = Integer.parseInt(s);
                }
                numbers.add(n);
            }
        }
        return numbers;
    }

    private Node getNodeByRowColumnIndex(final int row, final int column, GridPane gridPane) {
        Node result = null;
        ObservableList<Node> children = gridPane.getChildren();

        for (Node node : children) {
            if (GridPane.getRowIndex(node) != null && GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }

        return result;
    }
}
