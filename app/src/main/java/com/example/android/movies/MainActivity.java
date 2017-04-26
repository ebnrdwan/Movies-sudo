package com.example.android.movies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Object>, handlerCallback {
    private static final String DETAILFRAGMENT_TAG = "DF";
    private Bitmap theposterImage;
    private String theposter;
    String StoredPoster;
    private String thetitle;
    private String theoverView;
    private String thereleaseDate;
    private double therating;
    public static int id;
    public String videoKey;
    public String videoTitle;
    public static ArrayList<ItemTrailer> itemTrailerList;
    adapterTrailer adapterTrailer;
    ListView trailersList;
    adapterReview adapterReview;
    ListView reviewList;
    ArrayList<ItemReview> itemReviewsList;
    boolean ischecked = true;
    ArrayList<ItemsClass> serItems;
    SharedPreferences mypreference;
    public static boolean favClicked = false;
    final boolean[] va = {true};
    private boolean mTwoPane;

//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
//        {
//            setContentView(R.layout.activity_main);
//        }
//        else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
//        {
//            setContentView(R.layout.activity_main);
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        if (savedInstanceState == null) {
            Main_fragment main_fragment = new Main_fragment();
            main_fragment.setCurrentItem(this);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.one_pane, main_fragment)
                    .commitAllowingStateLoss();
//        }
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.two_pane);
        if (frameLayout != null) {
            // The detail container view will be present only in the large-screen layouts
            // (res/layout-sw600dp). If this view is present, then the activity should be
            // in two-pane mode.
            mTwoPane = true;
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.

        } else {
            mTwoPane = false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.setting) {
            Intent settingIntent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(settingIntent);
            return true;
        }
        return true;
    }

    public void onClicked() {

        favClicked = !favClicked;

    }

    @Override
    public Loader<Object> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Object> loader, Object o) {

    }

    @Override
    public void onLoaderReset(Loader<Object> loader) {

    }

    @Override
    public void onItemSelected(ItemsClass currentObject) {
        if (mTwoPane == true) {
            // In two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Detail_fragment fragment = new Detail_fragment();
            Bundle args = new Bundle();
            args.putParcelable("ItemObject", currentObject);
            fragment.setArguments(args);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.two_pane, fragment, DETAILFRAGMENT_TAG)
                    .commitAllowingStateLoss();
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
//            Bundle extra = new Bundle();
//            extra.putParcelable("ItemObject", currentObject);
                    intent.putExtra("ItemObject", currentObject);
            startActivity(intent);
        }
    }
}
