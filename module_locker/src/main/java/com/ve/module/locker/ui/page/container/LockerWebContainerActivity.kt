package com.ve.module.locker.ui.page.container

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
import com.ve.lib.common.R
import com.ve.lib.common.base.view.vm.BaseActivity
import com.ve.lib.common.vutils.LogUtil
import com.ve.module.locker.databinding.LockerActivityContainerWebBinding

/**
 * @Description 
 * 文章细节  网页代理activity
 * @Author  weiyi
 * @Date 2022/3/21
 */
class LockerWebContainerActivity : BaseActivity<LockerActivityContainerWebBinding>() {

    private lateinit var mAgentWeb: AgentWeb

    private var agentWebTitle: String = ""
    private var agentWebUrl: String = ""
    private var agentWebId: Int = -1

    companion object {
        const val WEB_URL_KEY: String = "WebContainer.url"
        const val WEB_TITLE_KEY: String = "WebContainer.title"
        const val WEB_ID_KEY: String = "WebContainer.id"

        fun start(context: Context, id: Int, title: String, url: String, bundle: Bundle? = null) {
            Intent(context, LockerWebContainerActivity::class.java).run {
                putExtra(WEB_ID_KEY, id)
                putExtra(WEB_TITLE_KEY, title)
                putExtra(WEB_URL_KEY, url)
                context.startActivity(this, bundle)
            }
        }
        fun start(context: Context, title: String, url: String, bundle: Bundle? = null) {
            Intent(context, LockerWebContainerActivity::class.java).run {
                putExtra(WEB_TITLE_KEY, title)
                putExtra(WEB_URL_KEY, url)
                context.startActivity(this, bundle)
            }
        }

        fun start(context: Context, title: String,url: String) {
            start(context, -1, title, url)
        }
    }


    override fun attachViewBinding(): LockerActivityContainerWebBinding {
        return LockerActivityContainerWebBinding.inflate(layoutInflater)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun initialize(saveInstanceState: Bundle?) {

        intent.extras?.let {
            agentWebId = it.getInt(WEB_ID_KEY, -1)
            agentWebTitle = it.getString(WEB_TITLE_KEY, "")
            agentWebUrl = it.getString(WEB_URL_KEY, "")
        }
        
        //设置标题栏之后才能修改菜单
        mBinding.extToolbar.toolbar.run {
            title=""
            setSupportActionBar(this)
            //需要先设置设置toolbar
            supportActionBar?.setHomeButtonEnabled(true)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        mBinding.extToolbar.tvTitle.apply {
            text = Html.fromHtml(agentWebTitle, Html.FROM_HTML_MODE_COMPACT)
            visibility = View.VISIBLE
            postDelayed({
                this.isSelected = true
            }, 2000)
        }
        
        
        initAgentWeb()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_content, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            //分享
            R.id.action_share -> {
                Intent().run {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT,
                        "%1${agentWebId}【%2$agentWebTitle】：%3$agentWebUrl"
                    )
                    type = "text/plain"
                    startActivity(Intent.createChooser(this, "分享"))
                }
                return true
            }
            R.id.action_collect -> {

                return true
            }
            R.id.action_browser -> {
                Intent().run {
                    action = "android.intent.action.VIEW"
                    data = Uri.parse(agentWebUrl)
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
            .setAgentWebParent(mBinding.webContainer, LinearLayout.LayoutParams(-1, -1))
            .useDefaultIndicator()
            .setWebViewClient(mWebViewClient)
            .createAgentWeb()
            .ready()
            .go(agentWebUrl)

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
        val title = "技术由 Locker AgentWeb 提供"
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
    
    
    private val mWebViewClient: WebViewClient = object : WebViewClient() {

        override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
            //获取url
            val url = request!!.url
            LogUtil.d("url---$url")
            /**
             * 一个URL通常由以下几个部分构成：协议、域名、端口、路径和URL地址参数。
             * https://baike.baidu.com/item/android/60243
             * uri.scheme 协议 https
             * uri.authority 域名 baike.baidu.com
             * uri.path 路径 item/android/60243
             * uri.queryParameterNames 参数 null
             * uri.getQueryParameter("id")
             */
            return super.shouldOverrideUrlLoading(view, request)
        }


        override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
            super.onPageStarted(view, url, favicon)
            LogUtil.d("url---$url")
        }

        override fun onPageFinished(view: WebView?, url: String?) {
            super.onPageFinished(view, url)
            LogUtil.d("url---$url")
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
