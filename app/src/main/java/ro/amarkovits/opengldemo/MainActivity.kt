package ro.amarkovits.opengldemo

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import com.giphy.sdk.creation.camera.CameraController
import com.giphy.sdk.creation.hardware.CameraView
import com.giphy.sdk.creation.model.Filter
import com.giphy.sdk.creation.model.MediaBundle


class MainActivity : AppCompatActivity() {

//    private var mGLView: GLSurfaceView? = null

    private var cameraView: CameraView? = null
    private var cameraController: CameraController? = null
    private var mediaBundle: MediaBundle? = null

    var filter = Filter.NONE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        mGLView = MyGLSurfaceView(this)

        cameraView = CameraView(this, null)

        cameraView?.setOnTouchListener { v, event ->
            cameraController?.handleFilterTouchEvent(v, event)
            true
        }

        cameraController = CameraController(cameraView!!)

        mediaBundle = MediaBundle.PickedVideoMediaBundle(Uri.parse("file:///android_asset/video.mp4"), cameraController!!)
//        mediaBundle = MediaBundle.ImageMediaBundle(BitmapInfo(BitmapFactory.decodeResource(resources, R.drawable.gmi8l18), 270f), cameraController!!)
        mediaBundle?.playMedia()


//        cameraController?.addSticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker1), GifOptions()), Uri.EMPTY)
//        cameraController?.addSticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker2), GifOptions()), Uri.EMPTY)

//        cameraView?.setOnClickListener {
//            filter = when (filter) {
//                Filter.NONE -> Filter.GEO
////                Filter.POP -> Filter.CRYSTAL
////                Filter.CRYSTAL -> Filter.FILM
////                Filter.FILM -> Filter.GEO
////                Filter.GEO -> Filter.RAINBOW
////                Filter.RAINBOW -> Filter.BARREL
//                else -> Filter.NONE
//            }
//            cameraController?.applyFilter(filter)
//        }

        setContentView(cameraView)

        cameraController?.applyFilter(Filter.GEO)

        Handler().postDelayed({

        }, 5000)
    }
}


//internal class MyGLSurfaceView(context: Context) : GLSurfaceView(context) {
//
//    private val mRenderer: MyGLRenderer
//
//    init {
//
//        // Create an OpenGL ES 2.0 context
//        setEGLContextClientVersion(2)
//
//        mRenderer = MyGLRenderer()
//        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker1), GifDrawable(resources, R.drawable.sticker1), "sticker1"))
//        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker2), GifOptions()), GifDrawable(resources, R.drawable.sticker2),"sticker2"))
//        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker3), GifOptions()), GifDrawable(resources, R.drawable.sticker3),"sticker3"))
//        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker4), GifOptions()), GifDrawable(resources, R.drawable.sticker4),"sticker4"))
//        mRenderer.addSticker(Sticker(GifTexImage2D(InputSource.ResourcesSource(resources, R.drawable.sticker5), GifOptions()), GifDrawable(resources, R.drawable.sticker5),"sticker5"))
//
//        // Set the Renderer for drawing on the GLSurfaceView
//        setRenderer(mRenderer)
//
//    }
//
//    override fun onTouchEvent(event: MotionEvent?): Boolean {
//        val action = event?.actionMasked
//        val screenLocation = intArrayOf(0, 0)
//        getLocationOnScreen(screenLocation)
//        when (action) {
//            MotionEvent.ACTION_DOWN -> {
//                queueEvent {
//                    mRenderer.onTouchDown(event.x-screenLocation[0], event.y-screenLocation[1])
//                }
//            }
//            MotionEvent.ACTION_MOVE -> {
//                queueEvent {
//                    mRenderer.onTouchMove(event.x-screenLocation[0], event.y-screenLocation[1])
//                }
//            }
//            MotionEvent.ACTION_UP -> {
//                queueEvent {
//                    mRenderer.onTouchUp(event.x-screenLocation[0], event.y-screenLocation[1])
//                }
//            }
//        }
//        return true
//    }
//
//}