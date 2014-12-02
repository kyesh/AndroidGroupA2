package se.hj.androidgroupa2;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import se.hj.androidgroupa2.objects.ApiHelper;
import se.hj.androidgroupa2.objects.LoginUser;
import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.User;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnFragmentCompleteListener {

	public static User LoggedInUser = null;
	
	public enum NAV_ITEM {
	    BORROWINGS(0), NOTIFICATIONS(1),
	    BARCODE_SCANNER(2), ADD_TITLE(3),
	    SETTINGS(4), LOGIN(10);

	    private int numVal;

	    NAV_ITEM(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	}
	
	private DrawerLayout _drawerLayout;
	private ListView _nav_list;
	private ActionBarDrawerToggle _actionBarDrawerToggle;
	
	private ArrayList<NavAdapterItem> _nav_items;
	private CharSequence _generalTitle;
	private CharSequence _drawerTitle;
	
	private TextView _nav_users_name;
	private TextView _nav_users_email;
	private LinearLayout _nav_user;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        _drawerTitle = _generalTitle = getTitle();
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _nav_list = (ListView) findViewById(R.id.nav_list);
        
        _nav_users_name = (TextView) findViewById(R.id.nav_users_name);
        _nav_users_email = (TextView) findViewById(R.id.nav_users_email);
        _nav_user = (LinearLayout) findViewById(R.id.nav_user);
        
        _nav_items = createNavItems(getResources().getStringArray(R.array.nav_list_items)); 
        _nav_list.setAdapter(new NavAdapter(
        		this,
        		R.layout.drawer_list_item,
        		android.R.id.text1,
        		_nav_items));
        _nav_list.setOnItemClickListener(new DrawerItemClickListener());
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
        	
        _actionBarDrawerToggle = new ActionBarDrawerToggle(
        		this,
        		_drawerLayout,
        		R.drawable.ic_drawer,
        		R.string.drawer_open,
        		R.string.drawer_close)
        {
        	public void onDrawerClosed(View view)
        	{
        		getActionBar().setTitle(_generalTitle);
        		invalidateOptionsMenu();
        	}
        	
        	public void onDrawerOpened(View view)
        	{
        		getActionBar().setTitle(_drawerTitle);
        		invalidateOptionsMenu();
        	}
        };
        _drawerLayout.setDrawerListener(_actionBarDrawerToggle);
        
        // TODO: Set fragment to start with and check for saved instance.
        /*if (savedInstanceState == null)
		{
        	selectItem(0);
		}*/

        checkForLoggedInUser();
    }
    
    private void setActiveFragment(Fragment fragment)
    {
    	_drawerLayout.closeDrawers();
    	FragmentManager fragmentManager = getFragmentManager();
    	
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.commit();
    }
    
    private void checkForLoggedInUser()
    {
    	User currentUser = null;
    	Header authHeader = null;
    	
    	boolean success = false;
    	
    	try {
    		FileInputStream input = openFileInput(StoredDataName.FILE_CURRENT_USER);
    		ObjectInputStream serializer = new ObjectInputStream(input);
    		currentUser = (User) serializer.readObject();
    		input.close();

    		input = openFileInput(StoredDataName.FILE_AUTH_HEADER);
    		serializer = new ObjectInputStream(input);
    		authHeader = (Header) serializer.readObject();
    		input.close();
		}
		catch (Exception e) {
			Log.e("FILE_USER", e.getMessage());
		}
    	
    	if (currentUser != null && authHeader != null) success = true;
    	
    	if (success)
    	{
    		ApiHelper.LoggedInUser = currentUser;
    		ApiHelper.AuthentificationHeader = authHeader;
    		setLoggedInUser(currentUser);
    		// TODO: set fragment to borrowings
    		setActiveFragment(new TestFragment());
    	}
    	else
    	{
    		setLoggedInUser(currentUser);
    		setActiveFragment(new LoginActivity());
    	}
    }
    
    private ArrayList<NavAdapterItem> createNavItems(String[] items)
    {
    	// TODO: Maybe make this nicer.
    	ArrayList<NavAdapterItem> list = new ArrayList<NavAdapterItem>();
    	
    	for (int i = 0; i < items.length; i++)
    	{
    		NavAdapterItem item = new NavAdapterItem();
    		item.Text = items[i];
    		
    		if (i == NAV_ITEM.BORROWINGS.getNumVal()) item.Icon = getResources().getDrawable(R.drawable.ic_action_storage);
    		else if (i == NAV_ITEM.NOTIFICATIONS.getNumVal()) item.Icon = getResources().getDrawable(R.drawable.ic_action_email);
    		else if (i == NAV_ITEM.BARCODE_SCANNER.getNumVal()) item.Icon = getResources().getDrawable(R.drawable.ic_action_camera);
    		else if (i == NAV_ITEM.ADD_TITLE.getNumVal()) item.DividerTop = true;
    		else if (i == NAV_ITEM.SETTINGS.getNumVal()) item.DividerTop = true;
    		
    		list.add(item);
    	}
		return list;
    }
    
	public void drawerSelectItem(int position) {
		
		// TODO: Select correct fragment and load it in!
		NavAdapterItem item = null;
		if (position >= 0 && position < _nav_list.getCount())
		{
			_nav_list.setItemChecked(position, true);
			setTitle(_nav_items.get(position).Text);
			
			item = _nav_items.get(position);
		}
		else return;
		
        FragmentManager fragmentManager = getFragmentManager();
        
		if (position == NAV_ITEM.BORROWINGS.getNumVal())
		{
			Fragment fragment = new TitleDetailFragment();
			fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
			setTitle(item.Text);
			
//			Fragment fragment = new TestFragment();
//	        fragmentManager.beginTransaction()
//	        				.replace(R.id.content_frame, fragment)
//	        				.commit();
//	        setTitle(item.Text);
		}
		else if (position == NAV_ITEM.NOTIFICATIONS.getNumVal() + 12989812)
		{
			LoginUser.logIn("rob.day@hj.see", "secret", new LoginUser.CallbackReference() {
				
				@Override
				public void callbackFunction(User user) {
					
					if (user != null)
						Toast.makeText(MainActivity.this, 
								"Welcome " + user.FirstName + " " + user.LastName, Toast.LENGTH_LONG).show();
					else
						Toast.makeText(MainActivity.this, "Could not log in", Toast.LENGTH_LONG).show();
				}
			});
		}
		else if (position == NAV_ITEM.BARCODE_SCANNER.getNumVal())
		{
	        Fragment fragment = new BarcodeScanner();
	        fragmentManager.beginTransaction()
	        				.replace(R.id.content_frame, fragment)
	        				.commit();
	        setTitle(item.Text);
		}
		
		_drawerLayout.closeDrawers();
	}
	
	public void setLoggedInUser(User user)
	{
		Resources res = getResources();
		if (user != null)
		{
			_nav_user.setBackgroundColor(res.getColor(R.color.nav_user_loggedIn));
			_nav_users_name.setGravity(Gravity.LEFT);
			_nav_users_name.setText(user.FirstName + " " + user.LastName);
			_nav_users_email.setText(user.EMail);
			_nav_users_email.setVisibility(View.VISIBLE);
		}
		else
		{
			_nav_user.setBackgroundColor(res.getColor(R.color.nav_user_notLoggedIn));
			_nav_users_name.setGravity(Gravity.CENTER);
			_nav_users_name.setText(R.string.nav_user_notLoggedIn_name);
			_nav_users_email.setVisibility(View.GONE);
		}
	}
	
	public void onNavLoginClick(View textView)
	{
		setActiveFragment(new LoginActivity());
	}
	
	@Override
	public void onBackPressed() {
		if (_drawerLayout.isDrawerOpen(Gravity.LEFT))
			_drawerLayout.closeDrawer(Gravity.LEFT);
		else
			super.onBackPressed();
	}
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    	getMenuInflater().inflate(R.menu.main, menu);
    	SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
    	searchView.setOnQueryTextListener(new SearchBarOnQueryListener());
    	
    	return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
    	return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
    	if (_actionBarDrawerToggle.onOptionsItemSelected(item)) return true;
    	
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    @Override
    public void setTitle(CharSequence title) {
    	super.setTitle(title);
    	_generalTitle = title;
    	getActionBar().setTitle(title);
    }
    
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
    	super.onPostCreate(savedInstanceState);
    	_actionBarDrawerToggle.syncState();
    }
    
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
    	super.onConfigurationChanged(newConfig);
    	_actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

	@Override
	public void onFragmentComplete(Fragment sender, Object params) {
		
		if (sender.getClass() == LoginActivity.class)
		{
			LoginActivity fragment = (LoginActivity) sender;
			if (params != null)
			{
				User param = (User) params;
				Toast.makeText(this, "Welcome " + param.FirstName + " " + param.LastName, Toast.LENGTH_SHORT).show();
				setLoggedInUser(param);
				//TODO: set fragment to borrowings
				setActiveFragment(new TestFragment());
			}
		}
	}
    
    private class DrawerItemClickListener implements ListView.OnItemClickListener {
    	@Override
    	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    		drawerSelectItem(position);
    	}
    }
    
    private class SearchBarOnQueryListener implements SearchView.OnQueryTextListener {

		@Override
		public boolean onQueryTextChange(String newText) {
			return false;
		}

		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO: Launch searchResult fragment
			setTitle(query);
			return true;
		}
    }
}
