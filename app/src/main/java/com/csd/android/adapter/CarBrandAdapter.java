package com.csd.android.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.csd.android.R;
import com.csd.android.activity.SelectCarBrandActivity;
import com.csd.android.activity.TaskDetailActivity;
import com.csd.android.model.CarBrand;
import com.csd.android.widget.PickerPop;
import com.csd.android.widget.PickerPop.OnConfirmListener;

import java.util.ArrayList;

public class CarBrandAdapter extends BaseAdapter {
	private ArrayList<CarBrand> list;
	private ArrayList<CarBrand> list_filter = new ArrayList<CarBrand>();

	private SelectCarBrandActivity context;

	public CarBrandAdapter(SelectCarBrandActivity context, ArrayList<CarBrand> list) {
		this.context = context;
		this.list = list;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private AdapterFilter adapterFilter;

	public Filter getFilter() {
		if (null == adapterFilter) {
			adapterFilter = new AdapterFilter();
		}
		return adapterFilter;
	}

	public int getSectionForPosition(int position) {
		return list.get(position).getLetter().charAt(0);
	}

	public int getPositionForSection(int section) {
		int result = -1;
		for (int i = 0; i < getCount(); i++) {
			int firstChar = getSectionForPosition(i);
			if (firstChar == section) {
				result = i;
				break;
			}
		}
		return result;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		final CarBrand entity = list.get(position);
		if (null == convertView) {
			convertView = LayoutInflater.from(context).inflate(R.layout.item_select_car_brand, null);
			holder = new ViewHolder();
			holder.alpha = (TextView) convertView.findViewById(R.id.city_select_alpha_text);
			holder.name = (TextView) convertView.findViewById(R.id.city_select_name);
			convertView.setTag(holder);
		}
		else {
			holder = (ViewHolder) convertView.getTag();
		}

		int section = getSectionForPosition(position);
		if (position == getPositionForSection(section)) {
			holder.name.setBackgroundDrawable(null);
			holder.alpha.setVisibility(View.VISIBLE);
			holder.alpha.setText(String.valueOf((char) section));
		}
		else {
			holder.name.setBackgroundResource(R.drawable.border_top_gray_solid_white);
			holder.alpha.setVisibility(View.GONE);
		}
		holder.name.setText(entity.getBrandName());

		holder.name.setOnClickListener(new OnClickListener() {
			private PickerPop pop;
			private String[] strs;

			@Override
			public void onClick(View v) {
				if (pop == null) {
					strs = new String[list.get(position).getCarType().size()];
					for (int i = 0; i < strs.length; i++) {
						strs[i] = list.get(position).getCarType().get(i).getTypeName();
					}
					pop = new PickerPop(context, strs);
					pop.setOnConfirmListener(new OnConfirmListener() {
						public void onConfirmClick(int... results) {
							Intent intent = new Intent();
							intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_TYPE_ID, list.get(position).getCarType().get(results[0])
									.getTypeId());
							intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_BRAND_NAME, list.get(position).getBrandName());
							intent.putExtra(TaskDetailActivity.INTENT_EXTRA_NAME_CAR_TYPE_NAME, list.get(position).getCarType().get(results[0])
									.getTypeName());
							context.setResult(Activity.RESULT_OK, intent);
							context.finish();
						}
					});
				}
				pop.show();
			}
		});

		return convertView;
	}

	final static class ViewHolder {
		TextView alpha;
		TextView name;
	}

	private class AdapterFilter extends Filter {

		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			FilterResults results = null;
			if (constraint != null && constraint.length() > 0) {
				results = new FilterResults();
				list_filter.clear();
				for (int i = 0; i < context.list.size(); i++) {
					if (context.list.get(i).getBrandName().contains(constraint)) {
						list_filter.add(context.list.get(i));
					}
				}

				results.values = list_filter;
				results.count = list_filter.size();
			}

			return results;
		}

		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			if (null == results || null == results.values) {
				list = context.list;
			}
			else {
				list = (ArrayList<CarBrand>) results.values;
			}
			notifyDataSetChanged();
		}

	}
}
