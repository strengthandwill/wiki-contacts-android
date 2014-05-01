package com.kahkong.wikicontacts.form;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.subcontroller.FlowLayout;

import android.content.Context;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * 
 * @author Poh Kah Kong
 *
 */
public class TagsForm extends LinearLayout {
	private Context context;	
	private List<String> tags = new ArrayList<String>();
	
	private FlowLayout tagsFL;
	
	public TagsForm(Context context, AttributeSet attrs) {
		super(context, attrs);
		if (isInEditMode()) {
			return;
		}
		
		this.context = context;
		init();
	}

	private void init() {
		final LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.contact_edit_etbtn, null);
		addView(view);
		tagsFL = new FlowLayout(context);
		addView(tagsFL);

		final EditText tagsET = (EditText) view.findViewById(R.id.editEditTextEtbtn);
		final Button tagsBtn = (Button) view.findViewById(R.id.editButtonEtbtn);
		tagsBtn.setText("+");
		tagsBtn.setVisibility(View.INVISIBLE);
		updateTagsView();

		tagsET.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
				String tagsETVal = editable.toString();
				boolean isAddable;
				if (tagsETVal.equals("")) {
					isAddable = false;
				} else {
					isAddable = true;
					String[] tagsTokens = tokenizeString(tagsETVal);
					for (int i = 0; i < tagsTokens.length; i++) {
						if (tags.contains(tagsTokens[i])) {
							isAddable = false;
							break;
						}
					}
				}
				if (isAddable) {
					tagsBtn.setVisibility(View.VISIBLE);
				} else {
					tagsBtn.setVisibility(View.INVISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
			}

			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
			}
		});

		tagsBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				String[] tagsTokens = tokenizeString(tagsET.getText()
						.toString());
				for (int i = 0; i < tagsTokens.length; i++) {
					tags.add(tagsTokens[i]);
				}
				tagsET.setText("");
				updateTagsView();				
			}
		});	
	}
	
	private void updateTagsView() {
		tagsFL.removeAllViews();
		if (tags.isEmpty()) {
			tagsFL.setVisibility(View.GONE);
		}
		Iterator<String> iterator = tags.iterator();
		while (iterator.hasNext()) {
			final String tagsToken = iterator.next();
			final Button tagsTokenTV = new Button(context, null, android.R.attr.buttonStyleSmall);
			tagsTokenTV.setText(Html.fromHtml(tagsToken + " <sup>x</sup>"));
			tagsFL.addView(tagsTokenTV);
			tagsTokenTV.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					tags.remove(tagsToken);
					tagsFL.removeView(tagsTokenTV);
				}
			});
		}
		tagsFL.setVisibility(View.VISIBLE);
	}
	
	public String[] getValues() {
		if (tags.isEmpty()) {
			return null;
		} else {
			return tags.toArray(new String[tags.size()]);
		}
	}
	
	public void setValues(String[] values) {
		if (values==null) {
			return;
		}
		tags.clear();
		for (int i=0; i<values.length; i++) {
			tags.add(values[i]);			
		}
		updateTagsView();
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {				
		int count = getChildCount();
		for (int i = 0; i < count; i++) {
			getChildAt(i).measure(widthMeasureSpec, heightMeasureSpec);
		}		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private String[] tokenizeString(String str) {
		return str.split("\\s+");
	}
}
