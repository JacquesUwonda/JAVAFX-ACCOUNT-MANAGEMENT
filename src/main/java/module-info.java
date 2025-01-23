module com.example.comptebank {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.comptebank to javafx.fxml;
    opens com.example.comptebank.Controllers to javafx.fxml;
    exports com.example.comptebank;
    exports com.example.comptebank.Controllers;
}