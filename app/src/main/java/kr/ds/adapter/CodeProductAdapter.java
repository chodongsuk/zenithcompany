package kr.ds.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.kr.zenithcompany.R;

import java.util.ArrayList;

import kr.ds.handler.CodeProductHandler;

/**
 * Created by Administrator on 2017-05-01.
 */
public class CodeProductAdapter implements SpinnerAdapter {

    private Context context;
    private ArrayList<CodeProductHandler> mData;

    public CodeProductAdapter(Context context, ArrayList<CodeProductHandler> data){
        this.context = context;
        this.mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }
    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return 1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = vi.inflate(android.R.layout.simple_spinner_dropdown_item, null);
        }
        convertView.setBackgroundResource(R.drawable.spinner_down_box);
        ((TextView) convertView).setText(mData.get(position).getName());
        ((TextView) convertView).setTextColor(0xff555555);
        ((TextView) convertView).setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        ((TextView) convertView).setPadding(dipToInt(10), dipToInt(10), dipToInt(10), dipToInt(10));
        return convertView;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView textView = (TextView) View.inflate(context, android.R.layout.simple_spinner_item, null);
        textView.setTextColor(0xff555555);
        textView.setText(mData.get(position).getName());
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 11);
        textView.setPadding(dipToInt(10), dipToInt(3), dipToInt(10), dipToInt(3));
        return textView;
    }


    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {

    }

    public int dipToInt(int number) {
        int num = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                number, context.getResources().getDisplayMetrics());
        return num;
    }



}