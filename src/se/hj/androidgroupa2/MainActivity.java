package se.hj.androidgroupa2;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.security.acl.LastOwnerException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.message.BasicHeader;
import org.json.JSONArray;
import org.json.JSONObject;

import se.hj.androidgroupa2.objects.ApiHelper;
import se.hj.androidgroupa2.objects.ExtendedTitle;
import se.hj.androidgroupa2.objects.LoginUser;
import se.hj.androidgroupa2.objects.OnFragmentCompleteListener;
import se.hj.androidgroupa2.objects.StoredDataName;
import se.hj.androidgroupa2.objects.User;
import se.hj.androidgroupa2.objects.UserCategory;
import se.hj.androidgroupa2.objects.UserCategory.CATEGORY;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnFragmentCompleteListener {

	public static User LoggedInUser = null;
	
	public enum NAV_ITEM_STAFF {
	    BORROWINGS(0), NOTIFICATIONS(1),
	    BARCODE_SCANNER(2), ADD_TITLE(3),
	    SETTINGS(4);

	    private int numVal;

	    NAV_ITEM_STAFF(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	}
	public enum NAV_ITEM_STUDENT {
	    BORROWINGS(0), NOTIFICATIONS(1),
	    BARCODE_SCANNER(2),
	    SETTINGS(3);

	    private int numVal;

	    NAV_ITEM_STUDENT(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	}
	public enum NAV_ITEM_UNKNOWN {
	    BARCODE_SCANNER(0),
	    SETTINGS(1);

	    private int numVal;

	    NAV_ITEM_UNKNOWN(int numVal) {
	        this.numVal = numVal;
	    }
	    
	    public int getNumVal() {
	        return numVal;
	    }
	}
	
	//Shake
	private SensorManager _SensorManager;
	private float _Accel;
	private float _AccelCurrent; 
	private float _AccelLast; 
	
	private DrawerLayout _drawerLayout;
	private ListView _nav_list;
	private ActionBarDrawerToggle _actionBarDrawerToggle;
	
	private ArrayList<NavAdapterItem> _nav_items;
	private CharSequence _generalTitle;
	private CharSequence _drawerTitle;
	
	private TextView _nav_users_name;
	private TextView _nav_users_email;
	private LinearLayout _nav_user;
	
	private MenuItem _actionBar_searchItem;
	
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
        
        //Shake
        _SensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
	    _SensorManager.registerListener(_SensorListener, _SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
	    _Accel = 0.00f;
	    _AccelCurrent = SensorManager.GRAVITY_EARTH;
	    _AccelLast = SensorManager.GRAVITY_EARTH;
        
        boolean userLoggedIn = checkForLoggedInUser();

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
        		setActionBarArrow();
        		getActionBar().setTitle(_generalTitle);
        		invalidateOptionsMenu();
        	}
        	
        	public void onDrawerOpened(View view)
        	{
        		_actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        		getActionBar().setTitle(_drawerTitle);
        		invalidateOptionsMenu();
        	}
        };
        _drawerLayout.setDrawerListener(_actionBarDrawerToggle);
        
        getFragmentManager().addOnBackStackChangedListener(new FragmentManagerBackStackListener());
        setActionBarArrow();
        
        // TODO: Set fragment to start with and check for saved instance.
        /*if (savedInstanceState == null)
		{
        	selectItem(0);
		}*/

    	if (userLoggedIn)
    	{
    		setLoggedInUser(ApiHelper.LoggedInUser);
    		// TODO: set to borrower fragment
    		setActiveFragment(new BorrowingsFragment(), R.string.title_activity_borrowings, false);
    	}
    	else
    	{
    		setLoggedInUser(null);
    		setActiveFragment(new LoginActivity(), R.string.title_activity_login, false);
    	}
    }
    
    private void setActiveFragment(Fragment fragment, int titleRes, boolean useBackStack)
    {
    	setActiveFragment(fragment, getResources().getString(titleRes), useBackStack);
    }
    
    private void setActiveFragment(Fragment fragment, String title, boolean useBackStack)
    {
    	_drawerLayout.closeDrawers();
    	setTitle(title);
    	FragmentManager fragmentManager = getFragmentManager();
    	
    	Fragment check = fragmentManager.findFragmentByTag(fragment.getClass().toString());
    	if (check != null && check.isVisible())
    	{
    		fragmentManager.popBackStackImmediate();
    	}
    	
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.content_frame, fragment, fragment.getClass().toString());
        if (useBackStack)
        	transaction.addToBackStack(null);
        else
        	fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        transaction.commit();    	
    }
    
    private boolean checkForLoggedInUser()
    {
    	User currentUser = null;
    	BasicHeader authHeader = null;
    	
    	boolean success = false;
    	
    	try {
    		FileInputStream input = openFileInput(StoredDataName.FILE_CURRENT_USER);
    		ObjectInputStream serializer = new ObjectInputStream(input);
    		currentUser = (User) serializer.readObject();
    		input.close();

    		/*FileInputStream input2 = openFileInput(StoredDataName.FILE_AUTH_HEADER);
    		ObjectInputStream serializer2 = new ObjectInputStream(input2);
    		authHeader = (BasicHeader) serializer2.readObject();
    		input2.close();*/
    		
    		SharedPreferences pref = getSharedPreferences(StoredDataName.SHARED_PREF, MODE_PRIVATE);
    		String headerName = pref.getString(StoredDataName.PREF_AUTH_HEADER_NAME, "");
    		String headerValue = pref.getString(StoredDataName.PREF_AUTH_HEADER_VALUE, "");
    		if (headerName.isEmpty() || headerValue.isEmpty()) throw new Exception("Header load failed.");
    		authHeader = new BasicHeader(headerName, headerValue);
		}
		catch (Exception e) {
			Log.e("FILE_USER", e.getMessage());
		}
    	
    	if (currentUser != null && authHeader != null) success = true;
    	
    	if (success)
    	{
    		ApiHelper.LoggedInUser = currentUser;
    		ApiHelper.AuthentificationHeader = authHeader;
    		return true;
    	}
    	else
    	{
    		return false;
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
    		
    		int userCat = UserCategory.CATEGORY.UNKNOWN.getNumVal();
    		if (ApiHelper.LoggedInUser != null) userCat = ApiHelper.LoggedInUser.Category.CategoryId;
    		
    		if (i == NAV_ITEM_STAFF.BORROWINGS.getNumVal()) item.Icon = getResources().getDrawable(R.drawable.ic_action_storage);
    		else if (i == NAV_ITEM_STAFF.NOTIFICATIONS.getNumVal()) item.Icon = getResources().getDrawable(R.drawable.ic_action_email);
    		else if (i == NAV_ITEM_STAFF.BARCODE_SCANNER.getNumVal()) item.Icon = getResources().getDrawable(R.drawable.ic_action_camera);
    		else if (i == NAV_ITEM_STAFF.ADD_TITLE.getNumVal()) item.DividerTop = true;
    		else if (i == NAV_ITEM_STAFF.SETTINGS.getNumVal()) item.DividerTop = true;
    		
    		if (userCat != UserCategory.CATEGORY.STAFF.getNumVal())
    		{
        		if (i == NAV_ITEM_STAFF.ADD_TITLE.getNumVal()) continue;
        		
        		if (userCat != UserCategory.CATEGORY.STUDENT.getNumVal())
        		{
            		if (i == NAV_ITEM_STAFF.BORROWINGS.getNumVal() || i == NAV_ITEM_STAFF.NOTIFICATIONS.getNumVal())
            			continue;
        		}
    		}
    		list.add(item);
    	}
		return list;
    }
    
	public void drawerSelectItem(int position) {
		
		NavAdapterItem item = null;
		int realPos = 0;
		if (position >= 0 && position < _nav_list.getCount())
		{
			_nav_list.setItemChecked(position, true);
			item = _nav_items.get(position);
			
			if (ApiHelper.LoggedInUser == null)
			{
				if (position == NAV_ITEM_UNKNOWN.BARCODE_SCANNER.getNumVal())
					realPos = NAV_ITEM_STAFF.BARCODE_SCANNER.getNumVal();
				else if (position == NAV_ITEM_UNKNOWN.SETTINGS.getNumVal())
					realPos = NAV_ITEM_STAFF.SETTINGS.getNumVal();
			}
			else if (ApiHelper.LoggedInUser.Category.CategoryId == UserCategory.CATEGORY.STUDENT.getNumVal())
			{
				if (position == NAV_ITEM_STUDENT.BORROWINGS.getNumVal())
					realPos = NAV_ITEM_STAFF.BORROWINGS.getNumVal();
				else if (position == NAV_ITEM_STUDENT.NOTIFICATIONS.getNumVal())
					realPos = NAV_ITEM_STAFF.NOTIFICATIONS.getNumVal();
				else if (position == NAV_ITEM_STUDENT.BARCODE_SCANNER.getNumVal())
					realPos = NAV_ITEM_STAFF.BARCODE_SCANNER.getNumVal();
				else if (position == NAV_ITEM_STUDENT.SETTINGS.getNumVal())
					realPos = NAV_ITEM_STAFF.SETTINGS.getNumVal();
			}
			else
				realPos = position;
		}
		else return;
        
		if (realPos == NAV_ITEM_STAFF.BORROWINGS.getNumVal())
		{
			//setActiveFragment(new TitleDetailFragment(), R.string.title_activity_titleDetailsPage, false);
			setActiveFragment(new BorrowingsFragment(), item.Text, false);
		}
		else if (realPos == NAV_ITEM_STAFF.NOTIFICATIONS.getNumVal())
		{
			//TODO: Start notifications activity
		}
		else if (realPos == NAV_ITEM_STAFF.BARCODE_SCANNER.getNumVal())
		{
	        setActiveFragment(new BarcodeScanner(), item.Text, true);
		}
		else if (realPos == NAV_ITEM_STAFF.ADD_TITLE.getNumVal())
		{
			//TODO: Start add title activity
		}
		else if (realPos == NAV_ITEM_STAFF.SETTINGS.getNumVal())
		{
	        // TODO: Start settings activity
		}
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

      NavAdapter adapter = (NavAdapter) _nav_list.getAdapter();
      adapter.clear();
      adapter.addAll(createNavItems(getResources().getStringArray(R.array.nav_list_items)));
      adapter.notifyDataSetChanged();
	}
	
	public void onNavLoginClick(View textView)
	{
		setActiveFragment(new LoginActivity(), R.string.title_activity_login, true);
	}
	
	private void setActionBarArrow()
	{
		int backStackCount = getFragmentManager().getBackStackEntryCount();
		_actionBarDrawerToggle.setDrawerIndicatorEnabled(backStackCount == 0);
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
    	_actionBar_searchItem = menu.findItem(R.id.action_search);
    	SearchView searchView = (SearchView) _actionBar_searchItem.getActionView();
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
    	if (_actionBarDrawerToggle.isDrawerIndicatorEnabled() &&
    			_actionBarDrawerToggle.onOptionsItemSelected(item)) 
    		return true;
    	
        int id = item.getItemId();
        if (id == R.id.action_settings)
            return true;
        else if (id == android.R.id.home && getFragmentManager().popBackStackImmediate())
        	return true;
        	
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
    protected void onDestroy() {
    	super.onDestroy();
    }

	@Override
	public void onFragmentComplete(Fragment sender, Object params) {
		
		if (sender.getClass() == LoginActivity.class)
		{
			LoginActivity fragment = (LoginActivity) sender;
			if (params != null)
			{
				User param = ApiHelper.LoggedInUser;
				Toast.makeText(this, "Welcome " + param.FirstName + " " + param.LastName, Toast.LENGTH_SHORT).show();
				setLoggedInUser(param);
				//TODO: set fragment to borrowings
				setActiveFragment(new BorrowingsFragment(), R.string.title_activity_borrowings, false);
			}
			else
			{
				setLoggedInUser(ApiHelper.LoggedInUser);
				setActiveFragment(new BorrowingsFragment(), R.string.title_activity_borrowings, false);
			}
		}
		else if (sender.getClass() == TitlePageFragment.class)
		{
			if (params != null)
			{
//				TitleDetailFragment fragment = (TitleDetailFragment) params;
//				getFragmentManager()
//				    .beginTransaction()
//				    .replace(R.id.content_frame, fragment, "TAG_TO_FRAGMENT")
//				    .addToBackStack("TAG_TO_FRAGMENT").commit();
//				setActiveFragment(fragment, R.string.title_activity_title_page, true);
			}
		}
		else if (sender.getClass() == SearchActivity.class)
		{
			if (params != null)
			{
				ExtendedTitle title = (ExtendedTitle) params;
				
		        Fragment fragment = new TitlePageFragment();
		        Bundle args = new Bundle();
		        args.putString("TitleId", title.TitleInformation.TitleId.toString());
		        fragment.setArguments(args);
		        setActiveFragment(fragment, R.string.title_activity_title_page, true);
		        
				/*
				Fragment fragment = new TitleDetailFragment();
				Bundle args = new Bundle();
				args.putSerializable(StoredDataName.ARGS_EXTENDED_TITLE, title);
				fragment.setArguments(args);
				setActiveFragment(fragment, "Title details", true);*/
			}
		}
		else if (sender.getClass() == BorrowingsFragment.class)
		{
			if (params != null)
			{
				Integer titleId = (Integer) params;
				
//		        Fragment fragment = new TitlePageFragment();
//		        Bundle args = new Bundle();
//		        args.putString(StoredDataName.ARGS_TITLEID, titleId.toString());
//		        fragment.setArguments(args);
//		        setActiveFragment(fragment, R.string.title_activity_title_page, true);
				Toast.makeText(this, "TEST: " + titleId.toString(), Toast.LENGTH_SHORT).show();
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

			if (query.isEmpty()) return false;

			if (_actionBar_searchItem != null)
			{
				_actionBar_searchItem.collapseActionView();
				SearchView searchView = (SearchView) _actionBar_searchItem.getActionView();
				searchView.clearFocus();
			}
			
			Bundle args = new Bundle();
			args.putString(StoredDataName.ARGS_SEARCH_QUERY, query);
			SearchActivity fragment = new SearchActivity();
			fragment.setArguments(args);
			setActiveFragment(fragment, R.string.title_activity_search, true);
			
			return true;
		}
    }
    
    private class FragmentManagerBackStackListener implements FragmentManager.OnBackStackChangedListener
    {
		@Override
		public void onBackStackChanged() {
			setActionBarArrow();
		}
    }
    
  //Shake 
    private final SensorEventListener _SensorListener = new SensorEventListener() {
    	
		public void onSensorChanged(SensorEvent se) {
			float x = se.values[0];
			float y = se.values[1];
			float z = se.values[2];
			_AccelLast = _AccelCurrent;
			_AccelCurrent = (float) Math.sqrt((double) (x * x + y * y + z * z));
			float delta = _AccelCurrent - _AccelLast;
			_Accel = _Accel * 0.9f + delta;
			
			SharedPreferences prefs = getSharedPreferences(StoredDataName.SHARED_PREF, MODE_PRIVATE); 
			Boolean Shake = prefs.getBoolean(StoredDataName.PREF_SETTINGS_SHAKE, true);

			if (_Accel > 12 && Shake == true) {

				Bundle args = new Bundle();
				args.putString(StoredDataName.ARGS_RANDOM_TITLE, "");
				SearchActivity fragment = new SearchActivity();
				fragment.setArguments(args);
				setActiveFragment(fragment, R.string.title_activity_search,
						true);
			}
		}
    	
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
		}
    };

		

	@Override
	public void onResume() {
		super.onResume();
		_SensorManager.registerListener(_SensorListener,
				_SensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	public void onPause() {
		_SensorManager.unregisterListener(_SensorListener);
		super.onPause();
	}
}
