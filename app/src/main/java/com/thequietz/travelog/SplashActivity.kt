package com.thequietz.travelog

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.thequietz.travelog.databinding.ActivitySplashBinding
import dagger.hilt.android.AndroidEntryPoint

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding
    private val splashViewModel by viewModels<SplashViewModel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        binding.lifecycleOwner = this
        val airPlaneAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.anim_plane)
        val textAnim = AnimationUtils.loadAnimation(this.applicationContext, R.anim.anim_travelog_fade_in)

        binding.tvSplashSubtitle.startAnimation(textAnim)
        binding.tvSplashTitle.startAnimation(textAnim)
        binding.tvSplashTeamName.startAnimation(textAnim)
        binding.ivSplash.startAnimation(airPlaneAnim)
        // TravelogApplication.prefs.disableTutorialLoadingState(false)
        Handler(Looper.getMainLooper()).postDelayed({
            if (!TravelogApplication.prefs.loadTutorialLoadingState()) {
                splashViewModel.caching()
                val intent = Intent(this, TutorialActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                startActivity(intent)
            }
        }, 3000)
        setContentView(binding.root)
    }
}
