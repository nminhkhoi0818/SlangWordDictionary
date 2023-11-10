package com.example.slangworddictionary;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.Text;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class DashboardController {
    // Navigation button
    @FXML
    private Button add_slang_btn;

    @FXML
    private Button delete_word_btn;

    @FXML
    private Button edit_word_btn;

    @FXML
    private Button find_def_btn;

    @FXML
    private Button find_slang_btn;

    @FXML
    private Button quiz_game_btn;

    @FXML
    private Button random_word_btn;

    @FXML
    private Button reset_btn;

    @FXML
    private Button search_history_btn;

    @FXML
    private Button view_all_btn;

    @FXML
    private Button about_btn;

    // Form - AnchorPane
    @FXML
    private AnchorPane view_all_form;

    @FXML
    private AnchorPane find_def_form;

    @FXML
    private AnchorPane find_slang_form;

    @FXML
    private AnchorPane search_history_form;

    @FXML
    private AnchorPane add_slang_form;

    @FXML
    private AnchorPane edit_form;

    @FXML
    private AnchorPane delete_form;

    @FXML
    private AnchorPane random_form;

    @FXML
    private AnchorPane quiz_form;

    @FXML
    private AnchorPane quiz_slang_form;

    @FXML
    private AnchorPane quiz_def_form;

    @FXML
    private AnchorPane about_form;

    // 1. View all words

    @FXML
    private TableView<SlangDefinition> view_table;

    @FXML
    private TableColumn<SlangDefinition, String> view_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> view_slang_col;

    @FXML
    private TableColumn<SlangDefinition, String> view_index_col;

    // 2. Filter by definition
    @FXML
    private TableView<SlangDefinition> filter_def_table;

    @FXML
    private TableColumn<SlangDefinition, String> filter_index_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_col;

    @FXML
    private TextField filter_def;

    // 3. Filter by slang word
    @FXML
    private TableView<SlangDefinition> filter_slang_table;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_index_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_slang_col;

    @FXML
    private TextField filter_slang;

    // 4. Search history

    @FXML
    private TableView<SearchHistoryEntry> search_history_table;

    @FXML
    private TableColumn<SearchHistoryEntry, String> word_history_col;

    @FXML
    private TableColumn<SlangDefinition, LocalDateTime> time_history_col;

    // 5. Add new slang
    @FXML
    private TextField add_def_field;

    @FXML
    private TextField add_slang_field;

    // 6. Edit slang word
    @FXML
    private TextField edit_search;

    @FXML
    private Button edit_search_btn;

    @FXML
    private TableColumn<SlangDefinition, String> edit_slang_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> edit_slang_index_col;

    @FXML
    private TableColumn<SlangDefinition, String> edit_slang_slang_col;

    @FXML
    private TableView<SlangDefinition> edit_table;

    // 7. Delete slang word
    @FXML
    private TextField delete_search;

    @FXML
    private Button delete_search_btn;

    @FXML
    private TableColumn<SlangDefinition, String> delete_slang_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> delete_slang_index_col;

    @FXML
    private TableColumn<SlangDefinition, String> delete_slang_slang_col;

    @FXML
    private TableView<SlangDefinition> delete_table;

    // 8. Random slang word
    @FXML
    private Text random_slang_field;

    @FXML
    private Text random_def_field;

    @FXML
    private Button random_refresh_btn;

    // Quiz Game
    @FXML
    private Label quiz_header;

    @FXML
    private Button quiz_slang_btn;

    @FXML
    private Button quiz_def_btn;

    // 9. Slang quiz game
    @FXML
    private Text quiz_slang_question;

    @FXML
    private Button quiz_slang_a;

    @FXML
    private Button quiz_slang_b;

    @FXML
    private Button quiz_slang_c;

    @FXML
    private Button quiz_slang_d;

    // 10. Definition quiz game
    @FXML
    private Text quiz_def_question;

    @FXML
    private Button quiz_def_a;

    @FXML
    private Button quiz_def_b;

    @FXML
    private Button quiz_def_c;

    @FXML
    private Button quiz_def_d;

    private List<SearchHistoryEntry> searchHistory = new ArrayList<>();

    public void initialize() throws IOException {
        readAllWords();
        // Add filter for search
        filter_def.textProperty().addListener((observable, oldValue, newValue) -> {
            searchSlangByDef();
        });
        filter_slang.textProperty().addListener((observable, oldValue, newValue) -> {
            searchDefBySlang();
        });
        edit_search.textProperty().addListener((observable, oldValue, newValue) -> {
            searchInEdit();
        });
        delete_search.textProperty().addListener((observable, oldValue, newValue) -> {
            searchInDelete();
        });

        // Activate random word
        getRandomWord();
    }

    void readAllWords() throws IOException {
        Dictionary.loadData(Dictionary.DATA_DIR);
        ObservableList<SlangDefinition> slangData = FXCollections.observableArrayList();

        for (String slang : Dictionary.data.keySet()) {
            Set<String> definitions = Dictionary.data.get(slang);
            for (String definition : definitions) {
                slangData.add(new SlangDefinition(slang, definition));
            }
        }
        view_index_col.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(view_table.getItems().indexOf(param.getValue()) + 1).asString());
        view_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
        view_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));

        view_table.setItems(slangData);
    }

    void readSearchHistoryData() throws IOException {
        ObservableList<SearchHistoryEntry> historyData = FXCollections.observableArrayList();
        readSearchHistoryFromFile(historyData);
        word_history_col.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
        time_history_col.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        search_history_table.setItems(historyData);
    }

    void readSearchHistoryFromFile(ObservableList<SearchHistoryEntry> historyData) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader("assets/history.txt"));
        String line;
        while (((line = br.readLine()) != null)) {
            String word = line.split(" ")[0];
            LocalDateTime timestamp = LocalDateTime.parse(line.split(" ")[1]);
            historyData.add(new SearchHistoryEntry(word, timestamp));
        }
    }

    @FXML
    void switchForm(ActionEvent event) throws IOException {
        List<AnchorPane> forms = Arrays.asList(
                view_all_form, find_def_form, find_slang_form, search_history_form,
                add_slang_form, edit_form, delete_form, random_form, quiz_form, about_form
        );

        for (AnchorPane form : forms) {
            form.setVisible(false);
        }

        if (event.getSource() == view_all_btn) {
            view_all_form.setVisible(true);
            readAllWords();
        } else if (event.getSource() == find_def_btn) {
            find_def_form.setVisible(true);
            readAllWords();
        } else if (event.getSource() == find_slang_btn) {
            find_slang_form.setVisible(true);
            readAllWords();
        } else if (event.getSource() == search_history_btn) {
            search_history_form.setVisible(true);
            readSearchHistoryData();
        } else if (event.getSource() == add_slang_btn) {
            add_slang_form.setVisible(true);
        } else if (event.getSource() == edit_word_btn) {
            edit_form.setVisible(true);
        } else if (event.getSource() == delete_word_btn) {
            delete_form.setVisible(true);
        } else if (event.getSource() == random_word_btn) {
            random_form.setVisible(true);
        } else if (event.getSource() == quiz_game_btn) {
            quiz_form.setVisible(true);
            quiz_slang_form.setVisible(false);
            quiz_def_form.setVisible(false);
            handleQuizForm(true);
        } else if (event.getSource() == about_btn) {
            about_form.setVisible(true);
        }
    }

    void saveSearchHistoryToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("assets/history.txt", true));
            for (SearchHistoryEntry entry : searchHistory) {
                bw.write(entry.getSearchTerm() + " " + LocalDateTime.parse(entry.getTimestamp(), DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy")) + "\n");
            }
            bw.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void addSearchToHistory(String searchTerm) {
        searchHistory.add(new SearchHistoryEntry(searchTerm));
        saveSearchHistoryToFile();
    }

    @FXML
    void searchSlangByDef() { search(filter_def, "definition"); }

    @FXML
    void searchDefBySlang() {
        search(filter_slang, "slang");
    }

    private void searchInEdit() {
        search(edit_search, "edit");
    }

    private void searchInDelete() {
        search(delete_search, "delete");
    }

    private void search(TextField filterSlang, String filterType) {
        String searchTerm = filterSlang.getText().toLowerCase().trim();
        FilteredList<SlangDefinition> filteredData = new FilteredList<>(view_table.getItems());

        filteredData.setPredicate(slangDefinition -> {
            if (searchTerm.isEmpty()) {
                return true;
            }
            if (Objects.equals(filterType, "definition"))
                return slangDefinition.getDefinition().toLowerCase().startsWith(searchTerm);
            else
                return slangDefinition.getSlang().toLowerCase().startsWith(searchTerm);
        });

        if (Objects.equals(filterType, "definition")) {
            filter_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
            filter_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));
            filter_index_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(filter_def_table.getItems().indexOf(cellData.getValue()) + 1).asString());
            filter_def_table.setItems(filteredData);
        }
        else if (Objects.equals(filterType, "slang")) {
            filter_slang_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
            filter_slang_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));
            filter_slang_index_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(filter_slang_table.getItems().indexOf(cellData.getValue()) + 1).asString());
            filter_slang_table.setItems(filteredData);
        }
        else if (Objects.equals(filterType, "edit")) {
            edit_slang_slang_col.setCellValueFactory(new PropertyValueFactory<>("slang"));
            edit_slang_def_col.setCellValueFactory(new PropertyValueFactory<>("definition"));
            edit_slang_index_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(edit_table.getItems().indexOf(cellData.getValue()) + 1).asString());
            edit_table.setItems(filteredData);
        } else {
            delete_slang_slang_col.setCellValueFactory(new PropertyValueFactory<>("slang"));
            delete_slang_def_col.setCellValueFactory(new PropertyValueFactory<>("definition"));
            delete_slang_index_col.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(delete_table.getItems().indexOf(cellData.getValue()) + 1).asString());
            delete_table.setItems(filteredData);
        }
    }

    public void onSlangSearchClick() {
        String searchTerm = filter_slang.getText();
        addSearchToHistory(searchTerm);
    }

    public void onDefSearchClick() {
        String searchTerm = filter_def.getText();
        addSearchToHistory(searchTerm);
    }

    void updateDataInFile(TreeMap<String, Set<String>> data) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("assets/data/slang.txt"));

        for (Map.Entry<String, Set<String>> entry : data.entrySet()) {
            String slang = entry.getKey();
            Set<String> definitions = entry.getValue();
            String definitionsStr = String.join("|", definitions);
            String line = slang + "`" + definitionsStr;
            bw.write(line + "\n");
        }

        bw.close();
    }


    void addSlangToDictionary(String slang, String definition) throws IOException {
        BufferedWriter bw = new BufferedWriter(new FileWriter("assets/data/slang.txt", true));
        bw.write(slang + "`" + definition + "\n");
        bw.close();
    }

    public void onSubmitNewWordButton() throws IOException {
        String slang = add_slang_field.getText();
        String definition = add_def_field.getText();
        if (!Objects.equals(slang, "") && !Objects.equals(definition, "")) {
            if (Dictionary.getData().containsKey(slang)) {
                Alert alert;
                alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Do you want to overwrite this slang word");
                alert.getButtonTypes().clear();
                alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
                Optional<ButtonType> option = alert.showAndWait();
                if (option.get().equals(ButtonType.YES)) {
                    Dictionary.getData().get(slang).clear();
                    Dictionary.getData().get(slang).add(definition);
                    updateDataInFile(Dictionary.getData());
                    displayResultMessage("Successfully Overwrite!");
                } else if (option.get().equals(ButtonType.NO)) {
                    addSlangToDictionary(slang, definition);
                    displayResultMessage("Successfully Duplicated!");
                }
            }
            else {
                Set<String> definitions;
                definitions = new HashSet<>();
                definitions.add(definition);
                Dictionary.getData().put(slang, definitions);
                updateDataInFile(Dictionary.getData());
                displayResultMessage("Successfully Added!");
            }
        }
        readAllWords();
    }

    private void displayResultMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void getItem() throws IOException {
        ObservableList<SlangDefinition> selectedSlang = edit_table.getSelectionModel().getSelectedItems();
        VBox content = new VBox(10);
        Label slangLabel = new Label("Slang: ");
        Label meaningLabel = new Label("Meaning: ");
        GridPane grid = new GridPane();
        TextField slangField = new TextField();
        TextField meaningField = new TextField();
        grid.setHgap(10);
        grid.setVgap(5);
        grid.add(slangLabel, 0, 0);
        grid.add(meaningLabel, 0, 1);
        grid.add(slangField, 1, 0);
        GridPane.setHgrow(slangField, Priority.ALWAYS);
        grid.add(meaningField, 1, 1);
        GridPane.setHgrow(meaningField, Priority.ALWAYS);
        content.getChildren().add(grid);

        Dialog<Object> dialog = new Dialog<>();
        dialog.getDialogPane().setHeaderText("Edit word");
        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        Button button = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        button.addEventFilter(ActionEvent.ACTION, new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    if (!validateDialog()) {
                        event.consume();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

            private boolean validateDialog() throws IOException {
                if ((slangField.getText().isEmpty()) || (meaningField.getText().isEmpty())) {
                    return false;
                }
                String editSlang = selectedSlang.get(0).getSlang();
                String editDef = selectedSlang.get(0).getDefinition();
                // Delete entry
                if (Dictionary.getData().containsKey(editSlang)) {
                    Set<String> existDef = Dictionary.getData().get(editSlang);
                    existDef.remove(editDef);

                    if (existDef.isEmpty()) {
                        Dictionary.getData().remove(editSlang);
                    }
                }

                // Add new entry
                String newSlang = slangField.getText();
                String newDef = meaningField.getText();

                if (!Objects.equals(newSlang, "") && !Objects.equals(newDef, "")) {
                    if (Dictionary.getData().containsKey(newSlang)) {
                        Dictionary.getData().get(newSlang).add(newDef);
                    } else {
                        Set<String> newSetDef = new HashSet<>(Collections.singleton(newDef));
                        Dictionary.getData().put(newSlang, newSetDef);
                    }
                }
                saveDictionaryToFile();
                readAllWords();
                searchInEdit();
                return true;
            }
        });
        dialog.show();
    }

    private void saveDictionaryToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter(Dictionary.DATA_DIR, false));
            for (Map.Entry<String, Set<String>> entry : Dictionary.getData().entrySet()) {
                String slang = entry.getKey();
                String definitions = String.join("|", entry.getValue());
                bw.write(slang + "`" + definitions + "\n");
            }
            bw.close();
        } catch (IOException ignored) {

        }
    }

    public void getItemDelete() throws IOException {
        ObservableList<SlangDefinition> selectedSlang = delete_table.getSelectionModel().getSelectedItems();
        String editSlang = selectedSlang.get(0).getSlang();
        String editDef = selectedSlang.get(0).getDefinition();
        // Delete entry
        if (Dictionary.getData().containsKey(editSlang)) {
            Set<String> existDef = Dictionary.getData().get(editSlang);
            existDef.remove(editDef);

            if (existDef.isEmpty()) {
                Dictionary.getData().remove(editSlang);
            }
        }
        saveDictionaryToFile();

        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        alert.setContentText("Delete Successfully!");
        alert.showAndWait();
        readAllWords();
        searchInDelete();
    }

    public void resetSlangDictionary() throws IOException {
        Dictionary.loadData(Dictionary.DATA_RAW);
        saveDictionaryToFile();
        Alert alert;
        alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation Message");
        alert.setHeaderText(null);
        alert.setContentText("Do you want to reset dictionary?");
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(ButtonType.YES, ButtonType.NO, ButtonType.CANCEL);
        Optional<ButtonType> option = alert.showAndWait();
        if (option.get().equals(ButtonType.YES)) {
            Alert alert1;
            alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Information Message");
            alert1.setHeaderText(null);
            alert1.setContentText("Reset Dictionary Successfully!");
            alert1.showAndWait();
        }
        readAllWords();
    }

    public void getRandomWord() {
        List<String> listKey = new ArrayList<String>(Dictionary.getData().keySet());
        Random randSlang = new Random();
        String randomSlang = listKey.get(randSlang.nextInt(listKey.size()));
        Set<String> randomDefs = Dictionary.getData().get(randomSlang);
        Random randDef = new Random();
        String[] defArray = randomDefs.toArray(new String[0]);
        String randomDef = defArray[randDef.nextInt(defArray.length)];
        random_slang_field.setText(randomSlang);
        random_def_field.setText(randomDef);
    }

    void handleQuizForm(boolean visibleType) {
        quiz_header.setVisible(visibleType);
        quiz_slang_btn.setVisible(visibleType);
        quiz_def_btn.setVisible(visibleType);
    }

    public void getRandomSlangQuesAns() {
        List<String> listKey = new ArrayList<>(Dictionary.getData().keySet());
        Random randSlang = new Random();
        String randomSlang = listKey.get(randSlang.nextInt(listKey.size()));
        Set<String> randomDefs = Dictionary.getData().get(randomSlang);
        String[] defArray = randomDefs.toArray(new String[0]);
        String correctAns = defArray[new Random().nextInt(defArray.length)];
        quiz_slang_question.setText(randomSlang);

        List<String> listAns = new ArrayList<>();
        listAns.add(correctAns);

        for (int i = 1; i <= 3; i++) {
            Random randSlangDif = new Random();
            String randomSlangDif = listKey.get(randSlangDif.nextInt(listKey.size()));
            Set<String> randomDefsDif = Dictionary.getData().get(randomSlangDif);
            String[] defArrayDif = randomDefsDif.toArray(new String[0]);
            String randomDefDif = defArrayDif[new Random().nextInt(defArrayDif.length)];
            listAns.add(randomDefDif);
        }

        Collections.shuffle(listAns);
        quiz_slang_a.setText(listAns.get(0));
        quiz_slang_b.setText(listAns.get(1));
        quiz_slang_c.setText(listAns.get(2));
        quiz_slang_d.setText(listAns.get(3));

        quiz_slang_a.setOnAction(e -> checkAnswer(quiz_slang_a, correctAns));
        quiz_slang_b.setOnAction(e -> checkAnswer(quiz_slang_b, correctAns));
        quiz_slang_c.setOnAction(e -> checkAnswer(quiz_slang_c, correctAns));
        quiz_slang_d.setOnAction(e -> checkAnswer(quiz_slang_d, correctAns));
    }

    public void getRandomDefQuesAns() {
        List<String> listKey = new ArrayList<>(Dictionary.getData().keySet());
        Random randSlang = new Random();
        String correctAns = listKey.get(randSlang.nextInt(listKey.size()));
        Set<String> randomDefs = Dictionary.getData().get(correctAns);
        String[] defArray = randomDefs.toArray(new String[0]);
        String randomDef = defArray[new Random().nextInt(defArray.length)];
        quiz_def_question.setText(randomDef);

        List<String> listAns = new ArrayList<>();
        listAns.add(correctAns);

        for (int i = 1; i <= 3; i++) {
            Random randSlangDif = new Random();
            String randomSlangDif = listKey.get(randSlangDif.nextInt(listKey.size()));
            listAns.add(randomSlangDif);
        }

        Collections.shuffle(listAns);
        quiz_def_a.setText(listAns.get(0));
        quiz_def_b.setText(listAns.get(1));
        quiz_def_c.setText(listAns.get(2));
        quiz_def_d.setText(listAns.get(3));

        quiz_def_a.setOnAction(e -> checkAnswer(quiz_def_a, correctAns));
        quiz_def_b.setOnAction(e -> checkAnswer(quiz_def_b, correctAns));
        quiz_def_c.setOnAction(e -> checkAnswer(quiz_def_c, correctAns));
        quiz_def_d.setOnAction(e -> checkAnswer(quiz_def_d, correctAns));
    }


    void checkAnswer(Button clickedButton, String correctAnswer) {
        String buttonText = clickedButton.getText();
        Alert alert;
        alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Information Message");
        alert.setHeaderText(null);
        if (buttonText.equals(correctAnswer)) {
            alert.setContentText("Correct Answer");
        } else {
            alert.setContentText("Incorrect Answer");
        }
        alert.showAndWait();
    }

    public void showQuizSlang() {
        handleQuizForm(false);
        quiz_slang_form.setVisible(true);
        getRandomSlangQuesAns();
    }

    public void showQuizDef() {
        handleQuizForm(false);
        quiz_def_form.setVisible(true);
        getRandomDefQuesAns();
    }

    public void getSlangQuestion() {
        getRandomSlangQuesAns();
    }

    public void getDefQuestion() {
        getRandomDefQuesAns();
    }

    public void clearHistory() throws IOException {
        String historyFilePath = "assets/history.txt";  // Replace with the actual path to your history.txt file

        try {
            FileWriter fileWriter = new FileWriter(historyFilePath);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("");
            bufferedWriter.close();
            fileWriter.close();
        } catch (IOException ignored) {

        }
        readSearchHistoryData();
    }
}