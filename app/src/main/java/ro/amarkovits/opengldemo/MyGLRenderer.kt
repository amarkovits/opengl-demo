package ro.amarkovits.opengldemo

import android.opengl.GLES20
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.Matrix.frustumM
import android.opengl.Matrix.orthoM
import android.util.Log
import java.util.*


class MyGLRenderer : GLSurfaceView.Renderer {

    val TAG = MyGLRenderer::class.java.simpleName

    private val mMVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private val stickers = ArrayList<GifTexImage2DObject>()

    fun addSticker(sticker: GifTexImage2DObject){
        stickers.add(sticker)
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)

        stickers.forEach {
            it.initialize()
        }
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

//        // Set the camera position (View matrix)
//        Matrix.setLookAtM(mViewMatrix, 0, 0f, 0f, -3f, 0f, 0f, 0f, 0f, 1.0f, 0.0f)
//
//        // Calculate the projection and view transformation
//        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        stickers.forEach {
            it.draw(mProjectionMatrix)
        }

    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        GLES20.glViewport(0, 0, width, height)

        val aspectRatio = if (width > height)
            width.toFloat() / height.toFloat()
        else
            height.toFloat()/ width.toFloat()

        if (width > height) {
            // Landscape
            orthoM(mProjectionMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, -1f, 1f)
        } else {
            // Portrait or square
            orthoM(mProjectionMatrix, 0, -1f, 1f, -aspectRatio, aspectRatio, -1f, 1f)
        }

        Log.d(TAG, "mProjectionMatrix ${Arrays.toString(mProjectionMatrix)}")

    }
}