package ro.amarkovits.opengldemo

import android.graphics.PointF
import android.opengl.GLES20
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.Matrix.orthoM
import android.util.Log
import java.util.*


class MyGLRenderer : GLSurfaceView.Renderer {

    val TAG = MyGLRenderer::class.java.simpleName

    private val mMVPMatrix = FloatArray(16)
    private val mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)

    private val stickers = ArrayList<Sticker>()
    private var selectedSticker: Sticker? = null

    private val touchStartPoint = PointF(0f, 0f)

    private var minMovement = 1f

    fun addSticker(sticker: Sticker) {
        stickers.add(sticker)
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // Set the background frame color
        GLES20.glClearColor(0.5f, 0.5f, 0.5f, 1.0f)
        //Blend settings, alpha not working otherwise
        GLES20.glEnable(GLES20.GL_BLEND)
        GLES20.glBlendFuncSeparate(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA, GLES20.GL_ONE, GLES20.GL_ZERO)
        GLES20.glBlendEquation(GLES20.GL_FUNC_ADD)

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

        orthoM(mProjectionMatrix, 0, 0f, width.toFloat(), height.toFloat(), 0f, -1f, 1f)

        //when the surface is changed put the stickers randomly on the screen
        val random = Random()
        stickers.forEach {
            it.setCenter(random.nextInt(width).toFloat(), random.nextInt(height).toFloat())
        }

        //avoid very small drag of objects
        minMovement = width.toFloat() / 300f

        Log.d(TAG, "mProjectionMatrix ${Arrays.toString(mProjectionMatrix)}")

    }

    fun onTouchDown(x: Float, y: Float) {
        Log.d(TAG, "onTouchDown $x $y")
        selectedSticker = stickers.asReversed().find {
            it.isSelectable(x, y)
        }
        if (selectedSticker != null) {
            //bring the selected sticker to the front
            stickers.remove(selectedSticker!!)
            stickers.add(selectedSticker!!)
            //keep the touch start position to calculate translation
            touchStartPoint.set(x, y)
        }
    }

    fun onTouchMove(x: Float, y: Float) {
        Log.d(TAG, "onTouchMove $x $y")
        if (selectedSticker != null) {
            val dx = x - touchStartPoint.x
            val dy = y - touchStartPoint.y
            if (Math.abs(dx) > minMovement && Math.abs(dy)> minMovement) {
                selectedSticker?.translate(dx, dy)
                //update touch start position so new translation is relative to this point
                touchStartPoint.set(x, y)
            }else{
                Log.d(TAG, "insignificant movement $dx $dy")
            }
        }
    }

    fun onTouchUp(x: Float, y: Float) {
        Log.d(TAG, "onTouchUp $x $y")
        selectedSticker = null
    }


}