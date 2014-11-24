package se.hj.androidgroupa2;

import android.graphics.drawable.Drawable;

public class NavAdapterItem {
	
	public boolean DividerTop = false;
	public boolean DividerBottom = false;
	public Drawable Icon = null;
	public String HeaderText = "";
	public String Text = "";

	public NavAdapterItem()
	{}
	
	public NavAdapterItem(boolean dividerTop, boolean dividerBottom, Drawable icon, String headerText, String text)
	{
		DividerTop = dividerTop;
		DividerBottom = dividerBottom;
		Icon = icon;
		HeaderText = headerText;
		Text = text;
	}
	
	@Override
	public String toString() {
		return Text;
	}
}
