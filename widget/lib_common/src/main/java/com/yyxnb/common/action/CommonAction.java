package com.yyxnb.common.action;

import com.yyxnb.common.CommonManager;
import com.yyxnb.common.utils.log.LogUtils;
import com.yyxnb.widget.action.WidgetAction;

import java.util.List;

/**
 * 通用意图
 *
 * @author yyx
 */
public interface CommonAction extends WidgetAction {

    /**
     * toast
     *
     * @param text
     */
    default void toast(CharSequence text) {
        CommonManager.getInstance().toast(text.toString());
    }

    /**
     * log
     *
     * @param tag
     * @param text
     */
    default void log(String tag, CharSequence text) {
        CommonManager.getInstance().log(tag, text.toString());
    }

    /**
     * log
     *
     * @param text
     */
    default void log(CharSequence text) {
        CommonManager.getInstance().log(text.toString());
    }

    /**
     * log e
     *
     * @param text
     */
    default void loge(CharSequence text) {
        LogUtils.e(text.toString());
    }

    /**
     * log list
     *
     * @param list 集合
     */
    default void list(List list) {
        LogUtils.list(list);
    }
}
