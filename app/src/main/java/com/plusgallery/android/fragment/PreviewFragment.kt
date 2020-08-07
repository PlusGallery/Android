package com.plusgallery.android.fragment

import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.media.MediaPlayer
import android.media.MediaPlayer.OnPreparedListener
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import com.plusgallery.android.R
import kotlinx.android.synthetic.main.fragment_preview.*
import java.io.IOException


class PreviewFragment : Fragment(), TextureView.SurfaceTextureListener {
    private lateinit var touchListener: View.OnTouchListener
    private lateinit var mediaPlayer: MediaPlayer

    companion object {
        fun new(listener: View.OnTouchListener): PreviewFragment {
            val fragment = PreviewFragment()
            fragment.touchListener = listener
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_preview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //imageView.setOnTouchListener(touchListener)

        textureView.surfaceTextureListener = this
        textureView.setOnTouchListener(touchListener)
    }

    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, p1: Int, p2: Int) {}

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {}

    override fun onSurfaceTextureDestroyed(p0: SurfaceTexture): Boolean {return false}

    override fun onSurfaceTextureAvailable(p0: SurfaceTexture, p1: Int, p2: Int) {
        val surface = Surface(p0)

        try {
            mediaPlayer = MediaPlayer()
            // parse the URL path
            mediaPlayer.setDataSource(
                requireContext(),
                Uri.parse("https://static1.e621.net/data/ee/4d/ee4dcd1cad7c4699f1549acc04d08c21.webm")
            )
            mediaPlayer.setSurface(surface)
            mediaPlayer.isLooping = true
            mediaPlayer.prepareAsync()
            /*mediaPlayer.setOnBufferingUpdateListener(this)
            mediaPlayer.setOnCompletionListener(this)
            mediaPlayer.setOnVideoSizeChangedListener(this)*/
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)

            // Play video when the media source is ready for playback.
            mediaPlayer.setOnPreparedListener { mediaPlayer -> mediaPlayer.start() }
        } catch (e: IllegalArgumentException) {
            Log.d("TAG", e.message!!)
        } catch (e: SecurityException) {
            Log.d("TAG", e.message!!)
        } catch (e: IllegalStateException) {
            Log.d("TAG", e.message!!)
        } catch (e: IOException) {
            Log.d("TAG", e.message!!)
        }
    }
}