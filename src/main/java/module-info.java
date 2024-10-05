/**
 * 这个类是：
 *
 * @author: William
 * @date: 2024-10-05T16:05:28CST 16:05
 * @description:
 */
module untitled {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;

    opens com.scau.cfd to javafx.fxml;
    exports com.scau.cfd;
}