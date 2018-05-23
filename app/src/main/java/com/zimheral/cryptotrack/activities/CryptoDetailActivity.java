package com.zimheral.cryptotrack.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zimheral.cryptotrack.R;
import com.zimheral.cryptotrack.pojos.Currency;

public class CryptoDetailActivity extends AppCompatActivity {

    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crypto_detail_layout);

        currency = (Currency) getIntent().getSerializableExtra("currency_detail");

        TextView price  = (TextView) findViewById(R.id.tView_price);
        price.setText(String.valueOf(currency.getQuotes().getUSD().getPrice()));

        TextView marketCap  = (TextView) findViewById(R.id.tView_marketCap);
        marketCap.setText(String.valueOf(currency.getQuotes().getUSD().getMarketCap()));

        TextView volume  = (TextView) findViewById(R.id.tView_volume);
        volume.setText(String.valueOf(currency.getQuotes().getUSD().getVolume24h()));

        TextView change  = (TextView) findViewById(R.id.tView_change);
        change.setText(String.valueOf(currency.getQuotes().getUSD().getPercentChange24h()));

    }
}
