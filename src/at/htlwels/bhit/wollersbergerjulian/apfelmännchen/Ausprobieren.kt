package at.htlwels.bhit.wollersbergerjulian.apfelmännchen

import javafx.scene.image.WritableImage
import java.awt.Image
import java.awt.image.BufferedImage
import java.awt.image.MemoryImageSource
import com.sun.deploy.uitoolkit.impl.awt.Applet2ImageFactory.createImage
import java.util.*
import com.oracle.util.Checksums.update
import javafx.application.Platform
import java.util.TimerTask
import java.util.concurrent.ScheduledThreadPoolExecutor


/**
 * Created by julian on 24.07.17.
 *
 * Ein bisschen ausprobieren :)
 */

fun main(args: Array<String>) {
    print(Double.NaN + 1.0)
    // gibt NaN aus.

    //val writeImage = WritableImage(10, 10)
    //val bufferedImage = BufferedImage(10, 10, BufferedImage.TYPE_INT_ARGB)

    //bufferedImage.getScaledInstance()

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
}

