/*
 * Gui with draggable points. Loads the javascript file in web/index.html
 */

package org.anchoredrectangles;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.web.WebView;
import javafx.scene.web.WebEngine;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import org.w3c.dom.Document;
import netscape.javascript.JSObject;

public class DraggableGui extends Application {

    private Parent createContent() {
        WebView browser = new WebView();
        WebEngine webEngine = browser.getEngine();
        try {
            String content = new String(Files.readAllBytes(Paths.get("../web/index.html")));
            webEngine.loadContent(content);
        } catch(IOException e) {}
        JsInterface jsi = this.jsi;
        webEngine.documentProperty().addListener(new ChangeListener<Document>() {
        @Override public void changed(ObservableValue<? extends Document> prop, Document oldDoc, Document newDoc) {
            JSObject window = (JSObject) webEngine.executeScript("window");
            window.setMember("java", jsi);
        }
        });
        return new StackPane(browser);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.jsi = new JsInterface(stage);
        stage.setTitle("Anchored Rectangles");
        stage.setScene(new Scene(createContent(), 300, 300));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    JsInterface jsi;
}

