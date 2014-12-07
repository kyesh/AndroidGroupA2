package se.hj.androidgroupa2;

import java.util.ArrayList;

import se.hj.androidgroupa2.objects.Loan;
import se.hj.androidgroupa2.objects.Reservation;
import se.hj.androidgroupa2.objects.UpdateDataInterface;
import android.app.Fragment;
import android.content.Context;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

public class BorrowingsFragment extends Fragment implements UpdateDataInterface {

	private ProgressBar _progressBar;
	private ListView _list;
	private ArrayList<BorrowingAdapterItem> _listItems;
	private BorrowingAdapter _listAdapter;
	private Resources _res;
	
	private boolean _doneLoadingSome;
	
	public BorrowingsFragment()
	{}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_borrowings, container, false);
		
		_res = getActivity().getResources();
		
		_progressBar = (ProgressBar) rootView.findViewById(R.id.borrowings_progressBar);
		_list = (ListView) rootView.findViewById(R.id.borrowings_list);
		
		_listItems = new ArrayList<BorrowingAdapterItem>();
		_listAdapter = new BorrowingAdapter(getActivity(), R.layout.borrowing_list_item, R.id.borrowings_list_item_title, _listItems, this);
		_list.setAdapter(_listAdapter);
		updateData();
		
		return rootView;
	}

	@Override
	public void updateData() {

		_progressBar.setVisibility(View.VISIBLE);
		_list.setVisibility(View.GONE);
		_doneLoadingSome = false;
		_listItems.clear();
		
		ConnectivityManager conManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = conManager.getActiveNetworkInfo();
		
		if (info != null && info.isConnected())
		{
			Loan.getCurrentUserLoans(new Loan.CallbackReference() {
				@Override
				public void callbackFunction(Object result) {

					ArrayList<Loan> loans = (ArrayList<Loan>) result;
					
					if (loans.size() != 0)
					{
						BorrowingAdapterItem item = new BorrowingAdapterItem();
						item.Header = _res.getString(R.string.borrowing_loansTitle);
						_listItems.add(item);
					}
					for (int i = 0; i < loans.size(); i++)
					{
						BorrowingAdapterItem item = new BorrowingAdapterItem();
						item.BorrowerLoan = loans.get(i);
						_listItems.add(item);
					}
					_listAdapter.notifyDataSetChanged();
					
					if (_doneLoadingSome)
					{
						_progressBar.setVisibility(View.GONE);
						_list.setVisibility(View.VISIBLE);
					}
					else _doneLoadingSome = true;
				}
			});

			Reservation.getCurrentUserReservations(new Reservation.CallbackReference() {
				@Override
				public void callbackFunction(ArrayList<Reservation> reservations) {

					if (reservations.size() != 0)
					{
						BorrowingAdapterItem item = new BorrowingAdapterItem();
						item.Header = _res.getString(R.string.borrowing_reservationsTitle);
						_listItems.add(item);
					}
					for (int i = 0; i < reservations.size(); i++)
					{
						BorrowingAdapterItem item = new BorrowingAdapterItem();
						item.BorrowerReservation = reservations.get(i);
						_listItems.add(item);
					}
					_listAdapter.notifyDataSetChanged();

					if (_doneLoadingSome)
					{
						_progressBar.setVisibility(View.GONE);
						_list.setVisibility(View.VISIBLE);
					}
					else _doneLoadingSome = true;
				}
			});
		}
		else
		{
			Toast.makeText(getActivity(), _res.getString(R.string.info_noInternet), Toast.LENGTH_LONG).show();
			_progressBar.setVisibility(View.GONE);
			_list.setVisibility(View.VISIBLE);
		}
	}
}
