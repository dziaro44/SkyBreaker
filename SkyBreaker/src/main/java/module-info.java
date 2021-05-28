module SkyBreaker {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens Skybreaker to javafx.fxml;
    exports Skybreaker;
}