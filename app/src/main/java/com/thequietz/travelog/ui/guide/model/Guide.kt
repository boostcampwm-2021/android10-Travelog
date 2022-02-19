package com.thequietz.travelog.ui.guide.model

import com.thequietz.travelog.ui.guide.Place
import com.thequietz.travelog.ui.guide.RecommendPlace

sealed class Guide {
    data class Header(val title: String = "") : Guide()
    data class SpecificRecommend(
        val specificRecommendList: MutableList<RecommendPlace> = mutableListOf()
    ) : Guide()
    data class Dosi(
        val specificDOSIList: MutableList<Place> = mutableListOf()
    ) : Guide()
}
enum class GuideViewType {
    TITLE, RECOMMEND, ALLPLACE
}
