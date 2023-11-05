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

import java.io.IOException;
import java.util.Set;

public class dashboardController {
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
    private TextField filter_def;

    public void initialize() {
        Dictionary dictionary = new Dictionary();
        try {
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

            filter_def.textProperty().addListener((observable, oldValue, newValue) -> {
                searchSlangByDef();
            });
        } catch (IOException e) {
            // Handle the exception appropriately
        }
    }

    @FXML
    void switchForm(ActionEvent event) {
        if (event.getSource() == view_all_btn) {
            view_all_form.setVisible(true);
            find_def_form.setVisible(false);
        }
        else if (event.getSource() == find_def_btn) {
            find_def_form.setVisible(true);
            view_all_form.setVisible(false);
        }
    }

    @FXML
    void searchSlangByDef() {
        String searchTerm = filter_def.getText().toLowerCase().trim();
        FilteredList<SlangDefinition> filteredData = new FilteredList<>(view_table.getItems());

        filteredData.setPredicate(slangDefinition -> {
            if (searchTerm.isEmpty()) {
                return true;
            }
            return slangDefinition.getDefinition().toLowerCase().contains(searchTerm);
        });

        filter_slang_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("slang"));
        filter_def_col.setCellValueFactory(new PropertyValueFactory<SlangDefinition, String>("definition"));
        filter_def_table.setItems(filteredData);
    }

}
