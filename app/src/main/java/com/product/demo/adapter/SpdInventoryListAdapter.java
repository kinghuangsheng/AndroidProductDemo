package com.product.demo.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.product.demo.greendao.entity.Assets;
import com.product.zcpd.R;
import com.speedata.libuhf.bean.SpdInventoryData;

import java.util.List;

/**
 * Created by huangsheng on 2018/9/8.
 */

public class SpdInventoryListAdapter extends AssetsListBaseAdapter<SpdInventoryData> {


    public SpdInventoryListAdapter(Context context, List<SpdInventoryData> data) {
        super(context, data);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder viewHolder;
        if (convertView == null)
        {
            convertView = inflateView(R.layout.item_spd_inventory_list);
            viewHolder = new ViewHolder();
            viewHolder.epcTV = (TextView) convertView.findViewById(R.id.tv_epc);
            convertView.setTag(viewHolder);
        }
        else
        {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        SpdInventoryData spdInventoryData = getItem(position);
        viewHolder.epcTV.setText(spdInventoryData.getEpc());

        return convertView;
    }

    static class ViewHolder
    {
        private TextView epcTV;

    }
}
