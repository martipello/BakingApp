package com.sealstudios.bakinapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.sealstudios.Utils.Constants;
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
public class DetailPagingFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static DetailPagingFragment init(int val, Step step) {
        DetailPagingFragment detailFrag = new DetailPagingFragment();
        // Supply val input as an argument.
        Bundle args = new Bundle();
        args.putInt(Constants.POSITION, val);
        args.putParcelable(Constants.STEP, step);
        detailFrag.setArguments(args);
        return detailFrag;
    }

    private Context mContext;
    private Step step;
    private String TAG = "BA_DtlPgFrgmnt";
    private int position;
    private SimpleExoPlayer player;
    private PlayerView playerView;
    private Player.EventListener eventListener;
    private VideoListener videoListener;
    private recipeDetailActivity recipeDetailActivity;
    private long playbackPosition = 0;
    private boolean playWhenReady = false;
    private int currentWindow = 0;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public DetailPagingFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            Bundle bundle = getArguments();
            step = bundle.getParcelable(Constants.STEP);
            setStep(step);
            position = bundle.getInt(Constants.POSITION);
        }

        if (savedInstanceState != null) {
            step = savedInstanceState.getParcelable(Constants.STEP);
            setStep(step);
            position = savedInstanceState.getInt(Constants.POSITION);
            playbackPosition = savedInstanceState.getLong(Constants.VIDEO_POSITION);
        }
        step = getStep();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        mContext = getActivity();
        TextView shortDescription = rootView.findViewById(R.id.recipe_short_detail_text);
        TextView descriptionText = rootView.findViewById(R.id.recipe_detail_text);
        playerView = rootView.findViewById(R.id.playerView);
        descriptionText.setText(step.getDescription());
        shortDescription.setText(step.getShortDescription());
        return rootView;
    }

    private void initializePlayer() {
        if (step.getVideoURL().isEmpty()){
            playerView.setVisibility(View.GONE);
        }else{
            playerView.setVisibility(View.VISIBLE);
            Uri uri = Uri.parse(step.getVideoURL());
            player = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(mContext),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            playerView.setPlayer(player);
            player.setPlayWhenReady(playWhenReady);
            player.seekTo(currentWindow, playbackPosition);
            MediaSource mediaSource = buildMediaSource(uri);
            player.prepare(mediaSource, true, false);
        }

    }

    private MediaSource buildMediaSource(Uri uri) {
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")).
                createMediaSource(uri);
    }

    public void setStep(Step step) {
        this.step = step;
    }

    public Step getStep() {
        return step;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(Constants.STEP, step);
        outState.putInt(Constants.POSITION, position);
        outState.putLong(Constants.VIDEO_POSITION, playbackPosition);
        setStep(step);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            step = savedInstanceState.getParcelable(Constants.STEP);
            position = savedInstanceState.getInt(Constants.POSITION);
            playbackPosition = savedInstanceState.getLong(Constants.VIDEO_POSITION);
            setStep(step);
        }
        super.onViewStateRestored(savedInstanceState);
    }

    private long getCurrentPlayerPosition() {
        return player.getCurrentPosition();
    }

    @SuppressLint("InlinedApi")
    private void hideSystemUi() {
        playerView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            initializePlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        hideSystemUi();
        if ((Util.SDK_INT <= 23 || player == null)) {
            initializePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (player != null) {
            playbackPosition = player.getCurrentPosition();
            currentWindow = player.getCurrentWindowIndex();
            playWhenReady = player.getPlayWhenReady();
            player.release();
            player = null;
        }
    }

    private void resumePlaybackFromPreviousPosition(int prevPosition) {
        player.seekTo(playbackPosition);
    }


}
