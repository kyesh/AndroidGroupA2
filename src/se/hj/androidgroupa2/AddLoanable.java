package se.hj.androidgroupa2;

import se.hj.androidgroupa2.objects.DoelibsNotification;
import se.hj.androidgroupa2.objects.Loanable;
import se.hj.androidgroupa2.objects.SimpleLoanable;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.Title;
import android.app.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.webkit.WebView.FindListener;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class AddLoanable extends Fragment{

	EditText _identity;
	EditText _room;
	EditText _locationCategory;
	
	private String _titleId;
	private Context _context;
	
	Button _SaveBtn;
	
	private OnClickListener _onClickListener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View mainView = inflater.inflate(R.layout.fragment_addloanable, container, false);
		getActivity().setTitle(R.string.title_activity_addLoanable);
		
		_identity = (EditText) mainView.findViewById(R.id.identity);
		_room = (EditText) mainView.findViewById(R.id.room);
		_locationCategory = (EditText) mainView.findViewById(R.id.locationCategory);
		_SaveBtn = (Button) mainView.findViewById(R.id.SaveBtn);
		
		Bundle args = getArguments();
		_titleId = args.getString(StoredDataName.ARGS_TITLEID);
		_context = getActivity();
				
		_onClickListener = new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				SimpleLoanable simpleloanable = new SimpleLoanable();
				simpleloanable.Barcode = _identity.getText().toString();
				simpleloanable.Category = _locationCategory.getText().toString();
				simpleloanable.Location = _room.getText().toString();
				simpleloanable.TitleId = _titleId;
				if (!simpleloanable.validateFields())
				{
					Toast.makeText(_context, "Validate your input", Toast.LENGTH_SHORT).show();
					return;
				}
				else
				{
					SimpleLoanable.addLoanableToDatabase(simpleloanable, new DoelibsNotification.CallbackReference() {
						@Override
						public void callbackFunction(Object result) {
							
							Boolean success = (Boolean) result;
							if (success == null) success = false;
							
							if (success)
							{
								Toast.makeText(_context, "Loanable created", Toast.LENGTH_SHORT).show();
							}
							else
							{
								Toast.makeText(_context, R.string.info_technicalIssues, Toast.LENGTH_SHORT).show();
								return;
							}
						}
					});
				}
			}
		
		};
		
		_SaveBtn.setOnClickListener(_onClickListener);
		
		
		return mainView;
	}
}
	