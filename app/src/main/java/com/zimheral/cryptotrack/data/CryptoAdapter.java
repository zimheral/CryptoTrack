package com.zimheral.cryptotrack.data;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zimheral.cryptotrack.R;
import com.zimheral.cryptotrack.activities.ItemPositionRecycler;
import com.zimheral.cryptotrack.pojos.Currency;
import com.zimheral.cryptotrack.pojos.USD;

import java.util.ArrayList;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.ViewHolder> {

    private ArrayList<Currency> mDataset;
    ItemPositionRecycler itemPositionRecycler;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView currencyNameView;
        private TextView currencyPriceView;

        public ViewHolder(View v) {
            super(v);
            currencyNameView =  (TextView) v.findViewById(R.id.currencyName);
            currencyPriceView = (TextView) v.findViewById(R.id.currencyPrice);
        }

        public TextView getCurrencyNameView() {
            return currencyNameView;
        }

        public TextView getCurrencyPriceView() {
            return currencyPriceView;
        }
    }

    public CryptoAdapter(ArrayList<Currency> items, ItemPositionRecycler itemPos) {
        mDataset = items;
        itemPositionRecycler = itemPos;
    }

    @Override
    public CryptoAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
            .inflate(R.layout.crypto_row_detail, parent, false);

        final ViewHolder vh = new ViewHolder(v);

        setListeners(v.findViewById(R.id.btnDetails),vh);
        setListeners(v.findViewById(R.id.btnDelete),vh);
        return vh;
    }

    private void setListeners(View v, final ViewHolder vh){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemPositionRecycler.onItemClick(v, vh.getAdapterPosition());
            }
        });
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Currency currency = (Currency) mDataset.get(position);
        USD usd = currency.getQuotes().getUSD();
        holder.getCurrencyNameView().setText(currency.getName());
        holder.getCurrencyPriceView().setText(String.valueOf(usd.getPrice()));
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}
