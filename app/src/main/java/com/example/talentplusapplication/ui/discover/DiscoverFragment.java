package com.example.talentplusapplication.ui.discover;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talentplusapplication.MyApplication;
import com.example.talentplusapplication.Proxy.SearchProxy;
import com.example.talentplusapplication.R;
import com.example.talentplusapplication.Utility;
import com.example.talentplusapplication.ui.user.UserProfileActivity;
import com.example.talentplusapplication.webservices.ApiClient;
import com.example.talentplusapplication.webservices.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DiscoverFragment extends Fragment implements DiscoverAdapter.OnLoadDiscoverInterface {

    private static final String TAG = "DiscoverFragment";
    private DashboardViewModel dashboardViewModel;
    private SearchView searchView;

    private SearchView.OnQueryTextListener queryTextListener;
    private List<SearchProxy> searchProxyList;;
    private   ImageView ivSearchContact;
    private EditText ivSearchNameEdt;;
    private TextView ivSearchTextView;;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_discover, container, false);

         ivSearchContact = (ImageView) root.findViewById(R.id.ivSearchIcon);
        ivSearchNameEdt =  root.findViewById(R.id.etSearchUser);
        ivSearchTextView =  root.findViewById(R.id.textView_discover);
        mRecyclerView = root.findViewById(R.id.recyclerView_discover);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MyApplication.getAppContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        ivSearchContact.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Log.d(TAG, "onClick: clicked searched icon");

                if (new Utility(MyApplication.getAppContext()).isConnectingToInternet()) {
                    onCallSearchByUser(ivSearchNameEdt.getText().toString());
                }else {
                    onShowToast("Internet connection is not available");
                }

            }

        });
        return root;
    }

    private void onCallSearchByUser(String userName) {

        final ProgressDialog dialog = new ProgressDialog(this.getActivity());
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(false);
        dialog.setMessage("Please wait");
        dialog.show();
        ApiInterface mApiInterface = ApiClient.getClient(MyApplication.getAppContext()).create(ApiInterface.class);

        mApiInterface.searchUserByName(userName).enqueue(new Callback<List<SearchProxy>>() {
            @Override
            public void onResponse(Call<List<SearchProxy>> call, Response<List<SearchProxy>> response) {



                dialog.dismiss();
                if (response.isSuccessful()) {

                   searchProxyList=response.body();
                   onShowSearchResult();
                }
            }

            @Override
            public void onFailure(Call<List<SearchProxy>> call, Throwable t) {
                onShowToast("Something is wrong");
            }
        });




    }

    private void onShowSearchResult() {
        if (searchProxyList!=null){

            ivSearchTextView.setVisibility(View.GONE);

            Log.e(TAG,"resultr "+ searchProxyList.size());
            DiscoverAdapter mDiscoverAdapter=new DiscoverAdapter(this.getContext(),searchProxyList,this);
            mRecyclerView.setAdapter(mDiscoverAdapter);
        }
    }

    private void onShowToast(String message) {

        Toast.makeText(MyApplication.getAppContext(), message, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onLoadUserSearchClick(int position) {

        Intent mIntent=new Intent(MyApplication.getAppContext(), UserProfileActivity.class);
        mIntent.putExtra("OtherUserId",searchProxyList.get(position).getUserId());
        startActivity(mIntent);
    }
}