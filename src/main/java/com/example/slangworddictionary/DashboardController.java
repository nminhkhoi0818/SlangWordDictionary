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

import java.io.*;
import java.time.LocalDateTime;
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

    // 8. Random slang word

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
    }

    void readAllWords() throws IOException {
        Dictionary dictionary = new Dictionary();
        dictionary.loadData(Dictionary.DATA_DIR);
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
        if (event.getSource() == view_all_btn) {
            view_all_form.setVisible(true);
            find_def_form.setVisible(false);
            find_slang_form.setVisible(false);
            search_history_form.setVisible(false);
            add_slang_form.setVisible(false);
            edit_form.setVisible(false);

            readAllWords();
        } else if (event.getSource() == find_def_btn) {
            find_def_form.setVisible(true);
            view_all_form.setVisible(false);
            find_slang_form.setVisible(false);
            search_history_form.setVisible(false);
            add_slang_form.setVisible(false);
            edit_form.setVisible(false);

            readAllWords();
        } else if (event.getSource() == find_slang_btn) {
            find_slang_form.setVisible(true);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
            search_history_form.setVisible(false);
            add_slang_form.setVisible(false);
            edit_form.setVisible(false);

            readAllWords();
        } else if (event.getSource() == search_history_btn) {
            search_history_form.setVisible(true);
            find_slang_form.setVisible(false);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
            add_slang_form.setVisible(false);
            edit_form.setVisible(false);

            readSearchHistoryData();
        } else if (event.getSource() == add_slang_btn) {
            add_slang_form.setVisible(true);
            search_history_form.setVisible(false);
            find_slang_form.setVisible(false);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
            edit_form.setVisible(false);
        } else if (event.getSource() == edit_word_btn) {
            edit_form.setVisible(true);
            add_slang_form.setVisible(false);
            search_history_form.setVisible(false);
            find_slang_form.setVisible(false);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
        }
    }

    void saveSearchHistoryToFile() {
        try {
            BufferedWriter bw = new BufferedWriter(new FileWriter("assets/history.txt", true));
            for (SearchHistoryEntry entry : searchHistory) {
                bw.write(entry.getSearchTerm() + " " + entry.getTimestamp() + "\n");
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

    private void search(TextField filterSlang, String filterType) {
        String searchTerm = filterSlang.getText().toLowerCase().trim();
        FilteredList<SlangDefinition> filteredData = new FilteredList<>(view_table.getItems());

        filteredData.setPredicate(slangDefinition -> {
            if (searchTerm.isEmpty()) {
                return true;
            }
            if (Objects.equals(filterType, "definition"))
                return slangDefinition.getDefinition().toLowerCase().contains(searchTerm);
            else
                return slangDefinition.getSlang().toLowerCase().contains(searchTerm);
        });

        if (Objects.equals(filterType, "definition")) {
            filter_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
            filter_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));
            filter_def_table.setItems(filteredData);
        }
        else if (Objects.equals(filterType, "slang")) {
            filter_slang_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
            filter_slang_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));
            filter_slang_table.setItems(filteredData);
        }
        else {
            edit_slang_slang_col.setCellValueFactory(new PropertyValueFactory<>("slang"));
            edit_slang_def_col.setCellValueFactory(new PropertyValueFactory<>("definition"));
            edit_table.setItems(filteredData);
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

                } else if (option.get().equals(ButtonType.NO)) {
                    addSlangToDictionary(slang, definition);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Duplicated!");
                    alert.showAndWait();
                }
            }
            else {
                addSlangToDictionary(slang, definition);
                Alert alert;
                alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Information Message");
                alert.setHeaderText(null);
                alert.setContentText("Successfully Added!");
                alert.showAndWait();
            }

        }
    }

    public void getItem() {
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
                if (!validateDialog()) {
                    event.consume();
                }
            }

            private boolean validateDialog() {
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
}