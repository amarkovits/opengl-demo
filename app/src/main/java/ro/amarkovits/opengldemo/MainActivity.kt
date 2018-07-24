package ro.amarkovits.opengldemo

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.opengl.GLSurfaceView


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
        mRenderer.addSticker(GifTexImage2DObject(resources, R.drawable.sticker6))

        // Set the Renderer for drawing on the GLSurfaceView
        setRenderer(mRenderer)

//        renderMode = GLSurfaceView.RENDERMODE_WHEN_DIRTY
    }
}