package se.hj.androidgroupa2;

import java.util.List;

import se.hj.androidgroupa2.objects.Loan;
import se.hj.androidgroupa2.objects.UpdateDataInterface;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class BorrowingAdapter extends ArrayAdapter<BorrowingAdapterItem> implements OnClickListener {

	private Context _context;
	private UpdateDataInterface _updateRef;
	private Resources _res;
	
	public BorrowingAdapter(Context context, int resource,
			int textViewResourceId, List<BorrowingAdapterItem> objects, UpdateDataInterface updateRef) {
		super(context, resource, textViewResourceId, objects);
		_context = context;
		_res = context.getResources();
		_updateRef = updateRef;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rootView = super.getView(position, convertView, parent);
		
		LinearLayout container = (LinearLayout) rootView.findViewById(R.id.borrowings_list_item_container);
		ImageView imageView = (ImageView) rootView.findViewById(R.id.borrowings_list_item_image);
		TextView header = (TextView) rootView.findViewById(R.id.borrowings_list_item_header);
		View divider = rootView.findViewById(R.id.borrowings_list_item_divider);
		TextView title = (TextView) rootView.findViewById(R.id.borrowings_list_item_title);
		TextView text1 = (TextView) rootView.findViewById(R.id.borrowings_list_item_text1);
		TextView text2 = (TextView) rootView.findViewById(R.id.borrowings_list_item_text2);
		TextView text3 = (TextView) rootView.findViewById(R.id.borrowings_list_item_text3);
		Button button = (Button) rootView.findViewById(R.id.borrowings_list_item_button);
		
		BorrowingAdapterItem item = getItem(position);
		
		if (item.Header != null && !item.Header.isEmpty())
		{
			header.setVisibility(View.VISIBLE);
			divider.setVisibility(View.VISIBLE);
			header.setText(item.Header);
			container.setVisibility(View.GONE);
			return rootView;
		}
		else
		{
			header.setVisibility(View.GONE);
			divider.setVisibility(View.GONE);
			container.setVisibility(View.VISIBLE);
		}
		
		if (item.BorrowerLoan != null)
		{
			title.setText(item.BorrowerLoan.LoanLoanable.TitleInformation.BookTitle);
			
			text1.setVisibility(View.VISIBLE);
			text2.setVisibility(View.VISIBLE);
			text3.setVisibility(View.VISIBLE);
			text1.setText(_res.getString(R.string.borrowing_location) + " " +
					item.BorrowerLoan.LoanLoanable.Location + 
					" (" + item.BorrowerLoan.LoanLoanable.Category + ")");
			text2.setText(_res.getString(R.string.borrowing_doelibsId) + " " +
					item.BorrowerLoan.LoanLoanable.Barcode);
			
			if (item.BorrowerLoan.RecallExpiredDate == null || item.BorrowerLoan.RecallExpiredDate.isEmpty() || item.BorrowerLoan.RecallExpiredDate == "null")
			{
				text3.setText(_res.getString(R.string.borrowing_recallDate) + " " +
						_res.getString(R.string.borrowing_recallDateNull));
				item.ItemImage = R.drawable.ic_book_256;
				container.setBackgroundColor(Color.WHITE);
			}
			else
			{
				item.ItemImage = R.drawable.ic_book_yellow_256;
				text3.setText(_res.getString(R.string.borrowing_recallDate) + " " +
						item.BorrowerLoan.RecallExpiredDate);
				container.setBackgroundColor(_res.getColor(R.color.borrowing_item_yellow));
			}
			
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.borrowing_button_checkIn);
			button.setTag(item.BorrowerLoan.LoanId);
			button.setOnClickListener(this);
		}
		else if (item.BorrowerReservation != null)
		{
			title.setText(item.BorrowerReservation.ResTitle.BookTitle);
			
			text1.setVisibility(View.VISIBLE);
			if (item.BorrowerReservation.LoanRecalled)
			{
				text1.setText(_res.getString(R.string.borrowing_status_available));
				item.ItemImage = R.drawable.ic_book_bookmark_green_256;
				container.setBackgroundColor(_res.getColor(R.color.borrowing_item_green));
			}
			else
			{
				text1.setText(_res.getString(R.string.borrowing_status_waiting));
				item.ItemImage = R.drawable.ic_book_bookmark_256;
				container.setBackgroundColor(Color.WHITE);
			}
			
			text2.setVisibility(View.GONE);
			text3.setVisibility(View.GONE);
			
			button.setVisibility(View.GONE);
			button.setText(R.string.borrowing_button_checkOut);
		}

		if (item.ItemImage == -1)
			imageView.setVisibility(View.GONE);
		else
		{
			imageView.setVisibility(View.VISIBLE);
			imageView.setImageResource(item.ItemImage);
		}
		
		return rootView;
	}

	@Override
	public void onClick(View v) {

		Integer loanId = (Integer) v.getTag();
		if (loanId == null) return;

		ConnectivityManager conManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		
		if (info != null && info.isConnected())
		{
			Loan.checkInLoan(loanId, new Loan.CallbackReference() {
				@Override
				public void callbackFunction(Object result) {
					
					Boolean success = (Boolean) result;
					if (success == null || !success)
						Toast.makeText(_context, _res.getString(R.string.info_technicalIssues), Toast.LENGTH_LONG).show();
					Toast.makeText(_context, _res.getString(R.string.borrowing_info_loanableCheckedIn), Toast.LENGTH_SHORT).show();
					_updateRef.updateData();
				}
			});
		}
		else
		{
			Toast.makeText(_context, _res.getString(R.string.info_noInternet), Toast.LENGTH_LONG).show();
		}
	}
}
