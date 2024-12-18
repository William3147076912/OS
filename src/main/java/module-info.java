/**
 * 这个类是：
 *
 * @author: William
 * @date: 2024-10-05T16:05:28CST 16:05
 * @description:
 */
module simulated.disk.file.system.implementation {
    requires org.kordamp.ikonli.core;
    requires javafx.fxml;
    requires static lombok;
    requires rxcontrols;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.ionicons;
    requires com.googlecode.ezvcard;
    requires org.kordamp.ikonli.antdesignicons;
    requires io.vproxy.vfx;
    requires javafx.controls;
    requires org.jetbrains.annotations;
    requires guice;
    requires io.vproxy.base;
    opens com.scau.cfd.controller to javafx.fxml;
    exports com.scau.cfd.app to javafx.graphics;
    exports com.scau.cfd.manage;
    opens com.scau.cfd.manage to javafx.fxml;
}