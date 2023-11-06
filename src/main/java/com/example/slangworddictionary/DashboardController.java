package com.example.slangworddictionary;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;

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

            readAllWords();
        } else if (event.getSource() == find_def_btn) {
            find_def_form.setVisible(true);
            view_all_form.setVisible(false);
            find_slang_form.setVisible(false);
            search_history_form.setVisible(false);
            add_slang_form.setVisible(false);

            readAllWords();
        } else if (event.getSource() == find_slang_btn) {
            find_slang_form.setVisible(true);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
            search_history_form.setVisible(false);
            add_slang_form.setVisible(false);

            readAllWords();
        } else if (event.getSource() == search_history_btn) {
            search_history_form.setVisible(true);
            find_slang_form.setVisible(false);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
            add_slang_form.setVisible(false);

            readSearchHistoryData();
        } else if (event.getSource() == add_slang_btn) {
            add_slang_form.setVisible(true);
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
        else {
            filter_slang_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
            filter_slang_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));
            filter_slang_table.setItems(filteredData);
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
}