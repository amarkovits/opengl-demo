package ro.amarkovits.opengldemo

import android.graphics.PointF
import android.opengl.GLES20
import javax.microedition.khronos.opengles.GL10
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import javax.microedition.khronos.egl.EGLConfig
import android.opengl.Matrix.orthoM
import android.util.Log
import java.nio.ByteBuffer
import java.util.*


class MyGLRenderer : GLSurfaceView.Renderer {

    val TAG = MyGLRenderer::class.java.simpleName

    private val mProjectionMatrix = FloatArray(16)

    private val stickers = ArrayList<Sticker>()
    private var selectedSticker: Sticker? = null

    private val touchStartPoint = PointF(0f, 0f)
    private var minMovement = 1f

    private var viewPortHeight = 0

    private val gifTextImage2DRenderer = GitTextImage2DRenderer()

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

        gifTextImage2DRenderer.initialize()

        stickers.forEach {
            it.initialize(mProjectionMatrix)
        }
    }

    override fun onDrawFrame(unused: GL10) {
        // Redraw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)

        stickers.forEach {
            gifTextImage2DRenderer.draw(it.gifTexImage2D, it.texName, it.mvpMatrix, it.verticesBuffer)
        }

    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Log.d(TAG, "onSurfaceChanged $width $height")
        viewPortHeight = height

        GLES20.glViewport(0, 0, width, height)

        orthoM(mProjectionMatrix, 0, 0f, width.toFloat(), height.toFloat(), 0f, -1f, 1f)

        //when the surface is changed put the stickers randomly on the screen
        val random = Random()
        stickers.forEach {
            it.resetPosition()
            it.translate(random.nextInt(width*8/10).toFloat(), random.nextInt(height*8/10).toFloat())
        }

        //avoid very small drag of objects
        minMovement = width.toFloat() / 300f

        Log.d(TAG, "mProjectionMatrix ${Arrays.toString(mProjectionMatrix)}")

    }

    fun onTouchDown(x: Float, y: Float) {
        Log.d(TAG, "onTouchDown $x $y")

        //check for transparency
        val buffer = ByteBuffer.allocate(4)
        GLES20.glReadPixels(x.toInt(), viewPortHeight - y.toInt(), 1, 1, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer)
        Log.d(TAG, "color: ${buffer[0]} ${buffer[1]} ${buffer[2]} ${buffer[3]}")

        if ((buffer[0].toInt() != 127 || buffer[1].toInt() != 127 || buffer[2].toInt() != 127)) {
            selectedSticker = stickers.asReversed().find {
                it.isSelectable(x, y)
            }
            if (selectedSticker != null) {
                Log.d(TAG, "selectedSticker=${selectedSticker?.name}")
                //bring the selected sticker to the front
                stickers.remove(selectedSticker!!)
                stickers.add(selectedSticker!!)
                //keep the touch start position to calculate translation
                touchStartPoint.set(x, y)
            }
        } else {
            Log.d(TAG, "transparent zone")
        }
    }

    fun onTouchMove(x: Float, y: Float) {
//        Log.d(TAG, "onTouchMove $x $y")
        if (selectedSticker != null) {
            val dx = x - touchStartPoint.x
            val dy = y - touchStartPoint.y
            if (Math.abs(dx) > minMovement || Math.abs(dy) > minMovement) {
                selectedSticker?.translate(dx, dy)
                //update touch start position so new translation is relative to this point
                touchStartPoint.set(x, y)
            } else {
                Log.d(TAG, "insignificant movement $dx $dy")
            }
        }
    }

    fun onTouchUp(x: Float, y: Float) {
        Log.d(TAG, "onTouchUp $x $y")
        selectedSticker = null
    }


}