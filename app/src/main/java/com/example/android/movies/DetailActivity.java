package com.example.android.movies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Intent getdata = getIntent();
        ItemsClass extrass = getdata.getParcelableExtra("ItemObject");
        Bundle extras = new Bundle();
        extras.putParcelable("ItemObject",extrass);
        if (extras != null) {
            Detail_fragment detail_fragment = new Detail_fragment();
            detail_fragment.setArguments(extras);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.activity_detail, detail_fragment)
                    .commit();
        }

    }
}
