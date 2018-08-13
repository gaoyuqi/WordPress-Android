package org.wordpress.android.ui.pages

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import kotlinx.android.synthetic.main.pages_fragment.*
import org.wordpress.android.R
import org.wordpress.android.R.id
import org.wordpress.android.ui.RequestCodes
import org.wordpress.android.ui.posts.BasicFragmentDialog.BasicDialogNegativeClickInterface
import org.wordpress.android.ui.posts.BasicFragmentDialog.BasicDialogPositiveClickInterface
import org.wordpress.android.ui.posts.EditPostActivity

const val EXTRA_PAGE_REMOTE_ID_KEY = "extra_page_remote_id_key"
const val EXTRA_PAGE_PARENT_ID_KEY = "extra_page_parent_id_key"

class PagesActivity : AppCompatActivity(), BasicDialogPositiveClickInterface, BasicDialogNegativeClickInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.pages_activity)

        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setHomeButtonEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onPositiveClicked(instanceTag: String) {
        passDeleteConfirmation(instanceTag.toLong())
    }

    override fun onNegativeClicked(instanceTag: String) {
    }

    private fun passDeleteConfirmation(remoteId: Long) {
        val fragment = supportFragmentManager.findFragmentById(id.fragment_container)
        if (fragment is PagesFragment) {
            fragment.onPageDeleteConfirmed(remoteId)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == RequestCodes.EDIT_POST && resultCode == Activity.RESULT_OK && data != null) {
            val pageId = data.getLongExtra(EditPostActivity.EXTRA_POST_REMOTE_ID, -1)
            if (pageId != -1L) {
                onPageEditFinished(pageId)
            }
        } else if (requestCode == RequestCodes.PAGE_PARENT && resultCode == Activity.RESULT_OK && data != null) {
            val parentId = data.getLongExtra(EXTRA_PAGE_PARENT_ID_KEY, -1)
            val pageId = data.getLongExtra(EXTRA_PAGE_REMOTE_ID_KEY, -1)
            if (pageId != -1L && parentId != -1L) {
                onPageParentSet(pageId, parentId)
            }
        }
    }

    private fun onPageEditFinished(pageId: Long) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is PagesFragment) {
            fragment.onPageEditFinished(pageId)
        }
    }

    private fun onPageParentSet(pageId: Long, parentId: Long) {
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (fragment is PagesFragment) {
            fragment.onPageParentSet(pageId, parentId)
        }
    }
}