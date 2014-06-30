package com.re.reverb.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.re.reverb.R;

public class MainFeedFragment extends Fragment
{

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		View theView = inflater.inflate(R.layout.fragment_main_feed, container, false);
		
		EditText editText = (EditText) theView.findViewById(R.id.editTextMainFeed);
		editText.addTextChangedListener(new TextWatcher(){

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after)
			{
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count)
			{
				System.out.println(s.toString());
			}

			@Override
			public void afterTextChanged(Editable s)
			{
				// TODO Auto-generated method stub
				
			}
			
		});
		
		return theView;
	}

}
