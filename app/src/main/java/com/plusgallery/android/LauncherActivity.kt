package com.plusgallery.android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.plusgallery.android.util.Animate
import kotlinx.android.synthetic.main.activity_launcher.*

class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        textAppName.animate().alpha(1.0f).setDuration(400)
            .withEndAction { initialize() }.start()
    }

    private fun initialize() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent, Animate.custom(this, R.anim.fade_in, 0))
        /*async{
            (application as GApplication).extensions.fetchInstalled()
        }.onComplete {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent, Animate.custom(this, R.anim.fade_in, 0))
        }*/
    }
}