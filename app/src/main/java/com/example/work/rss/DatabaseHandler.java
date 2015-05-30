package com.example.work.rss;

/**
 * Created by Work on 30.05.2015.
 */import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper implements IDatabaseHandler
{
    private static final int DATABASE_VERSION = 1;
    //private static final String DATABASE_NAME = "contactsManager";
    private static final String DATABASE_NAME = "RSS_items_Manager";
    //private static final String TABLE_CONTACTS = "contacts";
    private static final String TABLE_ITEMS = "items";
    private static final String KEY_ID = "id";
    //private static final String KEY_NAME = "name";
    private static final String KEY_TITLE = "title";
    //private static final String KEY_PH_NO = "phone_number";
    private static final String KEY_DAY = "add_day";
    private static final String KEY_LINK = "link";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_DAY + " TEXT,"
                + KEY_TITLE + " TEXT,"
                + KEY_LINK + " TEXT)";
        db.execSQL(CREATE_ITEMS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS);
        onCreate(db);
    }

    @Override
    public void addRssItem(RssItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TITLE, item.getTitle());
        values.put(KEY_LINK, item.getLink());
        values.put(KEY_DAY, item.getDay());

        db.insert(TABLE_ITEMS, null, values);
        db.close();
    }

//    @Override
//    public Contact getContact(int id) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        Cursor cursor = db.query(TABLE_ITEMS, new String[] { KEY_ID,
//                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
//                new String[] { String.valueOf(id) }, null, null, null, null);
//
//        if (cursor != null){
//            cursor.moveToFirst();
//        }
//
//        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2));
//
//        return contact;
//    }

    @Override
    public ArrayList<RssItem> getAllItems()
    {
        ArrayList<RssItem> contactList = new ArrayList<RssItem>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                RssItem items = new RssItem();
                items.setDay(cursor.getString(1));
                items.setTitle(cursor.getString(2));
                items.setLink(cursor.getString(3));
                contactList.add(items);
            } while (cursor.moveToNext());
        }

        return contactList;
    }

    @Override
    public void updateItem(RssItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DAY, item.getDay());
        values.put(KEY_TITLE, item.getTitle());
        values.put(KEY_LINK, item.getLink());
}

    @Override
    public void deleteItem(RssItem item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, KEY_ID + " = ?", new String[] { String.valueOf(item.getLink())});
        db.close();
    }

    @Override
    public void deleteAll() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ITEMS, null, null);
        db.close();
    }

    @Override
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }
}