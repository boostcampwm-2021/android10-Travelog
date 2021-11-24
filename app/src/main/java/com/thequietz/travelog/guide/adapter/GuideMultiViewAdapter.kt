package com.thequietz.travelog.guide.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.thequietz.travelog.databinding.ItemRecyclerGuidePlaceAllBinding
import com.thequietz.travelog.databinding.ItemRecyclerGuideRecommendPlaceBinding
import com.thequietz.travelog.databinding.ItemRecyclerTitleBinding
import com.thequietz.travelog.databinding.RecyclerAllPlaceImageBinding
import com.thequietz.travelog.databinding.RecyclerGuideRecommendImageBinding
import com.thequietz.travelog.guide.Place
import com.thequietz.travelog.guide.RecommendPlace
import com.thequietz.travelog.guide.model.Guide
import com.thequietz.travelog.guide.model.GuideViewType
import com.thequietz.travelog.guide.view.GuideFragmentDirections
import com.thequietz.travelog.place.model.PlaceGeometry
import com.thequietz.travelog.place.model.PlaceLocation
import com.thequietz.travelog.place.model.PlaceSearchModel

class GuideMultiViewAdapter(val frag: Fragment) : ListAdapter<Guide, RecyclerView.ViewHolder>(
    GuideDiffUtilCallback()
) {
    class GuideTitleViewHolder(val binding: ItemRecyclerTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(header: Guide.Header) {
            binding.item = header
            binding.executePendingBindings()
        }
    }
    class GuideRecommendViewHolder(frag: Fragment, val binding: RecyclerGuideRecommendImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val adapter = RecommendMultiViewImageAdapter(object : RecommendMultiViewImageAdapter.OnItemClickListener {
            override fun onItemClick(item: RecommendPlace) {
                val param = Gson().toJson(
                    PlaceSearchModel(
                        item.name,
                        "23",
                        PlaceGeometry(
                            PlaceLocation(
                                item.latitude, item.longitude
                            )
                        )
                    )
                )
                val action = GuideFragmentDirections
                    .actionGuideFragmentToPlaceDetailFragmentFromGuide(
                        param, true
                    )
                findNavController(frag).navigate(action)
            }
        })
        init {
            binding.rvItemRecommendImage.adapter = adapter
        }
        fun bind(recommend: Guide.SpecificRecommend) {
            adapter.submitList(recommend.specificRecommendList)
        }
    }
    class GuideAllPlaceViewHolder(frag: Fragment, binding: RecyclerAllPlaceImageBinding) : RecyclerView.ViewHolder(binding.root) {
        val adapter = AllPlaceMultiViewImageAdapter(object : AllPlaceMultiViewImageAdapter.OnItemClickListener {
            override fun onItemClick(item: Place) {
                val action = GuideFragmentDirections
                    .actionGuideFragmentToSpecificGuideFragment(item.areaCode)
                findNavController(frag).navigate(action)
            }
        })
        init {
            binding.rvItemAllPlaceImage.adapter = adapter
        }
        fun bind(dosi: Guide.Dosi) {
            adapter.submitList(dosi.specificDOSIList)
        }
    }
    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is Guide.Header -> 0
            is Guide.SpecificRecommend -> 1
            is Guide.Dosi -> 2
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (GuideViewType.values().get(viewType)) {
            GuideViewType.TITLE -> {
                GuideTitleViewHolder(
                    ItemRecyclerTitleBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            GuideViewType.RECOMMEND -> {
                GuideRecommendViewHolder(
                    frag,
                    RecyclerGuideRecommendImageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
            GuideViewType.ALLPLACE -> {
                GuideAllPlaceViewHolder(
                    frag,
                    RecyclerAllPlaceImageBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItem(position)) {
            is Guide.Header -> {
                (holder as GuideTitleViewHolder).bind(getItem(position) as Guide.Header)
            }
            is Guide.SpecificRecommend -> {
                (holder as GuideRecommendViewHolder).bind(getItem(position) as Guide.SpecificRecommend)
            }
            is Guide.Dosi -> {
                (holder as GuideAllPlaceViewHolder).bind(getItem(position) as Guide.Dosi)
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Guide>() {
            override fun areItemsTheSame(oldItem: Guide, newItem: Guide): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Guide, newItem: Guide): Boolean {
                return oldItem == newItem
            }
        }
    }
}

class RecommendMultiViewImageAdapter(private val onItemClickListener: OnItemClickListener) : ListAdapter<RecommendPlace, RecommendMultiViewImageAdapter.RecommendMultiViewImageViewHolder>(
    RecommendPlaceDiffUtilCallback()
) {
    interface OnItemClickListener {
        fun onItemClick(item: RecommendPlace)
    }
    class RecommendMultiViewImageViewHolder(val binding: ItemRecyclerGuideRecommendPlaceBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RecommendPlace) {
            binding.item = item
            binding.executePendingBindings()
        }
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendMultiViewImageAdapter.RecommendMultiViewImageViewHolder {
        return RecommendMultiViewImageViewHolder(
            ItemRecyclerGuideRecommendPlaceBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(
        holder: RecommendMultiViewImageViewHolder,
        position: Int
    ) {
        holder.bind(getItem(position))
        holder.binding.imgItemRecommendPlace.setOnClickListener { view ->
            onItemClickListener.onItemClick(getItem(position))
        }
    }
}
class AllPlaceMultiViewImageAdapter(
    private val onItemClickListener: OnItemClickListener
) : ListAdapter<Place, AllPlaceMultiViewImageAdapter.AllPlaceMultiImageViewHolder>(
    PlaceDiffUtilCallback()
) {
    interface OnItemClickListener {
        fun onItemClick(item: Place)
    }

    class AllPlaceMultiImageViewHolder(val binding: ItemRecyclerGuidePlaceAllBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Place) {
            binding.item = item
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllPlaceMultiImageViewHolder {
        return AllPlaceMultiImageViewHolder(
            ItemRecyclerGuidePlaceAllBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: AllPlaceMultiImageViewHolder, position: Int) {
        holder.bind(getItem(position))
        holder.binding.ivItemPlaceAll.setOnClickListener { view ->
            onItemClickListener.onItemClick(getItem(position))
        }
    }
}
class GuideDiffUtilCallback : DiffUtil.ItemCallback<Guide>() {
    override fun areItemsTheSame(oldItem: Guide, newItem: Guide): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }

    override fun areContentsTheSame(oldItem: Guide, newItem: Guide): Boolean {
        return oldItem == newItem
    }
}
class RecommendPlaceDiffUtilCallback : DiffUtil.ItemCallback<RecommendPlace>() {
    override fun areItemsTheSame(oldItem: RecommendPlace, newItem: RecommendPlace): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: RecommendPlace, newItem: RecommendPlace): Boolean {
        return oldItem == newItem
    }
}
