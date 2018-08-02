package com.sealstudios.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sealstudios.Utils.Constants;
import com.sealstudios.adapters.WidgetAdapter;
import com.sealstudios.bakinapp.IngredientService;
import com.sealstudios.bakinapp.MainActivity;
import com.sealstudios.bakinapp.R;
import com.sealstudios.bakinapp.recipeListActivity;
import com.sealstudios.objects.Ingredient;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class BakinAppWidget extends AppWidgetProvider {

    // Called when we receive an Intent from the service.
    // Checks the intent so we can make different actions
    @Override
    public void onReceive(Context context, Intent intent) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        if (intent.getAction().equals(Constants.LAUNCH_ACTIVITY)) {
            int appWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);

            SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS,Context.MODE_PRIVATE);
            int recipeId = sharedPreferences.getInt(Constants.RECIPE_ID,0);
            Intent i = new Intent(context, MainActivity.class);
            Bundle b = new Bundle();
            b.putInt(Constants.RECIPE_ID, recipeId);
            i.putExtras(b);

            context.startActivity(i);
        }else if(intent.getAction().equals(Constants.DONT_LAUNCH_ACTIVITY)){
            Toast.makeText(context,R.string.no_recipe,Toast.LENGTH_SHORT).show();
        }
        super.onReceive(context, intent);
    }

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS,Context.MODE_PRIVATE);
            int recipeId = sharedPreferences.getInt(Constants.RECIPE_ID,0);
            String recipeName = sharedPreferences.getString(Constants.RECIPE_NAME,"");
            Intent intent = new Intent(context, IngredientService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_list_view);
            //populates data
            remoteViews.setTextViewText(R.id.recipe_name,recipeName);
            remoteViews.setRemoteAdapter(appWidgetId, R.id.widget_list, intent);
            remoteViews.setEmptyView(R.layout.widget_list_view,R.id.no_results);
            remoteViews.setViewVisibility(R.id.no_results, View.GONE);

            Intent launchIntent = new Intent(context, BakinAppWidget.class);
            launchIntent.setAction(Constants.LAUNCH_ACTIVITY);
            launchIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            PendingIntent toastPendingIntent = PendingIntent.getBroadcast(context, 0, launchIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            remoteViews.setPendingIntentTemplate(R.id.widget_list, toastPendingIntent);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onAppWidgetOptionsChanged(Context context, AppWidgetManager appWidgetManager, int appWidgetId, Bundle newOptions) {
        updateAppWidget(context, appWidgetManager, appWidgetId);
        super.onAppWidgetOptionsChanged(context, appWidgetManager, appWidgetId, newOptions);
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

