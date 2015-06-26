package com.csd.android.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.csd.android.R;
import com.csd.android.activity.SelectAddressActivity;

public class SelectAddressAdapter extends BaseAdapter {
	private ArrayList<PoiInfo> list;
	private SelectAddressActivity context;

	public SelectAddressAdapter(SelectAddressActivity context, ArrayList<PoiInfo> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private class ViewHolder {
		TextView name, address;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.map_item, null);
			TextView poiName = (TextView) view.findViewById(R.id.poiName);
			TextView poiAddress = (TextView) view.findViewById(R.id.poiaddress);
			holder = new ViewHolder();
			holder.address = poiAddress;
			holder.name = poiName;
			view.setTag(holder);

		}
		else {
			view = convertView;
			holder = (ViewHolder) view.getTag();
		}
		PoiInfo poiInfo = list.get(position);
		holder.address.setText(poiInfo.address);
		holder.name.setText(poiInfo.name);
		return view;
	}

}
