package kr.ds.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.kr.zenithcompany.R;

import java.util.ArrayList;

import kr.ds.handler.ListHandler;
import kr.ds.utils.DsObjectUtils;

/**
 * Created by Administrator on 2016-08-31.
 */
public class ListAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ListHandler> mData;
    private LayoutInflater mInflater;

    public ListAdapter(Context context, ArrayList<ListHandler> data) {
        mContext = context;
        mData = data;
        mInflater = LayoutInflater.from(mContext);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.fragment_list1_item1,null);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!DsObjectUtils.isEmpty(mData.get(position).getTitle())){
            holder.textViewTitle.setVisibility(View.VISIBLE);
            holder.textViewTitle.setText(mData.get(position).getTitle());
        }else {
            holder.textViewTitle.setVisibility(View.GONE);
            holder.textViewTitle.setText("");
        }
        return convertView;
    }


    class ViewHolder {
        TextView textViewTitle;
    }

}