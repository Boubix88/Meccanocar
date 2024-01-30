package com.example.meccanocar.model;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.ByteArrayOutputStream;

public class Item implements Parcelable {
    private static final long serialVersionUID = 1L;

    private String name;
    private String description;
    private String[][] tabRef;
    private byte[] bmp;
    private Context context;

    public Item(String n, String d, String[][] r, Bitmap bmp, Context context) {
        this.name = n;
        this.description = d;
        this.tabRef = r;
        this.bmp = convertBitmapToByteArray(bmp);
        this.context = context;
    }

    protected Item(Parcel in) {
        name = in.readString();
        description = in.readString();

        // Lecture du nombre de lignes et de colonnes dans le tableau bidimensionnel
        int rows = in.readInt();
        int cols = in.readInt();

        // Lecture du tableau bidimensionnel depuis le Parcel
        tabRef = new String[rows][cols];
        for (int i = 0; i < rows; i++) {
            tabRef[i] = in.createStringArray();
        }

        bmp = in.createByteArray();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String[][] getTabRef() {
        return tabRef;
    }

    public Bitmap getBitmap() {
        if (bmp != null) {
            return convertByteArrayToBitmap(bmp);
        }

        // Chargez l'image par défaut à partir des ressources
        int resId = Resources.getSystem().getIdentifier("no_image", "drawable", "android");
        return BitmapFactory.decodeResource(Resources.getSystem(), resId);
    }

    public Bitmap convertByteArrayToBitmap(byte[] byteArray) {
        return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
    }

    public byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(description);

        // Utilisez writeInt pour écrire le nombre de lignes et de colonnes dans le tableau bidimensionnel
        parcel.writeInt(tabRef.length);
        parcel.writeInt(tabRef.length > 0 ? tabRef[0].length : 0);

        // Utilisez writeStringArray pour écrire un tableau de chaînes
        for (String[] row : tabRef) {
            parcel.writeStringArray(row);
        }

        parcel.writeByteArray(bmp);
    }
}