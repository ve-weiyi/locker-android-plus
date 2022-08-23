package com.ve.module.android.ui.page.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.view.*
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.just.agentweb.AgentWeb
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.module.android.R
import com.ve.module.android.databinding.ActivityDetailBinding
import com.ve.lib.common.utils.log.LogUtil

/**
 * @Description 
 * 文章细节  网页代理activity
 * @Author  weiyi
 * @Date 2022/3/21
 */
class ArticleDetailActivity : BaseActivity<ActivityDetailBinding>() {

    private lateinit var mAgentWeb: AgentWeb

    private var shareTitle: String = ""
    private var shareUrl: String = ""
    private var shareId: Int = -1

    companion object {
        const val ARTICLE_URL_KEY: String = "article_url_key"
        const val ARTICLE_TITLE_KEY: String = "article_title_key"
        const val ARTICLE_ID_KEY: String = "article_id_key"

        fun start(context: Context?, id: Int, title: String, url: String, bundle: Bundle? = null) {
            Intent(context, ArticleDetailActivity::class.java).run {
                putExtra(ARTICLE_ID_KEY, id)
                putExtra(ARTICLE_TITLE_KEY, title)
                putExtra(ARTICLE_URL_KEY, url)
                context?.startActivity(this, bundle)
            }
        }
        fun start(context: Context?, title: String, url: String, bundle: Bundle? = null) {
            Intent(context, ArticleDetailActivity::class.java).run {
                putExtra(ARTICLE_TITLE_KEY, title)
                putExtra(ARTICLE_URL_KEY, url)
                context?.startActivity(this, bundle)
            }
        }

        fun start(context: Context?, title: String,url: String) {
            start(context, -1, title, url)
        }
    }


    /**
     * 返回绑定对象
     */
    override fun attachViewBinding(): ActivityDetailBinding {
        return ActivityDetailBinding.inflate(layoutInflater)
    }



    @RequiresApi(Build.VERSION_CODES.N)
    override fun initialize(saveInstanceState: Bundle?) {
        //设置标题栏之后才能修改菜单
        mBinding.toolbar.toolbar.run {
            title = getString(R.string.details)
            setSupportActionBar(this)
            //需要先设置设置toolbar
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        mBinding.toolbar.tvTitle.apply {
            text = getString(R.string.loading)
            visibility = View.VISIBLE
            postDelayed({
                this.isSelected = true
            }, 2000)
        }

        intent.extras?.let {
            shareId = it.getInt(ARTICLE_ID_KEY, -1)
            shareTitle = it.getString(ARTICLE_TITLE_KEY, "")
            shareUrl = it.getString(ARTICLE_URL_KEY, "")
        }

        LogUtil.d("detail run")
        mBinding.toolbar.tvTitle.text = Html.fromHtml(intent.getStringExtra(ARTICLE_TITLE_KEY), Html.FROM_HTML_MODE_COMPACT)
        initAgentWeb()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, getString(
                        R.string.share_article_url,
                        getString(R.string.app_name), shareTitle, shareUrl
                    ))
                    type = "text/plain"
                    startActivity(Intent.createChooser(this, getString(R.string.action_share)))
                }
                return true
            }
            R.id.action_like -> {

                return true
            }
            R.id.action_browser -> {
                Intent().run {
                    action = "module.module.android.intent.action.VIEW"
                    data = Uri.parse(shareUrl)
                    startActivity(this)
                }
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onBackPressed() {
        mAgentWeb.let {
            if (!it.back()) {
                super.onBackPressed()
            }
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun initAgentWeb() {
        mAgentWeb = AgentWeb.with(this)
            .setAgentWebParent(mBinding.webContent, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebViewClient(mWebViewClient)
            .createAgentWeb()
            .ready()
            .go(getLoadUrl())

        val webView = mAgentWeb.webCreator.webView
        //获取手势焦点
        webView.requestFocusFromTouch()
        webView.settings.apply {
            //支持JS
            javaScriptEnabled = true
            //是否支持缩放
            setSupportZoom(true)
            builtInZoomControls = true
            //是否显示缩放按钮
            displayZoomControls = false
            //自适应屏幕
            useWideViewPort = true
            loadWithOverviewMode = true
        }
        addBGChild(mAgentWeb.webCreator.webParentLayout as FrameLayout) // 得到 AgentWeb 最底层的控件
    }

    private fun addBGChild(frameLayout: FrameLayout) {
        val title = "技术由 AgentWeb 提供"
        val mTextView = TextView(frameLayout.context)
        mTextView.text = title
        mTextView.textSize = 16f
        mTextView.setTextColor(Color.parseColor("#727779"))
        frameLayout.setBackgroundColor(Color.parseColor("#272b2d"))
        val mFlp = FrameLayout.LayoutParams(-2, -2)
        mFlp.gravity = Gravity.CENTER_HORIZONTAL
        val scale: Float = frameLayout.context.resources.displayMetrics.density
        mFlp.topMargin = (15 * scale + 0.5f).toInt()
        frameLayout.addView(mTextView, 0, mFlp)
    }

    private fun getLoadUrl(): String? {
        return intent.getStringExtra(ARTICLE_URL_KEY)
    }

    private val mWebViewClient: WebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            //获取url
            val uri = request!!.url
            /**
             * 一个URL通常由以下几个部分构成：协议、域名、端口、路径和URL地址参数。
             * https://baike.baidu.com/item/module.module.android/60243
             * uri.scheme 协议 https
             * uri.authority 域名 baike.baidu.com
             * uri.path 路径 item/module.module.android/60243
             * uri.queryParameterNames 参数 null
             * uri.getQueryParameter("id")
             */
            return super.shouldOverrideUrlLoading(view, request)
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)

        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)

        }
    }

    /**
     * 事件处理
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return mAgentWeb.handleKeyEvent(keyCode, event) || super.onKeyDown(keyCode, event)
    }

    /**
     * 跟随 Activity Or Fragment 生命周期 ， 释放 CPU 更省电 。
     */
    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        super.onDestroy()
    }
}
