package com.zimheral.cryptotrack.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.zimheral.cryptotrack.data.AsyncCryptoData;
import com.zimheral.cryptotrack.R;
import com.zimheral.cryptotrack.pojos.Currency;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private ArrayList<Currency> currencyData = new ArrayList<>();
    private Map<String,String> currencyMap = new HashMap<>();
    private Button btn_start;
    private ProgressBar progressBar;
    private TextView progressText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);
        btn_start = (Button) findViewById(R.id.btn_start);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressText = (TextView) findViewById(R.id.progressText);

        CustomLayout mCustomLayout = (CustomLayout)  findViewById(R.id.activity_main);
        Picasso.get().load(R.drawable.main).into(mCustomLayout);
    }

    @Override
    public void onResume(){
        super.onResume();
        btn_start.setEnabled(false);
        btn_start.setVisibility(View.GONE);
        loadData();
    }

    private void loadData() {

        ConnectivityManager cm = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

        if (activeNetwork !=null) {
            new AsyncCryptoData(getApplicationContext()){
                @Override
                protected void onPreExecute() {
                    progressBar.setVisibility(View.VISIBLE);
                    progressText.setVisibility(View.VISIBLE);
                    progressText.setText("Loading data from CoinMarketCap...");
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    progressBar.setVisibility(View.GONE);
                    progressText.setVisibility(View.GONE);
                    doneBackground(result);
                    currencyData = this.getCurrencyData();
                    currencyMap  = this.getCurrencyMap();
                }
            }.execute();
        }else{
            progressBar.setVisibility(View.GONE);
            progressText.setVisibility(View.GONE);
            Toast.makeText(this, getString(R.string.netError), Toast.LENGTH_LONG).show();
        }
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, CryptoListActivity.class);
        intent.putExtra("currency_data", currencyData);
        intent.putExtra("currency_map", (Serializable) currencyMap);
        startActivity(intent);
    }

    private void doneBackground(boolean success) {
        if (success) {
            Toast.makeText(this, "Data downloaded successfully!!", Toast.LENGTH_LONG).show();
            btn_start.setEnabled(true);
            btn_start.setVisibility(View.VISIBLE);
        } else {
            Toast.makeText(this, "Data download failed. Try again later", Toast.LENGTH_LONG).show();
        }
    }
}

class CustomLayout extends android.support.constraint.ConstraintLayout implements Target {

    public CustomLayout(Context context) {
        super(context);
    }

    public CustomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        setBackground(new BitmapDrawable(getResources(), bitmap));
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {
    }
}

