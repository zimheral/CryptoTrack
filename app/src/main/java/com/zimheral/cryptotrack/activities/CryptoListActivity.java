package com.zimheral.cryptotrack.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.zimheral.cryptotrack.data.AsyncCryptoData;
import com.zimheral.cryptotrack.data.CryptoAdapter;
import com.zimheral.cryptotrack.R;
import com.zimheral.cryptotrack.pojos.Currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CryptoListActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Currency> currency;
    private ArrayList<String> currencyNames = new ArrayList<>();
    private Map<String,String> currencyMap = new HashMap<>();
    AutoCompleteTextView autoCompleteField;
    private ProgressBar progressBar;
    private Button btnAddCurrency;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypto_list_layout);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        currency    = (ArrayList<Currency>) getIntent().getSerializableExtra("currency_data");
        currencyMap = (Map<String,String>) getIntent().getSerializableExtra("currency_map");

        for( Map.Entry<String,String> a:currencyMap.entrySet()){
            currencyNames.add(a.getKey());
        }

        //Initializes Autocomplete field
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, currencyNames);
        autoCompleteField = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_view);
        autoCompleteField.setAdapter(adapter);
        
        mRecyclerView = (RecyclerView) findViewById(R.id.currency_list_view);

        //improve recyclerview performance
        mRecyclerView.setHasFixedSize(true);

        //linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // adapter
        mAdapter = new CryptoAdapter(currency, new ItemPositionRecycler() {
            @Override
            public void onItemClick(View v, int position) {

                switch (v.getId()){
                    case R.id.btnDetails:
                        Intent intent = new Intent(v.getContext(), CryptoDetailActivity.class);
                        intent.putExtra("currency_detail", currency.get(position));
                        v.getContext().startActivity(intent);
                        break;
                    case R.id.btnDelete:

                        SharedPreferences sharedPref = getSharedPreferences(
                                getString(R.string.preference_file_name), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();

                        Set<String> currencySet = new HashSet<>(sharedPref.getStringSet(getString(R.string.currency_set),null));
                        currencySet.remove(currency.get(position).getSymbol());
                        editor.putStringSet(getString(R.string.currency_set), currencySet);
                        editor.commit();

                        currency.remove(position);
                        mRecyclerView.removeViewAt(position);
                        mAdapter.notifyItemRemoved(position);
                        mAdapter.notifyItemRangeChanged(position, currency.size());
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);
                        break;
                }
            }
        });
        mRecyclerView.setAdapter(mAdapter);

        autoCompleteField.clearFocus();
        btnAddCurrency = (Button) findViewById(R.id.btnAddCurrency);
        progressBar.setVisibility(View.GONE);

    }

    //Add a new cryptocurrency to the list
    public void addItem(View view){
        String id = currencyMap.get(autoCompleteField.getText().toString());

        if(id==null || id.isEmpty()){
            showToast("Please enter a valid value on the search field");
            return;
        }

        autoCompleteField.setEnabled(false);
        btnAddCurrency.setEnabled(false);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

        for(Currency c : currency){
            if(String.valueOf(c.getId()).equals(id)){
                progressBar.setVisibility(View.GONE);
                autoCompleteField.setEnabled(true);
                btnAddCurrency.setEnabled(true);
                showToast("Currency is already on the list!!!");
                return;
            }
        }

        new AsyncCryptoData(getApplicationContext()){
            @Override
            protected void onPreExecute() {
                 progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            protected void onPostExecute(Boolean result) {
                progressBar.setVisibility(View.GONE);

                SharedPreferences sharedPref = getSharedPreferences(
                        getString(R.string.preference_file_name), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();

                Currency temp = this.getCurrencyData().get(0);
                Set<String> currencySet = new HashSet<>(sharedPref.getStringSet(getString(R.string.currency_set),null));
                currencySet.add(temp.getSymbol());
                editor.putStringSet(getString(R.string.currency_set), currencySet);
                editor.commit();

                currency.add(this.getCurrencyData().get(0));
                mAdapter.notifyDataSetChanged();

                autoCompleteField.setEnabled(true);
                btnAddCurrency.setEnabled(true);
                showToast("Success: Currency was added to the list");
            }
        }.execute(id);
    }

    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

}
