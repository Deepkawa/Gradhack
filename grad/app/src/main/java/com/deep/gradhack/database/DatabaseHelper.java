package com.deep.gradhack.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deep.gradhack.Domain.CardDetails;
import com.deep.gradhack.database.model.SQLCard;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "cards_db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(SQLCard.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + SQLCard.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }

    public long insertCard(CardDetails cardDetails) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(SQLCard.CARD_NUMBER, cardDetails.getCardNumber());
        values.put(SQLCard.EXPIRY_DATE, cardDetails.getExpiryDate());

        long id = db.insert(SQLCard.TABLE_NAME, null, values);
        db.close();
        return id;
    }

    public ArrayList<CardDetails> getAllCards() {
        ArrayList<CardDetails> cardDetailsArrayList = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + SQLCard.TABLE_NAME ;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CardDetails cardDetail = new CardDetails();
                cardDetail.setCardNumber(cursor.getString(cursor.getColumnIndex(SQLCard.CARD_NUMBER)));
                cardDetail.setExpiryDate(cursor.getString(cursor.getColumnIndex(SQLCard.EXPIRY_DATE)));

                cardDetailsArrayList.add(cardDetail);
            } while (cursor.moveToNext());
        }
        db.close();
        return cardDetailsArrayList;
    }
}
