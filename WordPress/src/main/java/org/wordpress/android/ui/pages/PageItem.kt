package org.wordpress.android.ui.pages

import android.support.annotation.IdRes
import org.wordpress.android.R
import org.wordpress.android.ui.pages.PageItem.Action.DELETE_PERMANENTLY
import org.wordpress.android.ui.pages.PageItem.Action.MOVE_TO_DRAFT
import org.wordpress.android.ui.pages.PageItem.Action.MOVE_TO_TRASH
import org.wordpress.android.ui.pages.PageItem.Action.PUBLISH_NOW
import org.wordpress.android.ui.pages.PageItem.Action.SET_PARENT
import org.wordpress.android.ui.pages.PageItem.Action.VIEW_PAGE
import org.wordpress.android.ui.pages.PageItem.Type.DIVIDER
import org.wordpress.android.ui.pages.PageItem.Type.EMPTY
import org.wordpress.android.ui.pages.PageItem.Type.PAGE
import org.wordpress.android.ui.pages.PageItem.Type.PARENT

sealed class PageItem(val type: Type) {
    abstract class Page(
        open val id: Long,
        open val title: String,
        open val labelRes: Int? = null,
        open var indent: Int = 0,
        open val actions: Set<Action> = setOf(),
        open val icon: String? = null
    ) : PageItem(PAGE)

    data class PublishedPage(
        override val id: Long,
        override val title: String,
        override val labelRes: Int? = null,
        override var indent: Int = 0,
        override val actions: Set<Action> = setOf(VIEW_PAGE, SET_PARENT, MOVE_TO_DRAFT, MOVE_TO_TRASH)
    ) : Page(id, title, labelRes, indent, actions)

    data class DraftPage(
        override val id: Long,
        override val title: String,
        override val labelRes: Int? = null,
        override var indent: Int = 0,
        override val actions: Set<Action> = setOf(VIEW_PAGE, SET_PARENT, PUBLISH_NOW, MOVE_TO_TRASH)
    ) : Page(id, title, labelRes, indent, actions)

    data class ScheduledPage(
        override val id: Long,
        override val title: String,
        override val labelRes: Int? = null,
        override var indent: Int = 0,
        override val actions: Set<Action> = setOf(VIEW_PAGE, SET_PARENT, MOVE_TO_DRAFT, MOVE_TO_TRASH)
    ) : Page(id, title, labelRes, indent, actions)

    data class TrashedPage(
        override val id: Long,
        override val title: String,
        override val labelRes: Int? = null,
        override var indent: Int = 0,
        override val actions: Set<Action> = setOf(VIEW_PAGE, MOVE_TO_DRAFT, DELETE_PERMANENTLY)
    ) : Page(id, title, labelRes, indent, actions)

    data class ParentPage(
        val id: Long,
        val title: String,
        var isSelected: Boolean
    ) : PageItem(PARENT)

    data class Divider(val title: String) : PageItem(DIVIDER)

    data class Empty(val textResource: Int? = null) : PageItem(EMPTY)

    enum class Type(val viewType: Int) {
        PAGE(1), DIVIDER(2), EMPTY(3), PARENT(4)
    }

    enum class Action(@IdRes val itemId: Int) {
        VIEW_PAGE(R.id.view_page),
        SET_PARENT(R.id.set_parent),
        PUBLISH_NOW(R.id.publish_now),
        MOVE_TO_DRAFT(R.id.move_to_draft),
        DELETE_PERMANENTLY(R.id.delete_permanently),
        MOVE_TO_TRASH(R.id.move_to_trash);

        companion object {
            fun fromItemId(itemId: Int): Action {
                return values().firstOrNull { it.itemId == itemId }
                        ?: throw IllegalArgumentException("Unexpected item ID in context menu")
            }
        }
    }
}