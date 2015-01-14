package com.re.reverb.ui;

import android.support.v4.app.Fragment;

import com.re.reverb.R;

public class RegionsFragment extends Fragment
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
		View view = inflater.inflate(R.layout.fragment_regions, container, false);
		
		EditText editText = (EditText) view.findViewById(R.id.editTextRegions);
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
		
		return view;
	}

}
