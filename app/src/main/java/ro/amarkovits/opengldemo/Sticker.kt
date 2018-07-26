package ro.amarkovits.opengldemo

import android.graphics.RectF
import android.opengl.GLES20
import android.opengl.Matrix
import android.util.Log
import pl.droidsonroids.gif.GifTexImage2D
import java.nio.Buffer

class Sticker(val gifTexImage2D: GifTexImage2D, val name: String) {

    val TAG = Sticker::class.java.simpleName

    val verticesBuffer = floatArrayOf(0f, gifTexImage2D.height.toFloat(), gifTexImage2D.width.toFloat(), gifTexImage2D.height.toFloat(), 0f, 0f, gifTexImage2D.width.toFloat(), 0f).toFloatBuffer()
    var texName = 0
    val translationMatrix = FloatArray(16)

    //only used to check if the touch is inside the sticker
    val form = RectF(0f, 0f, gifTexImage2D.width.toFloat(), gifTexImage2D.height.toFloat())

    //preallocated structures so we don't allocate arrats and matrices every time
    private val inverseMatrix = FloatArray(16)
    private val inversePosition = FloatArray(4)
    private val touchPosition = floatArrayOf(0f, 0f, 0f, 1f)

    init {
        gifTexImage2D.startDecoderThread()
        Matrix.setIdentityM(translationMatrix, 0)
    }

    fun initialize() {
        createTexture()
        Log.d(TAG, "initialized $name texName=$texName")
    }

    fun isSelectable(x: Float, y: Float): Boolean {
        touchPosition[0] = x
        touchPosition[1] = y
        Matrix.invertM(inverseMatrix, 0, translationMatrix, 0)
        Matrix.multiplyMV(inversePosition, 0, inverseMatrix, 0, touchPosition, 0)
        return form.contains(inversePosition[0], inversePosition[1])
    }

    fun translate(dx: Float, dy: Float) {
        Log.d(TAG, "translatex $dx $dy")
        Matrix.translateM(translationMatrix, 0, dx, dy, 0f)
    }

    fun resetPosition() {
        Matrix.setIdentityM(translationMatrix, 0)
    }

    private fun createTexture() {
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