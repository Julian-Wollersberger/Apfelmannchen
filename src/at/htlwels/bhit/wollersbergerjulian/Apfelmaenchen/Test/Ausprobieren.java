package at.htlwels.bhit.wollersbergerjulian.Apfelmaenchen.Test;

import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.layout.BorderPane;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * Created by julian on 11.07.17.
 */
public class Ausprobieren {

    public void foo() {
        // https://github.com/DiegoCatalano/Catalano-Framework/blob/master/Catalano.Image/src/Catalano/Imaging/FastBitmap.java
        BufferedImage bufferedImage = new BufferedImage(100, 100, BufferedImage.TYPE_4BYTE_ABGR);
        bufferedImage.createGraphics();

        WritableImage image = new WritableImage(100, 100);
        //image.getPixelWriter().setArgb();
        ImageView view = new ImageView();
        //image.getPixelReader().getPixels();

        //ImageIO.write(bufferedImage, "PNG", new File("beides.png"));

        //https://stackoverflow.com/questions/21540378/convert-javafx-image-to-bufferedimage#21540615

        //SwingFXUtils.fromFXImage();

        //InvalidationListener imageChangeListener

        //BorderPane
    }
}
