package com.ve.module.android.ui.page.activity


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.ve.module.android.R
import com.ve.module.android.config.Constant
import com.ve.module.android.databinding.ActivityCommonBinding
import com.ve.lib.common.base.view.vm.BaseActivity

/**
 * @Description 通过bundle 和 FragmentName 切换页面
 * https://blog.csdn.net/qunqunstyle99/article/details/82026124
 * @Author  weiyi
 * @Date 2022/3/20
 */
class CommonActivity : BaseActivity<ActivityCommonBinding>() {

    companion object {

        const val TITLE: String = "CommonActivity.title"
        const val CLASS_NAME: String = "CommonActivity.fragment"
        /**
         * className 请使用Fragment::class.java.name，不要使用 Fragment.javaClass.name
         * CommonActivity.start(this,title,Fragment::class.java.name, bundle)
         */
        fun start(context: Context, title: String, className: String, fragmentBundle: Bundle? = null) {
            Intent(context, CommonActivity::class.java).run {
                putExtra(TITLE, title)
                putExtra(CLASS_NAME, className)
                if (fragmentBundle != null) {
                    putExtras(fragmentBundle)
                }
                context.startActivity(this,fragmentBundle)
            }
        }
    }

    override fun initialize(saveInstanceState: Bundle?) {
        var toolbar = mBinding.extToolbar.toolbar


        intent.extras?.let {
            val commonActivityTitle = it.getString(TITLE, "标题")
            val className = it.getString(CLASS_NAME, "class name") ?: return
            val bundle = intent.extras //从bundle中取出数据

            toolbar.run {
                title = commonActivityTitle
                setSupportActionBar(this)
                supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }

            val fragmentClass = Class.forName(className) //完整类名
            val fragment = fragmentClass.newInstance() as Fragment
            fragment.arguments=bundle

            supportFragmentManager.beginTransaction()
                .replace(R.id.common_frame_layout, fragment, Constant.Type.COLLECT_TYPE_KEY)
                .commit()

        }

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
