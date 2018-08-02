package com.sealstudios.bakinapp;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.sealstudios.Utils.Constants;
import com.sealstudios.adapters.CustomFragmentAdapter;
import com.sealstudios.adapters.CustomPagerAdapter;
import com.sealstudios.objects.Recipe;
import com.sealstudios.objects.Step;

import java.util.ArrayList;

import javax.sql.DataSource;

/**
 * A fragment representing a single recipe detail screen.
 * This fragment is either contained in a {@link recipeListActivity}
 * in two-pane mode (on tablets) or a {@link recipeDetailActivity}
 * on handsets.
 */
public class recipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */

    /**
     * The dummy content this fragment is presenting.
     */
    private ArrayList<Step> steps;
    private Recipe recipeItem;
    private String TAG = "BA_rcpDtlFrgmnt";
    private int position;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public recipeDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
        if (getArguments() != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Bundle bundle = getArguments();
            steps = bundle.getParcelableArrayList(Constants.STEP);
            position = bundle.getInt(Constants.POSITION);
        }
        if (savedInstanceState != null){
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);
        ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        //final CustomPagerAdapter customPagerAdapter = new CustomPagerAdapter(getActivity(),steps);
        CustomFragmentAdapter fragmentAdapter = new CustomFragmentAdapter(getChildFragmentManager(),steps);
        viewPager.setAdapter(fragmentAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                   //customPagerAdapter.releasePlayer();
            }

            @Override
            public void onPageSelected(int position) {
                if (((AppCompatActivity)getActivity()).getSupportActionBar() != null){
                    ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(steps.get(position).getShortDescription());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelableArrayList(Constants.STEP, steps);
        outState.putInt(Constants.POSITION,position);
        super.onSaveInstanceState(outState);
    }
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            steps = savedInstanceState.getParcelableArrayList(Constants.STEP);
            position = savedInstanceState.getInt(Constants.POSITION);
        }else{
        }
        super.onViewStateRestored(savedInstanceState);
    }
}
