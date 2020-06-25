package com.yyxnb.module_video.fragments;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yyxnb.adapter.BaseFragmentPagerAdapter;
import com.yyxnb.arch.annotations.BarStyle;
import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.common.Bus;
import com.yyxnb.arch.common.MsgEvent;
import com.yyxnb.common.DpUtils;
import com.yyxnb.common.log.LogUtils;
import com.yyxnb.module_base.base.BaseFragment;
import com.yyxnb.module_base.utils.DrawableTintUtil;
import com.yyxnb.module_video.R;
import com.yyxnb.module_video.databinding.FragmentVideoUserBinding;
import com.yyxnb.module_video.widget.AppBarStateListener;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.yyxnb.module_base.config.Constants.KEY_VIDEO_BOTTOM_VP;
import static com.yyxnb.module_base.config.Constants.KEY_VIDEO_BOTTOM_VP_SWITCH;

/**
 * 视频 - 个人页面
 */
@BindRes(statusBarStyle = BarStyle.LightContent)
public class VideoUserFragment extends BaseFragment {

    private FragmentVideoUserBinding binding;
    private MagicIndicator mIndicator;
    private ViewPager mViewPager;
    private AppBarLayout mAppBarLayout;
    private RelativeLayout mTitleLayout;
    private TextView mTitleName;

    private String[] titles = {"作品", "动态", "喜欢"};
    private List<Fragment> fragments;
    private boolean isUser;
    private boolean mAppBarExpand = true;//AppBarLayout是否展开

    public static VideoUserFragment newInstance(boolean isUser) {

        Bundle args = new Bundle();
        args.putBoolean("isUser", isUser);
        VideoUserFragment fragment = new VideoUserFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int initLayoutResId() {
        return R.layout.fragment_video_user;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        binding = getBinding();
        mIndicator = binding.mIndicator;
        mViewPager = binding.mViewPager;
        mAppBarLayout = binding.mAppBarLayout;
        mTitleLayout = binding.mTitleLayout;
        mTitleName = binding.mTitleName;

    }

    @Override
    public void initViewData() {

        if (fragments == null) {
            fragments = new ArrayList<>();
            fragments.add(new VideoListFragment());
            fragments.add(new VideoListFragment());
            fragments.add(new VideoListFragment());
        }

        binding.btnBack.setOnClickListener(v -> {
            Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP_SWITCH, 0));
        });

        initIndicator();
    }

    @Override
    public void initObservable() {


        mAppBarLayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            float totalScrollRange = appBarLayout.getTotalScrollRange();
            float rate = -1 * verticalOffset / totalScrollRange;
//                mTitle.setAlpha(rate);
            mTitleName.setAlpha(rate);
//            Drawable compat = DrawableCompat.wrap(binding.btnBack.getDrawable());
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                DrawableCompat.setTintList(compat, ColorStateList.valueOf(Color.argb(rate,0,0,0)));
//            }
            Log.w("000000", "r : " + rate);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                if (rate <= 0.2f){
                    binding.btnBack.setImageDrawable(DrawableTintUtil.tintDrawable(binding.btnBack.getDrawable(),Color.WHITE));
                }else {
                    binding.btnBack.setImageDrawable(DrawableTintUtil.tintListDrawable(binding.btnBack.getDrawable()
                            , ColorStateList.valueOf(Color.argb(rate, 0, 0, 0))));
                }
            }
        });

        mAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int state) {
                switch (state) {
                    case AppBarStateListener.EXPANDED:
                        //L.e("#mAppBarLayout--------->展开");
                        mAppBarExpand = true;
                        break;
                    case AppBarStateListener.COLLAPSED:
                        //L.e("#mAppBarLayout--------->收起");
                        mAppBarExpand = false;
                        break;
                }
            }
        });
    }

    @Override
    public void onVisible() {
//        isUser = getArguments().getBoolean("isUser", false);
//        binding.btnBack.setVisibility(isUser ? View.GONE : View.VISIBLE);
        Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP, false));
        LogUtils.w("user v");
//        if (!isUser && binding.btnBack.getVisibility() == View.VISIBLE){
//            monitor(true);
//        }else {
//            getView().setOnKeyListener(null);
//            monitor(false);
//        }
    }

    @Override
    public void onInVisible() {
        Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP, true));
        LogUtils.w("user iv");
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @SuppressWarnings("ConstantConditions")
    private void monitor(boolean b) {
        if (b) {
            getView().setFocusableInTouchMode(true);
            getView().requestFocus();
            getView().setOnKeyListener((v, keyCode, event) -> {
                if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
                    // 监听到返回按钮点击事件
                    Bus.post(new MsgEvent(KEY_VIDEO_BOTTOM_VP_SWITCH, 0));
                    return true;
                }
                return false;
            });
        } else {
            getView().setFocusableInTouchMode(false);
            getView().setOnKeyListener(null);
        }
    }

    private void initIndicator() {
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        //ture 即标题平分屏幕宽度的模式
        commonNavigator.setAdjustMode(true);
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {

            @Override
            public int getCount() {
                return titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                ColorTransitionPagerTitleView colorTransitionPagerTitleView = new ColorTransitionPagerTitleView(context);
                colorTransitionPagerTitleView.setNormalColor(Color.GRAY);
                colorTransitionPagerTitleView.setSelectedColor(Color.BLACK);
                colorTransitionPagerTitleView.setText(titles[index]);
                colorTransitionPagerTitleView.setOnClickListener(view -> mViewPager.setCurrentItem(index));
                return colorTransitionPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                //设置宽度
                indicator.setLineWidth(DpUtils.dp2px(getContext(), 20));
                //设置高度
                indicator.setLineHeight(DpUtils.dp2px(getContext(), 2));
                //设置颜色
                indicator.setColors(Color.BLACK);
                //设置圆角
                indicator.setRoundRadius(5);
                //设置模式
                indicator.setMode(LinePagerIndicator.MODE_EXACTLY);
                return indicator;
            }
        });
        mIndicator.setNavigator(commonNavigator);

        mViewPager.setOffscreenPageLimit(titles.length - 1);
        mViewPager.setAdapter(new BaseFragmentPagerAdapter(getChildFragmentManager(), fragments, Arrays.asList(titles)));
        //与ViewPagger联动
        ViewPagerHelper.bind(mIndicator, mViewPager);
    }
}