package ro.amarkovits.opengldemo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.opengl.GLSurfaceView
import android.support.v4.view.MotionEventCompat
import android.view.MotionEvent


class MainActivity : AppCompatActivity() {

    private var mGLView: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGLView = MyGLSurfaceView(this)
        setContentView(mGLView)

    }
}

internal class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {

    private val mRenderer: MyGLRenderer

    init {

        // Create an OpenGL ES 2.0 context
        setEGLContextClientVersion(2)

        mRenderer = MyGLRenderer()
        mRenderer.addSticker(Sticker(resources, R.drawable.sticker6, 0.2f, 0.2f))
        mRenderer.addSticker(Sticker(resources, R.drawable.sticker1, 0.7f, 0.7f))

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer)

//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.actionMasked
        when(action){
            MotionEvent.ACTION_DOWN->{
                mRenderer.onTouchDown(event.x, event.y)
            }
            MotionEvent.ACTION_MOVE->{
                mRenderer.onTouchMove(event.x, event.y)
            }
            MotionEvent.ACTION_UP->{
                mRenderer.onTouchUp(event.x, event.y)
            }
        }
        return super.onTouchEvent(event)
    }
}