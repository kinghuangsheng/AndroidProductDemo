package com.product.demo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.product.demo.R;
import com.product.demo.greendao.entity.Assets;

import java.util.List;

/**
 * Created by huangsheng on 2018/9/8.
 */

public class AssetsListAdapter extends AssetsListBaseAdapter<Assets> {


    public AssetsListAdapter(Context context, List<Assets> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = inflateView(R.layout.item_import_list);
            viewHolder = new ViewHolder();
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.barCodeTV = (TextView) convertView.findViewById(R.id.tv_bar_code);
            viewHolder.positionIV = (TextView) convertView.findViewById(R.id.tv_position);
            viewHolder.statusIV = (TextView) convertView.findViewById(R.id.tv_status);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Assets assets = getItem(position);

        viewHolder.nameTV.setText(assets.getName());
        viewHolder.barCodeTV.setText(assets.getBarCode());
        viewHolder.positionIV.setText(assets.getPosition());
        if(assets.inventorySuccess()){
            viewHolder.statusIV.setText("√");
        }else{
            viewHolder.statusIV.setText("×");
        }

        return convertView;
    }

    static class ViewHolder
    {
        private TextView nameTV;

        private TextView barCodeTV;

        private TextView positionIV;

        private TextView statusIV;


    }
}