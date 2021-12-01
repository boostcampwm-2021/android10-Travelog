package com.thequietz.travelog

import android.content.Context
import android.os.Bundle
import android.os.StrictMode
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.thequietz.travelog.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
        navController = navHostFragment!!.findNavController()
        binding.bottomNavigation.setupWithNavController(navController)

        val topLevelDestinations =
            hashSetOf(
                R.id.guideFragment,
                R.id.scheduleFragment,
                R.id.recordFragment,
                R.id.menuFragment
            )
        val appBarConfig = AppBarConfiguration.Builder(topLevelDestinations).build()
        binding.toolbar.setupWithNavController(navController, appBarConfig)

        navController.addOnDestinationChangedListener { controller, destination, arguments ->
            binding.toolbar.visibility = View.VISIBLE
            binding.toolbar.titleMarginStart = 10

            val customToolbarFragmentSet = setOf(
                R.id.otherInfoFragment,
                R.id.scheduleDetailFragment,
                R.id.schedulePlaceFragment,
                R.id.scheduleSelectFragment,
                R.id.confirmFragment,
                R.id.placeRecommendFragment,
                R.id.placeSearchFragment,
                R.id.recordBasicFragment,
                R.id.placeDetailFragment,
                R.id.placeDetailFragmentFromGuide
            )

            when (destination.id) {
                R.id.guideFragment -> binding.toolbar.title = "둘러보기"
                R.id.specificGuideFragment -> binding.toolbar.title = "상세 지역 선택"

                R.id.scheduleFragment -> binding.toolbar.title = "내 여행 일정"
                R.id.recordFragment -> binding.toolbar.title = "나만의 여행 기록"
                R.id.menuFragment -> binding.toolbar.title = "메뉴"
                R.id.recordAddImageFragment -> binding.toolbar.title = "여행 이미지 추가"
                R.id.recordViewOneFragment -> binding.toolbar.title = "여행 기록 보기"
                R.id.recordViewManyFragment -> binding.toolbar.title = "여행 기록 보기"
                R.id.scheduleDetailFragment -> binding.toolbar.title = "일정 설정"
                else -> binding.toolbar.title = ""
            }

            if (customToolbarFragmentSet.contains(destination.id))
                binding.toolbar.visibility = View.GONE
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view: View? = currentFocus
        if (view != null && (ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) && view is EditText) {
            val scrcoords = IntArray(2)
            view.getLocationOnScreen(scrcoords)
            val x: Float = ev.rawX + view.getLeft() - scrcoords[0]
            val y: Float = ev.rawY + view.getTop() - scrcoords[1]
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom()) {
                (this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                    this.window.decorView.applicationWindowToken,
                    0
                )
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}
