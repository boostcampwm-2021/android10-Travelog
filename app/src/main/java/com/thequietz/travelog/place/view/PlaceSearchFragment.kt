package com.thequietz.travelog.place.view

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.thequietz.travelog.R
import com.thequietz.travelog.databinding.FragmentPlaceSearchBinding
import com.thequietz.travelog.place.adapter.PlaceSearchAdapter
import com.thequietz.travelog.place.model.PlaceSearchModel
import com.thequietz.travelog.place.model.detail.PlaceDetailModel
import com.thequietz.travelog.place.viewmodel.PlaceSearchViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceSearchFragment : Fragment() {

    private var _binding: FragmentPlaceSearchBinding? = null
    private val binding get() = _binding!!
    private val viewModel: PlaceSearchViewModel by viewModels()

    private lateinit var adapter: PlaceSearchAdapter
    private lateinit var _context: Context
    private lateinit var gson: Gson
    private lateinit var inputManager: InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_place_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        gson = Gson()
        _context = requireContext()
        inputManager = _context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

        adapter = PlaceSearchAdapter(object : PlaceSearchAdapter.OnItemClickListener {
            override fun onClickItem(model: PlaceSearchModel, position: Int): Boolean {
                val param = gson.toJson(model)
                val action =
                    PlaceSearchFragmentDirections.actionPlaceSearchFragmentToPlaceDetailFragment(
                        param,
                        false,
                    )
                view.findNavController().navigate(action)
                return true
            }
        })
        binding.rvPlaceSearch.adapter = adapter
        binding.rvPlaceSearch.layoutManager = LinearLayoutManager(_context)
        binding.rvPlaceSearch.addItemDecoration(
            DividerItemDecoration(
                _context,
                LinearLayoutManager.VERTICAL
            )
        )
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.etPlaceSearch.setOnKeyListener { v, code, _ ->
            if (code !== android.view.KeyEvent.KEYCODE_ENTER) return@setOnKeyListener false
            val query = (v as EditText).text.toString()
            viewModel.loadPlaceList(query)
            true
        }

        viewModel.place.observe(viewLifecycleOwner, {
            (binding.rvPlaceSearch.adapter as PlaceSearchAdapter).submitList(it)
        })

        binding.etPlaceSearch.requestFocus()
        viewModel.initPlaceList()

        findNavController().apply {
            currentBackStackEntry?.savedStateHandle
                ?.getLiveData<PlaceDetailModel>("result")?.observe(
                    viewLifecycleOwner,
                    {
                        previousBackStackEntry?.savedStateHandle?.set("result", it)
                        popBackStack()
                    }
                )
        }
    }
}
