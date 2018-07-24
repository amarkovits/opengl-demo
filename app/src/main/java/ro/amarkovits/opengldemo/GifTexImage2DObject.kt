package ro.amarkovits.opengldemo

import android.content.res.Resources
import android.util.Log
import pl.droidsonroids.gif.GifTexImage2D
import pl.droidsonroids.gif.InputSource

class GifTexImage2DObject(resources: Resources, gifId: Int) {

    val TAG = GifTexImage2DObject::class.java.simpleName

    lateinit var gifTexImage2D : GifTexImage2D
    lateinit var gifTexImage2DProgram : GifTexImage2DProgram

    init {
        gifTexImage2D = GifTexImage2D(InputSource.ResourcesSource(resources, gifId), null)
        gifTexImage2D.startDecoderThread()
        gifTexImage2DProgram = GifTexImage2DProgram(gifTexImage2D)
    }

    fun initialize(){
        Log.d(TAG, "initializer")
        gifTexImage2DProgram.initialize()
    }

    fun draw(projectionMatrix: FloatArray){
        gifTexImage2DProgram.draw(projectionMatrix)
    }

}