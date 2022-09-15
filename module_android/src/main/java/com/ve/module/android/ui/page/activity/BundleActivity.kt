package com.ve.module.android.ui.page.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ve.module.android.R
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.ActivityCommonBinding
import com.ve.module.android.ui.page.fragment.CollectFragment
import com.ve.module.android.ui.page.setting.QrCodeFragment
import com.ve.module.android.ui.page.setting.SettingsFragment
import com.ve.module.android.ui.page.fragment.AboutFragment
import com.ve.module.android.ui.page.todo.AddTodoFragment
import com.ve.lib.common.base.view.vm.BaseActivity

class BundleActivity : BaseActivity<ActivityCommonBinding>() {


    private var mType = ""

    companion object {

        const val TITLE: String = "CommonActivity.title"
        const val CLASS_NAME: String = "CommonActivity.fragment"

        fun start(context: Context, title: String, className: String,bundle: Bundle? = null) {
            Intent(context, BundleActivity::class.java).run {
                putExtra(TITLE, title)
                putExtra(CLASS_NAME, className)
                context.startActivity(this,bundle)
            }
        }
    }

    override fun initialize(saveInstanceState: Bundle?) {
        val extras = intent.extras ?: return
        mType = extras.getString(Constant.TYPE_KEY, "")
        var toolbar=mBinding.extToolbar.toolbar

        toolbar.run {
            title = getString(R.string.app_name)
            setSupportActionBar(this)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }
        val fragment = when (mType) {
            Constant.Type.COLLECT_TYPE_KEY -> {
                toolbar.title = getString(R.string.collect)
                CollectFragment.getInstance(extras)
            }
            Constant.Type.ABOUT_US_TYPE_KEY -> {
                toolbar.title = getString(R.string.about_us)
                AboutFragment.getInstance(extras)
            }
            Constant.Type.SETTING_TYPE_KEY -> {
                toolbar.title = getString(R.string.setting)
                SettingsFragment.getInstance(extras)
            }
//            Constant.Type.SEARCH_TYPE_KEY -> {
//                toolbar.title = extras.getString(Constant.SEARCH_KEY, "")
//                SearchListFragment.getInstance(extras)
//            }
            Constant.Type.ADD_TODO_TYPE_KEY -> {
                toolbar.title = getString(R.string.add)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.EDIT_TODO_TYPE_KEY -> {
                toolbar.title = getString(R.string.edit)
                AddTodoFragment.getInstance(extras)
            }
            Constant.Type.SEE_TODO_TYPE_KEY -> {
                toolbar.title = getString(R.string.see)
                AddTodoFragment.getInstance(extras)
            }
//            Constant.Type.SHARE_ARTICLE_TYPE_KEY -> {
//                toolbar.title = getString(R.string.share_article)
//                ShareArticleFragment.getInstance()
//            }
            Constant.Type.SCAN_QR_CODE_TYPE_KEY -> {
                toolbar.title = getString(R.string.scan_code_download)
                QrCodeFragment.getInstance()
            }
            else -> {
                null
            }
        }
        fragment ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.common_frame_layout, fragment, Constant.Type.COLLECT_TYPE_KEY)
            .commit()

    }

    public fun showFragment(fragment: Fragment) {
        fragment ?: return
        supportFragmentManager.beginTransaction()
            .replace(R.id.common_frame_layout, fragment, Constant.Type.COLLECT_TYPE_KEY)
            .commit()
    }





    override fun attachViewBinding(): ActivityCommonBinding {
        return ActivityCommonBinding.inflate(layoutInflater)
    }

}
