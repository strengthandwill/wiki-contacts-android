package com.kahkong.wikicontacts.form;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.kahkong.wikicontacts.R;
import com.kahkong.wikicontacts.callback.Validator;

import android.content.Context;
import android.text.Editable;
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
public class MultiEditTextForm extends LinearLayout {
	private static final String NAMESPACE = "http://schemas.android.com/apk/res/android";
	
	private Context context;
	private Button addBtn;
	private List<EditText> editTexts = new ArrayList<EditText>();
	private int inputType;
	private Validator validator;
	private String[] errorMsgs;
	private boolean isSubmitted = false;

	public MultiEditTextForm(Context context, AttributeSet attrs) {
		super(context, attrs);		
		if (isInEditMode()) {
			return;			
		}
		
		this.context = context;
		inputType = attrs.getAttributeIntValue(NAMESPACE, "inputType", 1);
		init();
	}
	
	private void init() {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = layoutInflater.inflate(R.layout.contact_edit_etbtn, null);
		addView(view);
		final EditText editText = (EditText) view.findViewById(R.id.editEditTextEtbtn);
		addBtn = (Button) view.findViewById(R.id.editButtonEtbtn);
		editText.setInputType(inputType);
		addBtn.setText("+");
		addBtn.setVisibility(View.INVISIBLE);
		editTexts.add(editText);		
	}
	
	public String[] getValues() {
		List<String> values = new ArrayList<String>();
		Iterator<EditText> iterator = editTexts.iterator();
		while (iterator.hasNext()) {
			String value = iterator.next().getText().toString();
			if (!value.equals("")) {
				values.add(value);
			}
		}
		if (values.isEmpty()) {
			return null;
		} else {
			return values.toArray(new String[values.size()]);
		}
	}
	
	public void setValues(String[] values) {
		if (values==null) {
			return;
		}
		editTexts.get(0).setText(values[0]);
		for (int i=1; i<values.length; i++) {
			addEditTextBtn();
			editTexts.get(1).setText(values[1]);
		}
	}
	
	public void setValidator(final Validator validator, final String[] errorMsgs) {
		this.validator = validator;
		this.errorMsgs = errorMsgs;
		
		editTexts.get(0).addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
				toggleAddBtn();
				if (!isSubmitted) {
					return;
				}
				isValidated(editTexts.get(0));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});
		
		addBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				addEditTextBtn();
			}
		});	
	}
	
	private void addEditTextBtn() {
		LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		final View view2 = layoutInflater.inflate(R.layout.contact_edit_etbtn, null);
		addView(view2);
		final EditText editText2 = (EditText) view2.findViewById(R.id.editEditTextEtbtn);
		Button button2 = (Button) view2.findViewById(R.id.editButtonEtbtn);
		editText2.setInputType(inputType);				
		editText2.requestFocus();
		button2.setText("-");
		addBtn.setVisibility(View.INVISIBLE);
		editTexts.add(editText2);
		
		editText2.addTextChangedListener(new TextWatcher() {
			@Override
			public void afterTextChanged(Editable editable) {
				toggleAddBtn();
				if (!isSubmitted) {
					return;
				}
				isValidated(editText2);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		});

		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				editTexts.remove(editText2);
				removeView(view2);
			}
		});
	}
	
	public boolean submit() {
		isSubmitted = true;
		boolean isValidated = true;
		Iterator<EditText> iterator = editTexts.iterator();
		while (iterator.hasNext()) {
			EditText editText = iterator.next();
			if (!isValidated(editText)) {
				isValidated = false;
			}
		}
		return validator==null? true:isValidated;
	}
	
	private boolean isValidated(EditText editText) {
		int status = validator.validate(editText.getText().toString());
		if (status>0) {
			editText.setError(errorMsgs[status-1]);
			return false;
		} else {
			return true;
		}
	}
	
	private void toggleAddBtn() {
		boolean isAddable = true;
		Iterator<EditText> iterator = editTexts.iterator();
		Set<String> set = new HashSet<String>();
		while (iterator.hasNext()) {
			String value = iterator.next().getText().toString();
			if (value.equals("") || set.contains(value)) {
				isAddable = false;
			} else {
				set.add(value);
			}
		}
		if (isAddable) {
			addBtn.setVisibility(View.VISIBLE);
		} else {
			addBtn.setVisibility(View.INVISIBLE);
		}		
	}	
	
	public String getPrimaryValue() {
		return editTexts.get(0).getText().toString();
	}
	
	public void setPrimaryErrorMsg(int status) {
		if (status>0) {
			editTexts.get(0).setError(errorMsgs[status-1]);
		} else {
			editTexts.get(0).setError(null);
		}
	}
}
