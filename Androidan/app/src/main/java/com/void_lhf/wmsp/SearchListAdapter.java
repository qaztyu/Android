package com.void_lhf.wmsp;
import android.content.*;
import android.view.*;
import android.widget.*;
import java.util.*;

public class SearchListAdapter extends BaseAdapter
{
    private Context mContext;  
    private List<SearchListItem> mDatas1;
	private List<SearchListItem> mDatas2 = new ArrayList<SearchListItem>(); 
	
	public SearchListAdapter(Context context,List<SearchListItem> mDatas) {
        this.mContext = context;  
        this.mDatas1 = mDatas;
		this.mDatas2.addAll(this.mDatas1);
	}
	
	@Override
	public int getCount()
	{
		// TODO: Implement this method
		//return mDatas.size();
		return mDatas2 == null ? 0 : mDatas2.size();
	}

	@Override
	public Object getItem(int p1)
	{
		// TODO: Implement this method
		return mDatas2.get(p1);
	}

	@Override
	public long getItemId(int p1)
	{
		// TODO: Implement this method
		return p1;
	}

	@Override
	public void notifyDataSetChanged()
	{
		// TODO: Implement this method
		this.mDatas2.clear();
		this.mDatas2.addAll(mDatas1);
		super.notifyDataSetChanged();
	}
	

	@Override
	public View getView(int p1, View p2, ViewGroup p3)
	{
		SearchListItem item = (SearchListItem) getItem(p1);
		View view;
		ViewHolder viewHolder;
		if(p2==null) {
			view = LayoutInflater.from(mContext).
				inflate(R.layout.search_list_item,p3,false);
			viewHolder = new ViewHolder();
			viewHolder.textView1 = view.findViewById(R.id.searchlistitemTextView1);
			viewHolder.textView2 = view.findViewById(R.id.searchlistitemTextView2);
			view.setTag(viewHolder);
		} else {
			view = p2;
			viewHolder = (SearchListAdapter.ViewHolder) view.getTag();
		}
		viewHolder.textView1.setText(item.getItemName());
		viewHolder.textView2.setText(item.getItemDescription());
		return view;
	}
	
	class ViewHolder {
		TextView textView1;
		TextView textView2;
	}
}
