package com.yyxnb.module_wanandroid.ui;

import android.databinding.DataBindingUtil;
import android.os.Bundle;

import com.yyxnb.arch.annotations.BindRes;
import com.yyxnb.arch.base.IActivity;
import com.yyxnb.module_wanandroid.R;
import com.yyxnb.module_wanandroid.databinding.ActivityWanWebBinding;
import com.yyxnb.view.titlebar.TitleBar;
import com.yyxnb.webview.WebActivity;

@BindRes
public class WanWebActivity extends WebActivity implements IActivity {

    private ActivityWanWebBinding binding;
    private TitleBar mTitle;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_wan_web);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        mTitle = binding.iTitle.mTitle;

        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");

        mTitle.getCenterTextView().setText(title);
        mTitle.setBackListener(v -> finish());

        mLinearLayout = findViewById(R.id.mLinearLayout);


        initAgent(url, mLinearLayout);
    }
}