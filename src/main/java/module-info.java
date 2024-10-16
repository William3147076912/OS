/**
 * 这个类是：
 *
 * @author: William
 * @date: 2024-10-05T16:05:28CST 16:05
 * @description:
 */
module untitled {
    requires javafx.fxml;
    requires static lombok;
    requires rxcontrols;
    requires org.kordamp.ikonli.javafx;
    requires javafx.controls;
    requires org.kordamp.ikonli.ionicons;
    requires org.kordamp.ikonli.core;
    requires java.xml;
    opens com.scau.cfd to javafx.fxml;
    opens com.scau.cfd.controller to javafx.fxml;
    exports com.scau.cfd;
    exports com.scau.cfd.william_test to javafx.graphics;
}