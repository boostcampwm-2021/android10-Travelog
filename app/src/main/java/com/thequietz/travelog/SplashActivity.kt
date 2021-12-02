package com.thequietz.travelog

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.thequietz.travelog.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel by viewModels<SplashViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)

        val airPlaneAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.anim_plane)
        val textAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.anim_travelog_fade_in)

        binding.tvSplashSubtitle.startAnimation(textAnim)
        binding.tvSplashTitle.startAnimation(textAnim)
        binding.tvSplashTeamName.startAnimation(textAnim)
        binding.ivSplash.startAnimation(airPlaneAnim)

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
        }, 5000)
        setContentView(binding.root)
    }

    override fun onCreateView(
        parent: View?,
        name: String,
        context: Context,
        attrs: AttributeSet
    ): View? {
        return super.onCreateView(parent, name, context, attrs)
        binding.lifecycleOwner = this
    }
}
