package com.ve.lib.common.base.viewmodel

import androidx.lifecycle.*
import com.google.gson.JsonParseException
import com.ve.lib.common.network.exception.ApiException
import com.ve.lib.common.utils.log.LogUtil
import com.ve.lib.common.utils.view.ToastUtil
import kotlinx.coroutines.*

import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * @author waynie
 * @date 2022/8/22
 * @desc BaseViewModel
 * 1、数据持久化
我们知道在屏幕旋转的 时候 会经历 activity 的销毁与重新创建，这里就涉及到数据保存的问题，显然重新请求或加载数据是不友好的。在 ViewModel 出现之前我们可以用 activity 的onSaveInstanceState()机制保存和恢复数据，但缺点很明显，onSaveInstanceState只适合保存少量的可以被序列化、反序列化的数据，假如我们需要保存是一个比较大的 bitmap list ，这种机制明显不合适。
由于 ViewModel 的特殊设计，可以解决此痛点。

作者：以帅服人的珂哥
链接：https://www.jianshu.com/p/35d143e84d42
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 * (activity 与其内部的 fragment 可以共用一个ViewModel)
 */
open class BaseViewModel : ViewModel() {
    private val mViewModelName by lazy { javaClass.simpleName }

    /**********************************/

    val needLogin = MutableLiveData<Boolean>().apply { value = false }

    /**
     * 在viewModel层处理bean
     * 创建并执行协程 Coroutines
     * @param block 协程中执行
     * @param error 错误时执行
     * @param cancel 取消时执行
     * @param showErrorToast 是否弹出错误吐司
     * @return Job API 文档 https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines/-job/index.html
     *
     * CoroutineScope.launch 函数返回的是一个 Job 对象，代表一个异步的任务。
     * viewModelScope 也是继承 CoroutineScope的
     * Job 具有生命周期并且可以取消。
     * Job 还可以有层级关系，一个Job可以包含多个子Job，当父Job被取消后，所有的子Job也会被自动取消；
     * 当子Job被取消或者出现异常后父Job也会被取消。
     */
    protected fun launch(
        block: suspend CoroutineScope.() -> Unit,
        then: (suspend CoroutineScope.() -> Unit)? = null,
        error: (suspend (Exception) -> Unit)? = null,
        cancel: (suspend (Exception) -> Unit)? = null,
        showErrorToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            try {
                //apiCall,返回BaseResponse
                block.invoke(this)
            } catch (e: Exception) {
                LogUtil.msg(e.message)
                //处理错误
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            } finally {
                then?.invoke(this)
            }
        }
    }

    /**
     * 运行在主线程，主要进行ui操作
     * Room 不允许在主线程进行数据库存在
     * */
    protected fun launchMain(
        block: suspend CoroutineScope.() -> Unit,
        error: (suspend (Exception) -> Unit)? = null,
        cancel: (suspend (Exception) -> Unit)? = null,
        showErrorToast: Boolean = true
    ): Job {
        return MainScope().launch(Dispatchers.IO) {
            try {
                //apiCall,返回BaseResponse
                block.invoke(this)
            } catch (e: Exception) {
                //处理错误
                when (e) {
                    is CancellationException -> {
                        cancel?.invoke(e)
                    }
                    else -> {
                        onError(e, showErrorToast)
                        error?.invoke(e)
                    }
                }
            }
        }
    }

    /**
     * 创建并执行协程
     * @param block 协程中执行
     * @return Deferred<T> 继承自 Job 额外多3个方法
     */
    protected fun <T> async(block: suspend CoroutineScope.() -> T): Deferred<T> {
        return viewModelScope.async { block.invoke(this) }
    }

    /**
     * 取消协程 会抛出CancellationException
     * @param job 协程job
     */
    protected fun cancelJob(job: Job?) {
        if (job != null && job.isActive && !job.isCompleted && !job.isCancelled) {
            job.cancel()
        }
    }

    /**
     * 统一处理错误
     * @param e 异常
     * @param showErrorToast 是否显示错误吐司
     */
    private fun onError(e: Exception, showErrorToast: Boolean) {
        LogUtil.msg(e.message)
        e.printStackTrace()
        when (e) {
            is ApiException -> {
                when (e.errorCode) {
                    -1001 -> {
                        if (showErrorToast) ToastUtil.show(e.errorMsg)
                    }
                    // 其他错误
                    else -> {
                        if (showErrorToast) ToastUtil.show(e.errorMsg)
                    }
                }
            }
            // 网络请求失败
            is ConnectException, is SocketTimeoutException, is UnknownHostException, is retrofit2.HttpException -> {
                if (showErrorToast) ToastUtil.show(" 网络请求失败")
            }
            // 数据解析错误
            is JsonParseException -> {
                if (showErrorToast) ToastUtil.show(" 数据解析错误")
            }
            // 其他错误
            else -> {
                if (showErrorToast) ToastUtil.show(e.message ?: return)
            }

        }
    }

    /**
     * json提交 转RequestBody （表单提交 @FieldMap）
     */
//    protected fun toRequestBody(params: Any?): RequestBody {
//        return Gson().toJson(params).toRequestBody("application/json".toMediaTypeOrNull())
//    }
    /**********************************/
}
