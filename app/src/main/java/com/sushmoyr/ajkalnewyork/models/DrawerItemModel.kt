package com.sushmoyr.ajkalnewyork.models

import com.sushmoyr.ajkalnewyork.models.core.Category
import com.sushmoyr.ajkalnewyork.models.core.SubCategory

data class DrawerItemModel(
    val category: Category,
    val subCategoryList: List<SubCategory>? = null,
)
