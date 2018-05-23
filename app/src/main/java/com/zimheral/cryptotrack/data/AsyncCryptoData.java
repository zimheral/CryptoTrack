package com.zimheral.cryptotrack.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.zimheral.cryptotrack.R;
import com.zimheral.cryptotrack.Retrofit.Client;
import com.zimheral.cryptotrack.pojos.Currency;
import com.zimheral.cryptotrack.pojos.CurrencyData;
import com.zimheral.cryptotrack.pojos.CurrencyList;
import com.zimheral.cryptotrack.pojos.CurrencyListData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import retrofit2.Call;

public class AsyncCryptoData extends AsyncTask<String, Integer, Boolean> {

    private ArrayList<Currency> currencyData = new ArrayList<>();
    private Map<String,String> currencyMap = new HashMap<>();
    private Context context;

    public AsyncCryptoData(Context applicationContext) {
        super();
        this.context = applicationContext;
    }

    @Override
    protected Boolean doInBackground(String... id) {
        try {
            if(id!=null && id.length>0){
                return getData(id[0]);
            }else {
                return getData();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    protected boolean getData() throws IOException {

        Call<CurrencyListData> cListJson = Client.getService().getCurrencyList();
        List<CurrencyList> currencyListsArr = null;

        currencyListsArr = cListJson.execute().body().getData();

        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Set<String> currencySet;

        if (!(currencyListsArr == null) && !currencyListsArr.isEmpty()) {

            currencySet = sharedPref.getStringSet(context.getString(R.string.currency_set),null);
            if(currencySet==null) {
                currencySet = new HashSet<>(Arrays.asList(new String[]{"BTC", "ETH", "BCH"}));
                editor.putStringSet(context.getString(R.string.currency_set), currencySet);
                editor.commit();
            }


            for (CurrencyList c : currencyListsArr) {
                currencyMap.put(c.getName(),String.valueOf(c.getId()));
                for (Iterator<String> it = currencySet.iterator(); it.hasNext(); ) {
                    if (it.next().equals(c.getSymbol())) {
                        System.out.println(c.getSymbol());
                        Call<CurrencyData> call = Client.getService().getCurrencyData(String.valueOf(c.getId()));
                        currencyData.add((Currency) call.execute().body().getData());
                        continue;
                    }
                }
            }

            return true;
        }else{
            return false;
        }
    }

    protected boolean getData(String id) throws IOException {

        Call<CurrencyData> call = Client.getService().getCurrencyData(String.valueOf(id));
        currencyData.add((Currency) call.execute().body().getData());
        return true;
    }

    public ArrayList<Currency> getCurrencyData() {
        return currencyData;
    }

    public Map<String, String> getCurrencyMap() {
        return currencyMap;
    }
}
