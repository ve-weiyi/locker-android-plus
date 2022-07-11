package com.ve.module.locker.ui.page.category

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ve.lib.common.base.view.vm.BaseFragment
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.R
import com.ve.module.locker.databinding.LockerFragmentClassifyBinding
import com.ve.module.locker.ui.page.category.folder.LockerFolderGridFragment
import com.ve.module.locker.ui.page.category.folder.LockerFolderListFragment
import com.ve.module.locker.ui.page.category.tag.LockerTagGridFragment
import com.ve.module.locker.ui.page.category.tag.LockerTagListFragment


/**
 * @Author  weiyi
 * @Date 2022/7/11
 * @Description  current project locker-android
 * 分类管理
 */
class LockerClassifyFragment : BaseFragment<LockerFragmentClassifyBinding>() {

    override fun attachViewBinding(): LockerFragmentClassifyBinding {
        return LockerFragmentClassifyBinding.inflate(layoutInflater)
    }

    companion object {
        const val FRAGMENT_TITLE_KEY: String = "title"
        const val FRAGMENT_CLASS_NAME_KEY: String = "fragmentName"
        const val FRAGMENT_ARGUMENTS_KEY = "arguments"
    }

    /**
     * label 1:folder ,2tag
     */
    var mClassifyType = 1

    /**
     * type 1:grid ,2 list
     */
    var mRankType = 1

    lateinit var fragmentTitle: String
    lateinit var fragmentClassName: String
    lateinit var fragmentArguments: Bundle

    override fun initialize(saveInstanceState: Bundle?) {
        setHasOptionsMenu(true)

        //从bundle中取出数据
        fragmentTitle = arguments?.getString(FRAGMENT_TITLE_KEY) ?: ""
        fragmentClassName = arguments?.getString(FRAGMENT_CLASS_NAME_KEY) ?: ""
        fragmentArguments = arguments?.getBundle(FRAGMENT_ARGUMENTS_KEY) ?: Bundle()


        notifyChange(mClassifyType, mRankType)
    }

    /**
     * 如果需要把 title ,className 也传过去，可以transactionFragment(fragmentClassName,saveInstanceState)
     */
    private fun transactionFragment(fragmentClassName: String, bundle: Bundle? = null) {
        val fragmentClass = Class.forName(fragmentClassName) //完整类名
        val fragment = fragmentClass.newInstance() as Fragment
        fragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.ext_container, fragment, fragmentTitle)
            .commit()
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.locker_menu_classify, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            /**
             * 换排版
             */
            R.id.action_swap -> {
                if (mRankType == 1) {
                    mRankType = 2
                } else {
                    mRankType = 1
                }
                notifyChange(mClassifyType, mRankType)
            }
            /**
             * 换分类
             */
            R.id.action_menu -> {
                if (mClassifyType == 1) {
                    mClassifyType = 2
                } else {
                    mClassifyType = 1
                }
                notifyChange(mClassifyType, mRankType)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun notifyChange(mClassifyType: Int, mRankType: Int) {
        if (mClassifyType == 1 && mRankType == 1) {
            fragmentClassName = LockerFolderGridFragment::class.java.name
        } else if (mClassifyType == 1 && mRankType == 2) {
            fragmentClassName = LockerFolderListFragment::class.java.name
        } else if (mClassifyType == 2 && mRankType == 1) {
            fragmentClassName = LockerTagGridFragment::class.java.name
        } else if (mClassifyType == 2 && mRankType == 2) {
            fragmentClassName = LockerTagListFragment::class.java.name
        } else {
            LogUtil.msg("fragment class name is null")
        }


        if (fragmentClassName.isEmpty()) {
            LogUtil.msg("fragment class name is null")
        } else {
            transactionFragment(fragmentClassName, fragmentArguments)
        }

        if (fragmentTitle.isNotEmpty()) {
            (activity as AppCompatActivity?)!!.supportActionBar?.title = fragmentTitle
        }
    }
}