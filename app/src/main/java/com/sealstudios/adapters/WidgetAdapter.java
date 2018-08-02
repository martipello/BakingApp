package com.sealstudios.adapters;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sealstudios.Utils.Constants;
import com.sealstudios.bakinapp.MainActivity;
import com.sealstudios.bakinapp.R;
import com.sealstudios.objects.Ingredient;
import com.sealstudios.widget.BakinAppWidget;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class WidgetAdapter implements RemoteViewsService.RemoteViewsFactory {


    Context context;
    ArrayList<Ingredient> ingredients;
    String TAG = "wdgtAdptr";
    private int widgetId;

    public WidgetAdapter(Context context,Intent intent) {
        this.context = context;
        widgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,AppWidgetManager.INVALID_APPWIDGET_ID);
    }

    @Override
    public void onCreate() {
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS,Context.MODE_PRIVATE);
        int recipeId = sharedPreferences.getInt(Constants.RECIPE,0);
        //get all ingredients not sure how yet
        Gson gson = new Gson();
        String response = sharedPreferences.getString(Constants.INGEDIENTS_SAVED , "");
        ingredients = gson.fromJson(response,
                new TypeToken<ArrayList<Ingredient>>(){}.getType());
    }

    @Override
    public void onDataSetChanged() {
        ingredients.clear();
        SharedPreferences sharedPreferences = context.getSharedPreferences(Constants.SHARED_PREFS,Context.MODE_PRIVATE);
        int recipeId = sharedPreferences.getInt(Constants.RECIPE_ID,0);
        //get all ingredients not sure how yet
        Gson gson = new Gson();
        String response = sharedPreferences.getString(Constants.INGEDIENTS_SAVED , "");
        ingredients = gson.fromJson(response,
                new TypeToken<ArrayList<Ingredient>>(){}.getType());
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(context,BakinAppWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.layout.widget_list_view);

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (ingredients == null) return 0;
        return ingredients.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        Ingredient ingredient = ingredients.get(position);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(),R.layout.bakin_app_widget);

        String quantity = String.valueOf(ingredient.getQuantity()) + " " + ingredient.getMeasure();
        remoteViews.setTextViewText(R.id.quantity_text,quantity);
        remoteViews.setTextViewText(R.id.ingredient_text,String.valueOf(ingredient.getIngredient()));
        //passing this int allows the system to distinguish between intents
        Bundle extras = new Bundle();
        extras.putInt(Constants.LAUNCH_ACTIVITY, position);
        Intent fillInIntent = new Intent();
        fillInIntent.putExtras(extras);

        remoteViews.setOnClickFillInIntent(R.id.ingredient_holder, fillInIntent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return ingredients.size();
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }
}
