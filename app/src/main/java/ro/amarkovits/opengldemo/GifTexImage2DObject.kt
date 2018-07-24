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

    fun draw(){
        Log.d(TAG, "draw")
        gifTexImage2DProgram.draw()
    }

    fun resize(width: Int, height: Int){
        gifTexImage2DProgram.setDimensions(width/4, height/4)
        gifTexImage2DProgram.setPosition(width/4,0)

    }

}