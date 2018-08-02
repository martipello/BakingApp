package com.sealstudios.objects;

import android.os.Parcel;
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.v7.widget.RecyclerView;

import com.sealstudios.bakinapp.MainActivity;
import com.sealstudios.bakinapp.R;

import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

public class IngredientTest {

    @Test
    public void describeContents() {
    }

    @Test
    public void writeToParcel() {
    }

    @Test
    public void isParcleable(){
        Ingredient ingredient = new Ingredient(1.0,"measure", "ingredient");

        Parcel parcel = Parcel.obtain();
        ingredient.writeToParcel(parcel, ingredient.describeContents());
        parcel.setDataPosition(0);

        Ingredient createdFromParcel = Ingredient.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getQuantity(), is(1.0));
        assertThat(createdFromParcel.getMeasure(), is("measure"));
        assertThat(createdFromParcel.getIngredient(), is("ingredient"));
    }
}