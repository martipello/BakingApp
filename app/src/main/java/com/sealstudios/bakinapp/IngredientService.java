package com.sealstudios.bakinapp;

import android.app.Service;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.sealstudios.adapters.WidgetAdapter;

public class IngredientService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetAdapter(this.getApplicationContext(), intent);
    }
}
