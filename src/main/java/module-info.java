module cabbit24.glassnote {
    requires transitive javafx.controls;
    requires transitive javafx.fxml;
	requires org.fxmisc.richtext;
	requires javafx.graphics;
	requires java.desktop;

    opens cabbit24.glassnote to javafx.fxml;
    exports cabbit24.glassnote;
}
