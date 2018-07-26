package ro.amarkovits.opengldemo

import android.graphics.RectF
import android.opengl.GLES20
import android.util.Log
import pl.droidsonroids.gif.GifTexImage2D
import java.nio.Buffer

class Sticker(val gifTexImage2D: GifTexImage2D, val name: String) {

    val TAG = Sticker::class.java.simpleName

    val position = RectF(0f, 0f, gifTexImage2D.width.toFloat(), gifTexImage2D.height.toFloat())
    lateinit var verticesBuffer: Buffer
    var texName = 0

    init {
        gifTexImage2D.startDecoderThread()
        updateVerticesBuffer()
    }

    fun initialize() {
        createTexture()
        Log.d(TAG, "initialized $name texName=$texName")
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

    private fun createTexture(){
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        val texNames = intArrayOf(0)
        GLES20.glGenTextures(1, texNames, 0)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, texNames[0])
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE)
        texName = texNames[0]
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, gifTexImage2D.width, gifTexImage2D.height, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, null)
    }


}