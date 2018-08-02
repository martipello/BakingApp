package com.sealstudios.adapters;

import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v4.view.PagerAdapter;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toolbar;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.decoder.DecoderCounters;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.dash.DashMediaSource;
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.exoplayer2.video.VideoListener;
import com.google.android.exoplayer2.video.VideoRendererEventListener;
import com.sealstudios.Utils.Constants;
import com.sealstudios.bakinapp.R;
import com.sealstudios.bakinapp.recipeDetailActivity;
import com.sealstudios.objects.Step;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

public class CustomPagerAdapter extends PagerAdapter {

    private Context mContext;
    private ArrayList<Step> steps;
    private String TAG = "BA_CstmPgAdptr";
    private String status;
    private Player player;
    private Player.EventListener eventListener;
    private VideoListener videoListener;
    private recipeDetailActivity recipeDetailActivity;
    private long mPlayerCurrentPosition = 0;

    public CustomPagerAdapter(Context context, ArrayList<Step> adapterSteps) {
        mContext = context;
        this.steps = adapterSteps;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        Step step = steps.get(position);
        ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.recipe_detail, collection,false);
        TextView shortDescription = layout.findViewById(R.id.recipe_short_detail_text);
        TextView descriptionText = layout.findViewById(R.id.recipe_detail_text);
        Uri uri = Uri.parse(step.getVideoURL());
        PlayerView playerView = layout.findViewById(R.id.playerView);
        descriptionText.setText(step.getDescription());
        shortDescription.setText(step.getShortDescription());
        collection.addView(layout);
        Handler mainHandler = new Handler();
        // Measures bandwidth during playback. Can be null if not required.
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        SimpleExoPlayer player =
                ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);

        playerView.setPlayer(player);

// Produces DataSource instances through which media data is loaded.

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,Util.getUserAgent(mContext,Constants.APP_NAME));
        /*
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext,
                Util.getUserAgent(mContext, Constants.APP_NAME), bandwidthMeter);
                */
// This is the MediaSource representing the media to be played.
        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                .createMediaSource(uri);
// Prepare the player with the source.

        eventListener = new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        };
        videoListener = new VideoListener() {
            @Override
            public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {
                //TODO make video fullscreen
            }

            @Override
            public void onRenderedFirstFrame() {

            }
        };
        player.addListener(eventListener);
        player.addVideoListener(videoListener);
        player.prepare(videoSource);
        player.seekTo(0);
        return layout;
    }

    private long getCurrentPlayerPosition() {
        return player.getCurrentPosition();
    }


    public void releasePlayer() {
        mPlayerCurrentPosition = getCurrentPlayerPosition();
        player.setPlayWhenReady(false);
        player.release(); // this will make the player object eligible for GC
    }

    private void resumePlaybackFromPreviousPosition(int prevPosition) {
        player.seekTo(mPlayerCurrentPosition );
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public int getCount() {
        //return CustomPagerEnum.values().length;
        return steps.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return String.valueOf(steps.get(position).getId());
    }
}