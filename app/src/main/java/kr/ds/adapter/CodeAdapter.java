package kr.ds.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.kr.zenithcompany.R;
import com.kr.zenithcompany.SettingActivity;

import java.util.ArrayList;

import kr.ds.config.Config;
import kr.ds.handler.CodeHandler;
import kr.ds.utils.DsObjectUtils;
import kr.ds.utils.SharedPreference;

/**
 * Created by Administrator on 2016-08-31.
 */
public class CodeAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<CodeHandler> mData;
    private LayoutInflater mInflater;

    public CodeAdapter(Context context, ArrayList<CodeHandler> data) {
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
        return mData.get(position);
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
            convertView = mInflater.inflate(R.layout.activity_setting_item,null);
            holder.textViewTitle = (TextView) convertView.findViewById(R.id.textView_title);
            holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if(!DsObjectUtils.isEmpty(mData.get(position).getName())){
            holder.textViewTitle.setVisibility(View.VISIBLE);
            holder.textViewTitle.setText(mData.get(position).getName());
        }else {
            holder.textViewTitle.setVisibility(View.GONE);
            holder.textViewTitle.setText("");
        }
        if(mData.get(position).isCheck()){
            holder.checkBox.setChecked(true);
        }else{
            holder.checkBox.setChecked(false);
        }
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(((CodeHandler) getItem(position)).isCheck()){
                    mData.get(position).setCheck(false);
                }else{
                    mData.get(position).setCheck(true);
                }
                setCodes();

            }
        });

        return convertView;
    }

    class ViewHolder {
        TextView textViewTitle;
        CheckBox checkBox;
    }

    public void setCodes(){
        String codes = "";
        int c = 0;
        for(int i=0; i<mData.size(); i++){
            if(mData.get(i).isCheck()){
                c++;
                codes += mData.get(i).getCode()+",";
            }
        }
        if(codes.length() > 0) {
            codes = codes.substring(0, codes.length() - 1);
        }
        Log.i("TEST",codes+"");
        if(c == mData.size()){
            if(SettingActivity.mCheckBoxAll != null) {
                SettingActivity.mCheckBoxAll.setChecked(true);
            }
            Log.i("TEST","all");
            SharedPreference.putSharedPreference(mContext, Config.ALL_CODE, true);
        }else{
            if(SettingActivity.mCheckBoxAll != null) {
                SettingActivity.mCheckBoxAll.setChecked(false);
            }
            Log.i("TEST","noall");
            SharedPreference.putSharedPreference(mContext, Config.ALL_CODE, false);
        }

        SharedPreference.putSharedPreference(mContext, Config.CODES, codes);
        notifyDataSetChanged();
    }

}