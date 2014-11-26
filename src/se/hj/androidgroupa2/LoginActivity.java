package se.hj.androidgroupa2;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends Fragment {
	
	EditText _emailBox;
	EditText _passwordBox;
	TextView _error;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
    		Bundle savedInstanceState) {
    	View rootView = inflater.inflate(R.layout.activity_login, container, false);
    	getActivity().setTitle(R.string.title_activity_login);
    	
    	_emailBox = (EditText) rootView.findViewById(R.id.loginEmail);
        _passwordBox = (EditText) rootView.findViewById(R.id.loginPassword);
        _error = (TextView) rootView.findViewById(R.id.login_error);
    	
    	return rootView;
    }
    
    public void onLogIn(View v){
        
        String email = _emailBox.getText().toString();
        String password = _passwordBox.getText().toString();
        
        if(email.equals("emil@gmail.com") && password.equals("secret")){
        	_error.setText("");
        	Context context = getActivity().getApplicationContext();
        	CharSequence text = "Loggin in...";
        	int duration = Toast.LENGTH_SHORT;

        	Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
        	
        }
        /*if(userfunctions.loginUser(email, password)){
        	error.setText("");
        	Context context = getApplicationContext();
        	CharSequence text = "Loggin in...";
        	int duration = Toast.LENGTH_SHORT;

        	Toast toast = Toast.makeText(context, text, duration);
        	toast.show();
        	
        	//Intent intent = new Intent(this, DisplayMessageActivity.class);
            //startActivity(intent);
        }*/
        else{
        	_error.setText("Wrong email or password");
        }
    }
    
    public void onNotNow(View v){
    	//redirect to main page
    	//Intent intent = new Intent(this, DisplayMessageActivity.class);
        //startActivity(intent);
    }
}
