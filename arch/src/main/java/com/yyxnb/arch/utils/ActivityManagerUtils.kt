package com.yyxnb.arch.utils

import android.annotation.SuppressLint
import android.content.Context
import com.yyxnb.arch.base.BaseActivity
import com.yyxnb.arch.interfaces.OnActivityStatusChangeListener
import java.io.Serializable
import java.util.*

object ActivityManagerUtils :Serializable{

    private var onActivityStatusChangeListener: OnActivityStatusChangeListener? = null
    private var activityStack: Stack<BaseActivity>? = null

    fun setOnActivityStatusChangeListener(onActivityStatusChangeListener: OnActivityStatusChangeListener) {
        ActivityManagerUtils.onActivityStatusChangeListener = onActivityStatusChangeListener
    }

    /**
     * 获取当前Activity栈中元素个数
     */
    val count: Int
        get() = activityStack!!.size

    /**
     * 添加Activity到堆栈
     */
    fun pushActivity(activity: BaseActivity) {
        if (activityStack == null) {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
        onActivityStatusChangeListener?.onActivityCreate(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): BaseActivity? {
        var activity: BaseActivity? = null
        if (!activityStack!!.empty()) {
            activity = activityStack!!.lastElement()
        }
        return activity
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity() {
        val activity = activityStack!!.lastElement()
        killActivity(activity)
    }

    /**
     * 结束指定的Activity
     */
    fun killActivity(activity: BaseActivity?) {
        var _activity = activity
        if (_activity != null) {
            _activity.finish()
            activityStack!!.remove(_activity)
            _activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun killActivity(cls: Class<*>) {
        val iterator = activityStack!!.iterator()
        var temp: BaseActivity? = null
        while (iterator.hasNext()) {
            val activity = iterator.next()
            if (activity != null && activity.javaClass == cls) {
                temp = activity
            }
        }
        temp?.finish()
    }

    /**
     * 结束所有Activity
     */
    fun killAllActivity() {
        if (activityStack != null) {
            while (!activityStack!!.empty()) {
                val activity = currentActivity()
                killActivity(activity)
            }
            activityStack!!.clear()
        }
    }

    /**
     * 退出应用程序
     */
    @SuppressLint("MissingPermission")
    fun exit(context: Context) {
        try {
            killAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr.killBackgroundProcesses(context.packageName)
            System.exit(0)
        } catch (e: Exception) {
        }

    }

    @Deprecated("")
    fun AppExit(context: Context) {
        exit(context.applicationContext)
    }

    /**
     * 关闭指定 Activity
     */
    fun deleteActivity(activity: BaseActivity?) {
        if (activity != null) {
            activityStack!!.remove(activity)
            onActivityStatusChangeListener?.onActivityDestroy(activity)
            System.gc()
        }
    }

    /**
     * 关闭所有 [Activity],排除指定的 [Activity]
     *
     * @param cls activity
     */
    fun killOtherActivityExclude(vararg cls: Class<*>) {
        val excludeList = listOf(*cls)
        if (activityStack != null) {
            val activityStackCache = Stack<BaseActivity>()
            activityStackCache.addAll(activityStack!!)
            val iterator = activityStackCache.iterator()
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (activity != null && excludeList.contains(activity.javaClass)) {
                    continue
                }
                iterator.remove()
                activity?.finish()
            }
        }
    }

    /**
     * 根据类名获取堆栈中最后一个 activity 实例
     *
     * @param cls activity
     */
    fun getActivityInstance(cls: Class<*>): BaseActivity? {
        if (!activityStack!!.empty()) {
            val reverseList = Stack<BaseActivity>()
            reverseList.addAll(activityStack!!)
            Collections.reverse(reverseList)
            val iterator = reverseList.iterator()
            var temp: BaseActivity? = null
            while (iterator.hasNext()) {
                val activity = iterator.next()
                if (activity != null && activity.javaClass == cls) {
                    temp = activity
                }
            }
            if (temp != null) {
                return temp
            }
        }
        return null
    }

    fun onDestroy() {
        activityStack = Stack()
    }

}