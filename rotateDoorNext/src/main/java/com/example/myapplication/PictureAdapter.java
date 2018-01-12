package com.example.myapplication;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 * 图片列表的适配器
 * 
 * @author guolin
 */
public class PictureAdapter extends ArrayAdapter<Picture> {

	public PictureAdapter(Context context, int textViewResourceId, List<Picture> objects) {
		super(context, textViewResourceId, objects);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Picture picture = getItem(position);
		View view;
		if (convertView == null) {
			view = LayoutInflater.from(getContext()).inflate(android.R.layout.simple_list_item_1,
					null);
		} else {
			view = convertView;
		}
		TextView text1 = (TextView) view.findViewById(android.R.id.text1);
		text1.setText(picture.getName());
		return view;
	}

}
