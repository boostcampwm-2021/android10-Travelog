package com.thequietz.travelog

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.thequietz.travelog.databinding.ActivityTutorialBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialActivity : AppCompatActivity() {
    private lateinit var binding: ActivityTutorialBinding
    private val tutorialViewModel by viewModels<TutorialViewModel>()
    private val adapter by lazy { TutorialViewPagerAdapter() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTutorialBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.vpTutorial.adapter = adapter
        binding.ciTutorial.attachToRecyclerView(binding.vpTutorial.getChildAt(0) as RecyclerView)
        setViewPager()
        setListener()
    }
    fun setViewPager() {
        binding.lifecycleOwner = this
        binding.viewModel = tutorialViewModel
        binding.vpTutorial.registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    tutorialViewModel.setCurrentImg(position)
                }
            }
        )
        tutorialViewModel.tutorialImg.observe(this, { it ->
            it?.let { adapter.submitList(it) }
        })
    }
    fun setListener() {
        binding.btnTutorialSkip.setOnClickListener {
            TravelogApplication.prefs.disableTutorialLoadingState()
            val intent = Intent(this, MainActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }
}
