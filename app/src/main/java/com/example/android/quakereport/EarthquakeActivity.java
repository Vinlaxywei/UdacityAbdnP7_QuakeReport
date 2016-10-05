package com.example.android.quakereport;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthQuake>> {
    public static final String LOG_TAG = EarthquakeActivity.class.getName();
    ArrayList<EarthQuake> mEarthquakes;
    EarthQuakeListAdapter mAdapter;
    ProgressBar mProgressBar;
    ListView mListView;
    TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mEarthquakes = new ArrayList<>();

        mListView = (ListView) findViewById(R.id.list_view);
        mAdapter = new EarthQuakeListAdapter(this, mEarthquakes);
        mListView.setAdapter(mAdapter);

        emptyView = (TextView) findViewById(R.id.empty_View);
        mListView.setEmptyView(emptyView);

//        Button button = (Button) findViewById(R.id.btn_refresh);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "长按打开相关地震检测信息", Toast.LENGTH_SHORT).show();
            }
        });

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                EarthQuake earthQuake = mEarthquakes.get(position);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(earthQuake.getmUrl()));
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
                return true;
            }
        });

        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_setting:
                startActivity(new Intent(this, SettingActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<EarthQuake>> onCreateLoader(int id, Bundle args) {
        Log.d(LOG_TAG, "onCreateLoader: ");
        if (isOnline()) {
            return new EventLoader(this);
        } else {
            emptyView.setText(R.string.emptyview_network_incorrect);
            mProgressBar.setVisibility(View.INVISIBLE);
            return null;
        }
    }

    @Override
    public void onLoadFinished(Loader<List<EarthQuake>> loader, List<EarthQuake> data) {
        Log.d(LOG_TAG, "onLoadFinished: ");

        if (data != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
            mAdapter.clear();
            mAdapter.addAll(data);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<EarthQuake>> loader) {
        Log.d(LOG_TAG, "onLoaderReset: ");
        mAdapter.clear();
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
