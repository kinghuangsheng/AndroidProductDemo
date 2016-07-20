package base.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.List;

public abstract class BaseListAdapter<T> extends BaseAdapter {

	private Context context;

	public List<T> getData() {
		return data;
	}

	private List<T> data;


	protected BaseListAdapter(Context context, List<T> data){
		this.context = context;
		this.data = data;
	}
	
	@Override
	public int getCount() {
		synchronized (data) {
			return data.size();
		}
		
	}

	@Override
	public T getItem(int position) {
		synchronized (data) {
			return data.get(position);
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

//	public void setData(List<T> data){
//		synchronized (data) {
//			this.data = data;
//		}
//	}
	
	public View inflateView(int resId) {
		return LayoutInflater.from(context).inflate(resId, null);
	}
	
	public Context getContext() {
		return context;
	}

	
}
