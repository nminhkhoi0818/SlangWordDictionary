package com.example.slangworddictionary;

import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.TextField;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class DashboardController {
    @FXML
    private Button add_word_btn;

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
    private AnchorPane view_all_form;

    @FXML
    private AnchorPane find_def_form;

    @FXML
    private AnchorPane find_slang_form;

    @FXML
    private AnchorPane search_history_form;

    @FXML
    private TableView<SlangDefinition> view_table;

    @FXML
    private TableColumn<SlangDefinition, String> view_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> view_slang_col;

    @FXML
    private TableColumn<SlangDefinition, String> view_index_col;

    @FXML
    private TableView<SlangDefinition> filter_def_table;

    @FXML
    private TableColumn<SlangDefinition, String> filter_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_index_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_col;

    @FXML
    private TableView<SlangDefinition> filter_slang_table;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_def_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_index_col;

    @FXML
    private TableColumn<SlangDefinition, String> filter_slang_slang_col;

    @FXML
    private TextField filter_def;

    @FXML
    private TextField filter_slang;

    @FXML
    private TableView<SearchHistoryEntry> search_history_table;

    @FXML
    private TableColumn<SearchHistoryEntry, String> word_history_col;

    @FXML
    private TableColumn<SlangDefinition, LocalDateTime> time_history_col;

    private List<SearchHistoryEntry> searchHistory = new ArrayList<>();

    public void initialize() {
        Dictionary dictionary = new Dictionary();
        try {
            // Set up for view all words
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

            // Add filter for search
            filter_def.textProperty().addListener((observable, oldValue, newValue) -> {
                searchSlangByDef();
            });

            filter_slang.textProperty().addListener((observable, oldValue, newValue) -> {
                searchDefBySlang();
            });

            // Read search history data
            ObservableList<SearchHistoryEntry> historyData = FXCollections.observableArrayList();
            readSearchHistoryFromFile(historyData);
            word_history_col.setCellValueFactory(new PropertyValueFactory<>("searchTerm"));
            time_history_col.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
            search_history_table.setItems(historyData);
        } catch (IOException e) {

        }
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
    void switchForm(ActionEvent event) {
        if (event.getSource() == view_all_btn) {
            view_all_form.setVisible(true);
            find_def_form.setVisible(false);
            find_slang_form.setVisible(false);
            search_history_form.setVisible(false);
        } else if (event.getSource() == find_def_btn) {
            find_def_form.setVisible(true);
            view_all_form.setVisible(false);
            find_slang_form.setVisible(false);
            search_history_form.setVisible(false);
        } else if (event.getSource() == find_slang_btn) {
            find_slang_form.setVisible(true);
            view_all_form.setVisible(false);
            find_def_form.setVisible(false);
            search_history_form.setVisible(false);
        } else if (event.getSource() == search_history_btn) {
            search_history_form.setVisible(true);
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
    void searchSlangByDef() {
        search(filter_def, "definition");
    }

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
}