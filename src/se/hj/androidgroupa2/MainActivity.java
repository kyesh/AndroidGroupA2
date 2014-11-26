package se.hj.androidgroupa2;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import se.hj.androidgroupa2.objects.LoginUser;
import se.hj.androidgroupa2.objects.User;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;


public class MainActivity extends Activity {

	private DrawerLayout _drawerLayout;
	private ListView _nav_list;
	private ActionBarDrawerToggle _actionBarDrawerToggle;
	
	private ArrayList<NavAdapterItem> _nav_items;
	private CharSequence _generalTitle;
	private CharSequence _drawerTitle;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        _drawerTitle = _generalTitle = getTitle();
        _drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        _nav_list = (ListView) findViewById(R.id.nav_list);
        
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
        
        Fragment fragment = new TestFragment();
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
        				.replace(R.id.content_frame, fragment)
        				.commit();
        setTitle("Test Fragment");
    }
    
    private ArrayList<NavAdapterItem> createNavItems(String[] items)
    {
    	// TODO: Maybe make this nicer.
    	ArrayList<NavAdapterItem> list = new ArrayList<NavAdapterItem>();
    	
    	for (int i = 0; i < items.length; i++)
    	{
    		NavAdapterItem item = new NavAdapterItem();
    		item.Text = items[i];
    		
    		if (i == 0) item.Icon = getResources().getDrawable(R.drawable.ic_action_storage);
    		else if (i == 1) item.Icon = getResources().getDrawable(R.drawable.ic_action_email);
    		else if (i == 2) item.Icon = getResources().getDrawable(R.drawable.ic_action_camera);
    		else if (i == 3) item.DividerTop = true;
    		else if (i == 4) item.DividerTop = true;
    		
    		list.add(item);
    	}
		return list;
    }
    
	public void drawerSelectItem(int position) {
		
		// TODO: Select correct fragment and load it in!
		_nav_list.setItemChecked(position, true);
		setTitle(_nav_items.get(position).Text);
		
		NavAdapterItem item = _nav_items.get(position);
        FragmentManager fragmentManager = getFragmentManager();
		if (position == 0)
		{
			Fragment fragment = new TestFragment();
	        fragmentManager.beginTransaction()
	        				.replace(R.id.content_frame, fragment)
	        				.commit();
	        setTitle(item.Text);
		}
		else if (position == 1)
		{
			LoginUser.LogIn("rob.day@hj.see", "secret", new LoginUser.CallbackReference() {
				
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
		else if (position == 2) // Barcode scanner
		{
	        Fragment fragment = new BarcodeScanner();
	        fragmentManager.beginTransaction()
	        				.replace(R.id.content_frame, fragment)
	        				.commit();
	        setTitle(item.Text);
		}
		
		_drawerLayout.closeDrawers();
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
