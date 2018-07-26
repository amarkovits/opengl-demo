package ro.amarkovits.opengldemo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.opengl.GLSurfaceView
import android.support.v4.view.MotionEventCompat
import android.view.MotionEvent
import android.view.View
import pl.droidsonroids.gif.GifDrawable
import pl.droidsonroids.gif.GifOptions
import pl.droidsonroids.gif.GifTexImage2D
import pl.droidsonroids.gif.InputSource


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
        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker1), GifOptions()), GifDrawable(resources, R.drawable.sticker1), "sticker1"))
        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker2), GifOptions()), GifDrawable(resources, R.drawable.sticker2),"sticker2"))
        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker3), GifOptions()), GifDrawable(resources, R.drawable.sticker3),"sticker3"))
        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker4), GifOptions()), GifDrawable(resources, R.drawable.sticker4),"sticker4"))
        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker5), GifOptions()), GifDrawable(resources, R.drawable.sticker5),"sticker5"))

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer)

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.actionMasked
        val screenLocation = intArrayOf(0, 0)
        getLocationOnScreen(screenLocation)
        when (action) {
            MotionEvent.ACTION_DOWN -> {
                queueEvent {
                    mRenderer.onTouchDown(event.x-screenLocation[0], event.y-screenLocation[1])
                }
            }
            MotionEvent.ACTION_MOVE -> {
                queueEvent {
                    mRenderer.onTouchMove(event.x-screenLocation[0], event.y-screenLocation[1])
                }
            }
            MotionEvent.ACTION_UP -> {
                queueEvent {
                    mRenderer.onTouchUp(event.x-screenLocation[0], event.y-screenLocation[1])
                }
            }
        }
        return true
    }

}