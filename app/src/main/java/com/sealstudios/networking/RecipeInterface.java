package com.sealstudios.networking;

import com.sealstudios.Utils.Constants;
import com.sealstudios.objects.Recipe;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RecipeInterface {

    @GET(Constants.END_POINT)
    Call<ArrayList<Recipe>> getAllRecipes();
}
