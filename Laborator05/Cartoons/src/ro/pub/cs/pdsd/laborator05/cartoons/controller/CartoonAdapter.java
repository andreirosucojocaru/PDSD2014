package ro.pub.cs.pdsd.laborator05.cartoons.controller;

import java.util.ArrayList;

import ro.pub.cs.pdsd.laborator05.cartoons.R;
import ro.pub.cs.pdsd.laborator05.cartoons.model.Cartoon;
import ro.pub.cs.pdsd.laborator05.cartoons.utils.PictureFinder;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CartoonAdapter extends BaseAdapter {
	
	private ArrayList<Cartoon> content;
	private Activity context;
	
	public CartoonAdapter(Activity context, ArrayList<Cartoon> content) {
		this.content = content;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return content.size();
	}
	
	@Override
	public Object getItem(int position) {
		return content.get(position);
	}
	
	@Override
	public long getItemId(int position) {
		return 0;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater layoutInflater = context.getLayoutInflater();
		Cartoon cartoonContent = (Cartoon)getItem(position);
		if (position % 2 == 0)
			convertView = layoutInflater.inflate(R.layout.cartoon1, parent, false);
		else
			convertView = layoutInflater.inflate(R.layout.cartoon2, parent, false);
		ImageView imageView1 = (ImageView)convertView.findViewById(R.id.imageView1);
		imageView1.setImageResource(PictureFinder.findPictureByName(cartoonContent.getPicture()));
		TextView textView1 = (TextView)convertView.findViewById(R.id.textView1);
		textView1.setText(cartoonContent.getName());
		TextView textView2 = (TextView)convertView.findViewById(R.id.textView2);
		textView2.setText(cartoonContent.getCreator());
		TextView textView3 = (TextView)convertView.findViewById(R.id.textView3);
		textView3.setText(cartoonContent.getDebut());		
		
		return convertView;
	}
}
