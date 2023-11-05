package com.example.slangworddictionary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

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
    void switchForm(ActionEvent event) {
        if (event.getSource() == view_all_btn) {
            System.out.println("View all button");
        }
        else if (event.getSource() == find_def_btn) {
            System.out.println("Find word by definiton");
        }
    }
}
