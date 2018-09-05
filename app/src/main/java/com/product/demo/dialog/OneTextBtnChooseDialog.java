package com.product.demo.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.product.demo.R;

import base.android.BaseDialog;
import base.dagger2.component.DialogComponent;


public class OneTextBtnChooseDialog extends BaseDialog implements
    View.OnClickListener
{
    private View.OnClickListener listener;
    
    private String btnStr;
    
    private String msg;
    
    public OneTextBtnChooseDialog(Context context, int msgResId,
        int intBtnStrResId, View.OnClickListener leftListener)
    {
        this(context, context.getString(msgResId),
            context.getString(intBtnStrResId), leftListener);
    }
    
    public OneTextBtnChooseDialog(Context context, String msg, String btnStr,
        View.OnClickListener listener)
    {
        super(context);
        this.msg = msg;
        this.btnStr = btnStr;
        this.listener = listener;
    }
    
    @Override
    public void onCreate() {
        ((TextView) findViewById(R.id.tv_msg)).setText(msg);
        Button btn = (Button) findViewById(R.id.btn);
        btn.setText(btnStr);
        btn.setOnClickListener(this);
    }

    @Override
    public int getLayoutViewID() {
        return R.layout.dialog_one_btn_choose;
    }

    @Override
    public void dagger2Inject(DialogComponent component) {
        component.inject(this);
    }

    public void setTextMsgConnet()
    {
        ((TextView) findViewById(R.id.tv_msg)).setGravity(Gravity.CENTER);
    }
    
    public void setMsg(String msg)
    {
        
        ((TextView) findViewById(R.id.tv_msg)).setText(msg);
    }
    
    public void setBtnStr(String btnStr)
    {
        ((TextView) findViewById(R.id.btn)).setText(btnStr);
    }
    
    @Override
    public void onClick(View v)
    {
        dismiss();
        switch (v.getId())
        {
            case R.id.btn:
                if (listener != null)
                {
                    listener.onClick(v);
                }else{
                    dismiss();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
    }
}
