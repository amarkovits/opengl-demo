package ro.amarkovits.opengldemo

import android.content.res.Resources
import android.graphics.RectF
import android.util.Log
import pl.droidsonroids.gif.GifOptions
import pl.droidsonroids.gif.GifTexImage2D
import pl.droidsonroids.gif.InputSource

class Sticker(resources: Resources, gifId: Int, var x: Float, var y: Float) {

    val TAG = Sticker::class.java.simpleName

    lateinit var gifTexImage2D: GifTexImage2D
    lateinit var gifTexImage2DProgram: GifTexImage2DProgram
    lateinit var position: RectF

    init {
        gifTexImage2D = GifTexImage2D(InputSource.ResourcesSource(resources, gifId), GifOptions())
        gifTexImage2D.startDecoderThread()
        gifTexImage2DProgram = GifTexImage2DProgram(gifTexImage2D)
        position = RectF(0f, 0f, gifTexImage2D.width.toFloat(), gifTexImage2D.height.toFloat())
    }

    fun initialize() {
        Log.d(TAG, "initializer")
        gifTexImage2DProgram.initialize()
    }

    fun draw(projectionMatrix: FloatArray) {
        gifTexImage2DProgram.draw(projectionMatrix, floatArrayOf(position.left, position.bottom, position.right, position.bottom, position.left, position.top, position.right, position.top).toFloatBuffer())
    }

    fun isSelectable(x: Float, y: Float): Boolean {
        return position.contains(x, y)
    }

    fun translate(dx: Float, dy: Float) {
        position.set(position.left + dx, position.top + dy, position.right + dx, position.bottom + dy)
    }

    //set the position of the sticker on the screen (x,y are the center of the sticker)
    fun setCenter(cx: Float, cy: Float) {
        val width = gifTexImage2D.width.toFloat()
        val height = gifTexImage2D.height.toFloat()
        position.set(cx - width / 2, cy - height / 2, cx + width / 2, cy + height / 2)
    }

}