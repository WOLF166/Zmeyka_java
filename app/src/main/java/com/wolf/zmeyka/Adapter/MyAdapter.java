package com.wolf.zmeyka.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wolf.zmeyka.Model.Record;
import com.wolf.zmeyka.R;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter {

    Context context;
    ArrayList<Record> arrayList;
    public MyAdapter(Context context, ArrayList<Record> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return arrayList.get(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(R.layout.mycustomlistview, null);
        TextView t1_id = (TextView) convertView.findViewById(R.id.id_txt);
        TextView t2_name = (TextView) convertView.findViewById(R.id.name_txt);
        TextView t3_record = (TextView) convertView.findViewById(R.id.record_txt);



        Record record = arrayList.get(position);

        t1_id.setText(String.valueOf(record.getId()));
        t2_name.setText(record.getName());
        t3_record.setText(String.valueOf(record.getRecord()));


        return convertView;
    }

    @Override
    public int getCount() {
        return this.arrayList.size();
    }
}
