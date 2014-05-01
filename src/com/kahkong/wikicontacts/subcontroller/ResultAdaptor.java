package com.kahkong.wikicontacts.subcontroller;

import java.util.List;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.modal.ContactAndImage;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class ResultAdaptor extends BaseAdapter {
	private Context context;
	private List<ContactAndImage> contactAndImages;
	
	private static class ViewHolder {
		public TextView nameTV;
		public TextView descriptionTV;
		public TextView tagsTV;
		public ImageView imageIV;
	}

	public ResultAdaptor(Context context, List<ContactAndImage> contactAndImages) {
		this.context = context;
		this.contactAndImages = contactAndImages;
	}

	@Override
	public int getCount() {
		if (contactAndImages==null) {
			return 0;
		}
		return contactAndImages.size();
	}

	@Override
	public Object getItem(int position) {
		if (contactAndImages==null) {
			return null;
		}
		return contactAndImages.get(position);		
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (contactAndImages==null) {
			return null;
		}

		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = layoutInflater.inflate(R.layout.result, null);

			viewHolder = new ViewHolder();
			viewHolder.nameTV = (TextView) convertView.findViewById(R.id.resultNameTV);
			viewHolder.descriptionTV = (TextView) convertView.findViewById(R.id.resultDescriptionTV);
			viewHolder.tagsTV = (TextView) convertView.findViewById(R.id.resultTagsTV);
			viewHolder.imageIV = (ImageView) convertView.findViewById(R.id.resultImageIV);
			convertView.setTag(viewHolder);
		}	else {
			viewHolder = (ViewHolder) convertView.getTag();		
		}



		final ContactAndImage contactAndImage = contactAndImages.get(position);		

		viewHolder.nameTV.setText(contactAndImage.getName());		
		if (contactAndImage.getDescription()!=null) {
			viewHolder.descriptionTV.setVisibility(View.VISIBLE);
			viewHolder.descriptionTV.setText(contactAndImage.getDescription());
		} else {
			viewHolder.descriptionTV.setVisibility(View.GONE);
		}		
		if (contactAndImage.getTags()!=null) {
			viewHolder.tagsTV.setVisibility(View.VISIBLE);
			viewHolder.tagsTV.setText("Tags: " + ViewFormatter.arrayToString(contactAndImage.getTags()));	
		} else {
			viewHolder.tagsTV.setVisibility(View.GONE);
		}

		if (contactAndImage.getImage()!=null) {
			viewHolder.imageIV.setImageBitmap(contactAndImage.getImage());
		} else {
			viewHolder.imageIV.setImageResource(R.drawable.ic_launcher);
		}
		return convertView;
	}

	public void setContacts(List<ContactAndImage> contactAndImages) {
		this.contactAndImages = contactAndImages;
	}

	public void clear() {
		if (contactAndImages==null) {
			return;
		}
		contactAndImages.clear();
		notifyDataSetChanged();
	}
}
