package com.plusgallery.android

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.plusgallery.android.util.Animate
import com.plusgallery.android.util.Threading
import kotlinx.android.synthetic.main.activity_launcher.*


class LauncherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_launcher)
        textAppName.animate().alpha(1.0f).setDuration(400)
            .withEndAction { initialize() }.start()
    }

    private fun initialize() {
        (application as GApplication).extensions.fetchRemote {
            Threading.sync {
                if (!it) {
                    Toast.makeText(this, getString(R.string.launcher_error),
                        Toast.LENGTH_LONG).show()
                }
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent, Animate.custom(this, R.anim.fade_in, 0))
            }
        }
    }
}