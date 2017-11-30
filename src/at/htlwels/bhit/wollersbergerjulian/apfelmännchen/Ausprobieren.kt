package at.htlwels.bhit.wollersbergerjulian.apfelmännchen

import at.htlwels.bhit.wollersbergerjulian.apfelmännchen.zeichnen.ZeichenRegion
import javafx.scene.image.WritableImage
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource
import com.sun.deploy.uitoolkit.impl.awt.Applet2ImageFactory.createImage
import java.util.*
import com.oracle.util.Checksums.update
import javafx.animation.AnimationTimer
import javafx.application.Platform
import javafx.event.Event
import javafx.scene.image.ImageView
import javafx.scene.input.DragEvent
import java.io.File
import java.io.PipedInputStream
import java.io.PipedOutputStream
import java.util.TimerTask
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.ScheduledThreadPoolExecutor
import javax.imageio.ImageIO


/**
 * Created by julian on 24.07.17.
 *
 * Ein bisschen ausprobieren :)
 */

fun main(args: Array<String>) {
    print(Double.NaN + 1.0)
    // gibt NaN aus.

    val writeImage = WritableImage(10, 10)
    val bufferedImage = BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)

    //bufferedImage.getScaledInstance()
    //Image().graphics.
    //Image().getScaledInstance()
    javafx.embed.swing.SwingFXUtils.fromFXImage(writeImage, null)

    ImageIO.write(bufferedImage, "PNG", File("beides.png"));

    //stackoverflow.com/questions/16764549/timers-and-javafx#18654916
    val timer = java.util.Timer()

    timer.schedule(object : TimerTask() {
        override fun run() {
            Platform.runLater(Runnable {
                //label.update()
                //javafxcomponent.doSomething()
            })
        }
    }, 1, 1)

    ScheduledThreadPoolExecutor(1).queue.isEmpty()

    val pis = PipedInputStream()
    pis.connect(PipedOutputStream())
    pis.read()

    ConcurrentLinkedQueue<String>()

    // https@ //www.toptal.com/opengl/opengl-tutorial-for-android-building-a-mandelbrot-set-generator
    // https://github.com/skanti/Gaussian-Filter-GPU-OpenGL
}

fun drag(event: DragEvent) {
    // Rellativ zu Event-Source
    event.x
}

