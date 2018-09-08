package com.product.demo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.List;

import base.util.UIUtil;

public abstract class AssetsListBaseAdapter<T> extends BaseAdapter
{

    private Context context;

    private List<T> data;

    protected AssetsListBaseAdapter(Context context, List<T> data)
    {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount()
    {
        synchronized (data)
        {
            return data.size();
        }

    }

    @Override
    public T getItem(int position)
    {
        synchronized (data)
        {
            return data.get(position);
        }
    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public View inflateView(int resId)
    {
        View v = LayoutInflater.from(context).inflate(resId, null);
        UIUtil.viewScreenAdapter(v);
        return v;
    }

    public Context getContext()
    {
        return context;
    }
    
}
