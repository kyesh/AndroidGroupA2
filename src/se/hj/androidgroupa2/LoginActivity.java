package se.hj.androidgroupa2;

import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import se.hj.androidgroupa2.objects.ApiHelper;
import se.hj.androidgroupa2.objects.LoginUser;
import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.User;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TextView.OnEditorActionListener;

public class LoginActivity extends Fragment {
	
	EditText _emailBox;
	EditText _passwordBox;
	TextView _error;
	
	Button _notNowBtn;
	Button _loginBtn;
	
	OnFragmentCompleteListener _fragmentCallback;
	

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		try {
			_fragmentCallback = (OnFragmentCompleteListener) activity;
		} catch (Exception e) {
			Log.e("CLASS", "Activity not implementing correct interface.");
		}
	}
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.activity_login, container, false);
    	getActivity().setTitle(R.string.title_activity_login);
    	
    	_emailBox = (EditText) rootView.findViewById(R.id.loginEmail);
        _passwordBox = (EditText) rootView.findViewById(R.id.loginPassword);
        _error = (TextView) rootView.findViewById(R.id.login_error);
        
        _passwordBox.setOnEditorActionListener(new TextView.OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_DONE)
				{
					onLogIn(v);
				}
				return false;
			}
        });
        
        _notNowBtn = (Button) rootView.findViewById(R.id.notNowBtn);
        _notNowBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onNotNow(v);
			}
		});

        _loginBtn = (Button) rootView.findViewById(R.id.logInBtn);
        _loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				onLogIn(v);
			}
		});
    	
    	return rootView;
    }
    
    public void onLogIn(View v){
        
        String email = _emailBox.getText().toString();
        String password = _passwordBox.getText().toString();
        
        if (email.isEmpty() || password.isEmpty())
        {
        	_error.setText("You must specify your credentials");
        	return;
        }
        

		ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		
		if (info != null && info.isConnected())
		{
	        LoginUser.logIn(email, password, new LoginUser.CallbackReference() {
	        //LoginUser.logIn("rob.day@hj.see", "secret", new LoginUser.CallbackReference() {
				@Override
				public void callbackFunction(User user) {
					if (ApiHelper.LoggedInUser == null || ApiHelper.AuthentificationHeader == null)
			        	_error.setText("Wrong email or password");
					else
					{
						try {
							FileOutputStream output = getActivity().openFileOutput(StoredDataName.FILE_CURRENT_USER, MainActivity.MODE_PRIVATE);
							ObjectOutputStream serializer = new ObjectOutputStream(output);
							serializer.writeObject(ApiHelper.LoggedInUser);
							output.close();
							
							/*output = getActivity().openFileOutput(StoredDataName.FILE_AUTH_HEADER, MainActivity.MODE_PRIVATE);
							serializer = new ObjectOutputStream(output);
							serializer.writeObject(ApiHelper.AuthentificationHeader);
							output.close();*/
							SharedPreferences.Editor pref = getActivity().getSharedPreferences(StoredDataName.SHARED_PREF, MainActivity.MODE_PRIVATE).edit();
							pref.putString(StoredDataName.PREF_AUTH_HEADER_NAME, ApiHelper.AuthentificationHeader.getName());
							pref.putString(StoredDataName.PREF_AUTH_HEADER_VALUE, ApiHelper.AuthentificationHeader.getValue());
							pref.putString(StoredDataName.PREF_AUTH_COOKIE_VALUE, ApiHelper.AuthentificationCookieValue);
							pref.commit();
						}
						catch (Exception e) {
							Log.e("FILE_LOGIN", e.getMessage() + " | " + ApiHelper.AuthentificationHeader.toString());
						}
						_fragmentCallback.onFragmentComplete(LoginActivity.this, user);
					}
				}
			});
		}
		else
		{
			Toast.makeText(getActivity(), "You are not connected to the internet", Toast.LENGTH_LONG).show();
		}
    }
    
    public void onNotNow(View v){
    	//redirect to main page
    	//Intent intent = new Intent(this, DisplayMessageActivity.class);
        //startActivity(intent);
    	_fragmentCallback.onFragmentComplete(this, null);
    }
    

}
