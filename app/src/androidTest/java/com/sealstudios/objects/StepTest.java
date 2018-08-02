package com.sealstudios.objects;

import android.os.Parcel;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class StepTest {

    @Test
    public void describeContents() {
    }

    @Test
    public void writeToParcel() {
    }

    @Test
    public void isParcleable(){
        Step step = new Step(1,"short description", "description","video url","thumbnail");

        Parcel parcel = Parcel.obtain();
        step.writeToParcel(parcel, step.describeContents());
        parcel.setDataPosition(0);

        Step createdFromParcel = Step.CREATOR.createFromParcel(parcel);
        assertThat(createdFromParcel.getId(), is(1));
        assertThat(createdFromParcel.getShortDescription(), is("short description"));
        assertThat(createdFromParcel.getDescription(), is("description"));
        assertThat(createdFromParcel.getVideoURL(), is("video url"));
        assertThat(createdFromParcel.getThumbnailURL(), is("thumbnail"));
    }
}