package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.debug;

import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.view.MainApp;
import at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.view.ZeichenFensterController;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Created by julian on 18.07.17.
 */
public class DebugFensterController implements Initializable {

    private MainApp mainApp;
    private @FXML GridPane layoutInfoGridPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        new java.util.Formatter();
    }

    public void fülleGridPane(Parent zeichenFensterRootLayout) {

        Consumer<Node> consumer = new Consumer<Node>() {
            @Override
            public void accept(Node node) {
                if(node instanceof Pane) {
                    Pane pane = (Pane) node;

                    // Hier kommt überall NEGATIVE_INFINITY raus.
                    /*Label minWidthPropertylabel = new Label();
                    minWidthPropertylabel.textProperty().bind(pane.minWidthProperty().asString("%+4.2f"));
                    Label minHeightPropertylabel = new Label();
                    minHeightPropertylabel.textProperty().bind(pane.minHeightProperty().asString("%+4.2f"));
                    Label prefWidthPropertylabel = new Label();
                    prefWidthPropertylabel.textProperty().bind(pane.prefWidthProperty().asString("%+4.2f"));
                    Label prefHeightPropertylabel = new Label();
                    prefHeightPropertylabel.textProperty().bind(pane.prefHeightProperty().asString("%+4.2f"));
                    Label maxWidthPropertylabel = new Label();
                    maxWidthPropertylabel.textProperty().bind(pane.maxWidthProperty().asString("%+4.2f"));
                    Label maxHeightPropertylabel = new Label();
                    maxHeightPropertylabel.textProperty().bind(pane.maxHeightProperty().asString("%+4.2f"));*/

                    Label layoutBoundsLabel = new Label();
                    layoutBoundsLabel.textProperty().bind(node.layoutBoundsProperty().asString());

                    // Neue Row
                    layoutInfoGridPane.addRow(layoutInfoGridPane.impl_getRowCount(),
                            new Label(node.getClass().getSimpleName() + " (" + ((node.getParent() != null) ? node.getParent().getClass().getSimpleName() : "")),
                            /*minWidthPropertylabel,
                            minHeightPropertylabel,
                            prefWidthPropertylabel,
                            prefHeightPropertylabel,
                            maxWidthPropertylabel,
                            maxHeightPropertylabel,*/
                            layoutBoundsLabel
                    );

                    // Das selbe für alle Kinder

                    //noinspection TrivialMethodReference
                    ((Parent) node).getChildrenUnmodifiable().forEach(this::accept);
                }
            }
        };

        consumer.accept(zeichenFensterRootLayout);
        //zeichenFensterRootLayout.getChildrenUnmodifiable().forEach(consumer);
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
