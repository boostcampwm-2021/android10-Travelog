package com.thequietz.travelog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.thequietz.travelog.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSplashBinding.inflate(inflater, container, false)

        val airPlaneAnim = AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.anim_plane)
        val textAnim = AnimationUtils.loadAnimation(requireActivity().applicationContext, R.anim.anim_travelog_fade_in)
        binding.tvSplashSubtitle.startAnimation(textAnim)
        binding.tvSplashTitle.startAnimation(textAnim)
        binding.tvSplashTeamName.startAnimation(textAnim)
        // binding.ivSplash.animation = anim
        binding.ivSplash.startAnimation(airPlaneAnim)
        return binding.root
    }
}
