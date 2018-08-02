package com.sealstudios.adapters;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.sealstudios.Utils.GlideApp;
import com.sealstudios.Utils.ItemTouchHelper;
import com.sealstudios.bakinapp.R;
import com.sealstudios.objects.Recipe;

import java.util.ArrayList;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.MyViewHolder> {
    private ArrayList<Recipe> recipeList;
    private ItemTouchHelper.OnItemTouchListener onItemTouchListener;
    private Context context;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeTitle;
        public TextView recipeServing;
        public ImageView recipeImage;
        public ConstraintLayout holder;

        public MyViewHolder(View view) {
            super(view);
            recipeServing = view.findViewById(R.id.recipe_steps_count_text);
            recipeTitle = view.findViewById(R.id.recipe_title_text);
            recipeImage = view.findViewById(R.id.recipe_image);
            holder = view.findViewById(R.id.recipe_holder);
            holder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemTouchListener.onCardClick(v, getAdapterPosition());
                }
            });
            holder.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemTouchListener.onCardLongClick(v, getAdapterPosition());
                    return false;
                }
            });
        }
    }

    public RecipeAdapter(ArrayList<Recipe> recipeList, Context context,
                         ItemTouchHelper.OnItemTouchListener onItemTouchListener) {
        this.recipeList = recipeList;
        this.onItemTouchListener = onItemTouchListener;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_holder, parent, false);
        return new MyViewHolder(itemView);
    }

    public void refreshMyList(ArrayList<Recipe> list) {
        this.recipeList.clear();
        this.recipeList.addAll(list);
        this.notifyDataSetChanged();
    }

    public ArrayList<Recipe> getList() {
        return recipeList;
    }

    public Recipe getItemAtPosition(int position) {
        return recipeList.get(position);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final Recipe recipe = recipeList.get(position);
        holder.recipeTitle.setText(recipe.getName());
        holder.recipeServing.setText(context.getString(R.string.recipe_servings, recipe.getServings()));
        GlideApp.with(context)
                .load(recipe.getImage())
                .centerCrop()
                .transition(DrawableTransitionOptions.withCrossFade()) //Optional
                .skipMemoryCache(true)  //No memory cache
                .diskCacheStrategy(DiskCacheStrategy.NONE)   //No disk cache
                .placeholder(R.drawable.recipe_placeholder)
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(holder.recipeImage);
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}

