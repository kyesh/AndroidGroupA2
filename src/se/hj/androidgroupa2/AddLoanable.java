package se.hj.androidgroupa2;

import se.hj.androidgroupa2.objects.Loanable;
import se.hj.androidgroupa2.objects.SimpleLoanable;
import se.hj.androidgroupa2.objects.Title;
import android.app.Fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;

public class AddLoanable extends Fragment{

	EditText _identity;
	EditText _room;
	EditText _locationCategory;
	
	Button _SaveBtn;
	
	private OnClickListener _onClickListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_addloanable, container, false);
		_identity = (EditText) mainView.findViewById(R.id.identity);
		_room = (EditText) mainView.findViewById(R.id.room);
		_locationCategory = (EditText) mainView.findViewById(R.id.locationCategory);
		_SaveBtn = (Button) mainView.findViewById(R.id.SaveBtn);
				
		_onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Bundle args = new Bundle();
				SimpleLoanable simpleloanable = new SimpleLoanable();
				simpleloanable.Barcode = _identity.getText().toString();
				simpleloanable.Category = _room.getText().toString();
				simpleloanable.Location = _locationCategory.getText().toString();
				
			}
		
		};
		
		_SaveBtn.setOnClickListener(_onClickListener);
		
		
		return mainView;
	}
}
	