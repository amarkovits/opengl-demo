package ro.amarkovits.opengldemo

import android.content.res.Resources
import android.graphics.RectF
import android.util.Log
import pl.droidsonroids.gif.GifOptions
import pl.droidsonroids.gif.GifTexImage2D
import pl.droidsonroids.gif.InputSource
import java.nio.Buffer

class Sticker(val gifTexImage2D: GifTexImage2D, val name: String) {

    val TAG = Sticker::class.java.simpleName

    val gifTexImage2DProgram = GifTexImage2DProgram(gifTexImage2D)
    val position = RectF(0f, 0f, gifTexImage2D.width.toFloat(), gifTexImage2D.height.toFloat())
    lateinit var verticesBuffer: Buffer

    init {
        gifTexImage2D.startDecoderThread()
        updateVerticesBuffer()
    }

    fun initialize() {
        Log.d(TAG, "initializer")
        gifTexImage2DProgram.initialize()
    }

    fun draw(projectionMatrix: FloatArray) {
        gifTexImage2DProgram.draw(projectionMatrix, verticesBuffer)
    }

    fun isSelectable(x: Float, y: Float): Boolean {
        return position.contains(x, y)
    }

    fun translate(dx: Float, dy: Float) {
        Log.d(TAG, "translatex $dx $dy")
        position.set(position.left + dx, position.top + dy, position.right + dx, position.bottom + dy)
        updateVerticesBuffer()
    }

    //set the position of the sticker on the screen (x,y are the center of the sticker)
    fun setCenter(cx: Float, cy: Float) {
        val width = gifTexImage2D.width.toFloat()
        val height = gifTexImage2D.height.toFloat()
        position.set(cx - width / 2, cy - height / 2, cx + width / 2, cy + height / 2)
        updateVerticesBuffer()
    }

    private fun updateVerticesBuffer(){
        verticesBuffer = floatArrayOf(position.left, position.bottom, position.right, position.bottom, position.left, position.top, position.right, position.top).toFloatBuffer()
    }

}