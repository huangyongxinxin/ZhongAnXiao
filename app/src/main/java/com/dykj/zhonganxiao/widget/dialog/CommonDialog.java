package com.dykj.zhonganxiao.widget.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.dykj.zhonganxiao.R;
import com.flyco.dialog.widget.base.BaseDialog;

public class CommonDialog extends BaseDialog {
    private TextView tvCancel;
    private TextView tvConfirm;
    private TextView mTitle;
    private TextView mContent;
    private String title = "温馨提示";
    private String content;
    private String leftContent = "否";
    private String rightContent = "是";
    OnCallBack callBack;

    public CommonDialog(Context context) {
        super(context);
    }

    @Override
    public View onCreateView() {
        widthScale(0.75f);
        View view = View.inflate(mContext, R.layout.dialog_norm, null);
        tvCancel = (TextView) view.findViewById(R.id.tvCancel);
        tvConfirm = (TextView) view.findViewById(R.id.tvConfirm);
        mContent = (TextView) view.findViewById(R.id.mContent);
        mTitle = (TextView) view.findViewById(R.id.mTitle);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onCancel();

            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callBack != null)
                    callBack.onConfirm();

            }
        });
        return view;
    }

    @Override
    public void setUiBeforShow() {
        mTitle.setText(title);
        if (!TextUtils.isEmpty(content)) {
            mContent.setText(content);
            mContent.setVisibility(View.VISIBLE);
        } else {
            mContent.setVisibility(View.GONE);
        }
        tvCancel.setText(leftContent);
        tvConfirm.setText(rightContent);
    }


    public CommonDialog title(String title) {
        this.title = title;
        return this;
    }

    public CommonDialog content(String content) {
        this.content = content;
        return this;
    }

    public CommonDialog leftContent(String leftContent) {
        this.leftContent = leftContent;
        return this;
    }

    public CommonDialog rightContent(String rightContent) {
        this.rightContent = rightContent;
        return this;
    }

    public interface OnCallBack {
        void onCancel();

        void onConfirm();
    }

    public void setOnCallBack(OnCallBack callBack) {
        this.callBack = callBack;
    }
}
