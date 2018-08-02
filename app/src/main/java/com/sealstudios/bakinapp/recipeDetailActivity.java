package com.sealstudios.bakinapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.NavUtils;
import android.view.MenuItem;

import com.sealstudios.Utils.Constants;
import com.sealstudios.adapters.CustomFragmentAdapter;
import com.sealstudios.adapters.CustomPagerAdapter;
import com.sealstudios.objects.Step;

import java.util.ArrayList;

/**
 * An activity representing a single recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link recipeListActivity}.
 */
public class recipeDetailActivity extends AppCompatActivity {

    ArrayList<Step> steps;
    int position;
    private Toolbar toolbar;
    private String TAG = "BA_rcpDtlAct";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);
        // Show the Up button in the action bar.
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        if (savedInstanceState == null) {}
            steps = new ArrayList<>();
            steps.addAll(getIntent().getExtras().<Step>getParcelableArrayList(Constants.STEP));
            setSteps(steps);
            position = getIntent().getExtras().getInt(Constants.POSITION);
            ViewPager viewPager = findViewById(R.id.viewpager);
            //final CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(this,steps);
            CustomFragmentAdapter fragmentAdapter = new CustomFragmentAdapter(getSupportFragmentManager(),getSteps());
            viewPager.setAdapter(fragmentAdapter);
            viewPager.setCurrentItem(position);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    //TODO debug this
                    //                //TODO calling this here happens before the player has a chance to initialise causing a null pointer
                    //customPagerAdapter.releasePlayer();
                }

                @Override
                public void onPageSelected(int position) {
                    if (getActionBar() != null)
                        getActionBar().setTitle(getSteps().get(position).getShortDescription());
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
    }

    public void setSteps(ArrayList<Step> steps) {
        this.steps = steps;
    }

    public ArrayList<Step> getSteps() {
        return steps;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. Use NavUtils to allow users
            // to navigate up one level in the application structure. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
