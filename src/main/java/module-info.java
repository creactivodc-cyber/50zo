module com.univalle._0zo {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.univalle._0zo to javafx.fxml;
    exports com.univalle._0zo;

    opens com.univalle._0zo.view to javafx.fxml;
    exports com.univalle._0zo.view;

    opens com.univalle._0zo.controller to javafx.fxml;
    exports com.univalle._0zo.controller;

    opens com.univalle._0zo.model to javafx.fxml;
    exports com.univalle._0zo.model;
}