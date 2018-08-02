package com.sealstudios.bakinapp;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewAnimationUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sealstudios.Utils.Constants;
import com.sealstudios.Utils.ItemTouchHelper;
import com.sealstudios.adapters.RecipeAdapter;
import com.sealstudios.adapters.WidgetAdapter;
import com.sealstudios.networking.NetworkInstance;
import com.sealstudios.networking.RecipeInterface;
import com.sealstudios.objects.Ingredient;
import com.sealstudios.objects.Recipe;
import com.sealstudios.widget.BakinAppWidget;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    GridLayoutManager gridLayoutManager;
    RecipeAdapter recipeAdapter;
    int gridSpanCount;
    private final String TAG = "BA_MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        gridSpanCount = this.getResources().getInteger(R.integer.recipe_grid_count);
        recyclerView = findViewById(R.id.recipe_recycler);
        ArrayList<Recipe> recipeArrayList = new ArrayList<>();
        gridLayoutManager = new GridLayoutManager(this,gridSpanCount,GridLayoutManager.VERTICAL,false);
        ItemTouchHelper.OnItemTouchListener itemTouchListener = new ItemTouchHelper.OnItemTouchListener() {
            @Override
            public void onCardClick(View view, int position) {
                //start new activity
                Recipe recipe = recipeAdapter.getItemAtPosition(position);
                Bundle b = new Bundle();
                b.putParcelable(Constants.RECIPE, recipe);
                Intent i = new Intent(MainActivity.this, recipeListActivity.class);
                i.putExtras(b);
                SharedPreferences sharedPreferences = MainActivity.this.getSharedPreferences(Constants.SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor prefsEditor = sharedPreferences.edit();
                Gson gson = new Gson();
                String ingredientListText = gson.toJson(recipe.getIngredients());
                prefsEditor.putString(Constants.INGEDIENTS_SAVED, ingredientListText);
                prefsEditor.putInt(Constants.RECIPE_ID, recipe.getId());
                prefsEditor.putString(Constants.RECIPE_NAME, recipe.getName());
                prefsEditor.apply();

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, BakinAppWidget.class);
                intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
                int[] ids = appWidgetManager.getAppWidgetIds(new ComponentName(getApplication(),BakinAppWidget.class));
                appWidgetManager.notifyAppWidgetViewDataChanged(ids,R.id.widget_list);
                //intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
                //sendBroadcast(intent);
                for (int id : ids){
                    BakinAppWidget.updateAppWidget(MainActivity.this,appWidgetManager,id);
                }
                startActivity(i);
            }

            @Override
            public void onCardLongClick(View view, int position) {

            }
        };
        recipeAdapter = new RecipeAdapter(recipeArrayList,this,itemTouchListener);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(recipeAdapter);
        /*Create handle for the RetrofitInstance interface*/
        getRecipes();
    }

    public void getRecipes(){
        RecipeInterface recipeInterface = NetworkInstance.getRetrofitInstance().create(RecipeInterface.class);
        Call<ArrayList<Recipe>> call = recipeInterface.getAllRecipes();
        call.enqueue(new Callback<ArrayList<Recipe>>() {
            @Override
            public void onResponse(Call<ArrayList<Recipe>> call, Response<ArrayList<Recipe>> response) {
                recipeAdapter.refreshMyList(response.body());
                final Bundle data = getIntent().getExtras();
                if (data != null){
                    if (data.containsKey(Constants.RECIPE_ID)){
                        Recipe recipe = new Recipe();
                        for (Recipe myRecipe : recipeAdapter.getList()){
                            if (myRecipe.getId() == data.getInt(Constants.RECIPE_ID)){
                                recipe = myRecipe;
                            }
                        }
                        Bundle b = new Bundle();
                        b.putParcelable(Constants.RECIPE, recipe);
                        Intent i = new Intent(MainActivity.this, recipeListActivity.class);
                        i.putExtras(b);
                        startActivity(i);

                    }
                }

            }

            @Override
            public void onFailure(Call<ArrayList<Recipe>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Oops sorry, try again later", Toast.LENGTH_SHORT).show();
                Log.d(TAG,t.getMessage());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
