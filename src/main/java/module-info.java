module com.example.slangworddictionary {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.slangworddictionary to javafx.fxml;
    exports com.example.slangworddictionary;
}