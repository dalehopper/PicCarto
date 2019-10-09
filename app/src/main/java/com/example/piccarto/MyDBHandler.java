package com.example.piccarto;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDBHandler extends SQLiteOpenHelper {
/*    private int overlayID;
    private byte[] imageByteArray;
    private float anchorX, anchorY, height, width, bearing;
    private double positionA, position0;*/



    //information of database
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "overlayDB.db";
    public static final String TABLE_NAME = "Overlay";
    public static final String COLUMN_ID = "OverlayID";
    public static final String COLUMN_IMAGEBYTEARRAY = "ImageByteArray";
    public static final String COLUMN_ANCHORX = "AnchorX";
    public static final String COLUMN_ANCHORY = "AnchorY";
    public static final String COLUMN_HEIGHT = "Height";
    public static final String COLUMN_WIDTH = "Width";
    public static final String COLUMN_BEARING = "Bearing";
    public static final String COLUMN_POSITIONA = "PositionA";
    public static final String COLUMN_POSITIONO = "PositionO";
    public static final String COLUMN_OWNER = "Owner";
    // initialize the database

    public MyDBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_OVERLAY_TABLE = "CREATE TABLE " +
                TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_IMAGEBYTEARRAY
                + " BLOB," + COLUMN_ANCHORX + " REAL," + COLUMN_ANCHORY +" REAL," + COLUMN_HEIGHT + " REAL," + COLUMN_WIDTH
                +" REAL,"+ COLUMN_BEARING + " REAL," + COLUMN_POSITIONA + "REAL," + COLUMN_POSITIONO + " REAL," + COLUMN_OWNER +" TEXT)";
        db.execSQL(CREATE_OVERLAY_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }
    public void addOverlay(Overlay overlay){
        ContentValues values = new ContentValues();
        values.put(COLUMN_IMAGEBYTEARRAY, overlay.getImageByteArray());
        values.put(COLUMN_ANCHORX, overlay.getAnchorX());
        values.put(COLUMN_ANCHORY, overlay.getAnchorY());
        values.put(COLUMN_HEIGHT, overlay.getHeight());
        values.put(COLUMN_WIDTH, overlay.getWidth());
        values.put(COLUMN_BEARING, overlay.getBearing());
        values.put(COLUMN_POSITIONA, overlay.getPositionA());
        values.put(COLUMN_POSITIONO, overlay.getPosition0());
        values.put(COLUMN_OWNER, overlay.getOwner());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_NAME, null, values);
        db.close();
    }
    public Overlay findOverlay(int overlayID){
        String query = "Select * FROM " + TABLE_NAME + " WHERE " +COLUMN_ID + " = " +"'" + overlayID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Overlay overlay = new Overlay();
        if (cursor.moveToFirst()){
            cursor.moveToFirst();
            overlay.setOverlayID(Integer.parseInt(cursor.getString(0)));
            overlay.setImageByteArray(cursor.getBlob(1));
            overlay.setAnchorX(cursor.getFloat(2));
            overlay.setAnchorY(cursor.getFloat(3));
            overlay.setHeight(cursor.getFloat(4));
            overlay.setWidth(cursor.getFloat(4));
            overlay.setBearing(cursor.getFloat(5));
            overlay.setPositionA(cursor.getFloat(6));
            overlay.setPosition0(cursor.getFloat(7));
            overlay.setOwner(cursor.getString(8));
        }else {
            overlay = null;
        }
        db.close();
        return overlay;
    }

    public boolean deleteOverlay(int ID) {
        boolean result = false;
        String query = "Select*FROM" + TABLE_NAME + "WHERE" + COLUMN_ID + "= '" + ID + "'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Overlay overlay = new Overlay();
        if (cursor.moveToFirst()) {
            overlay.setOverlayID(Integer.parseInt(cursor.getString(0)));
            db.delete(TABLE_NAME, COLUMN_ID + "=?",
                    new String[] {
                String.valueOf(overlay.getOverlayID())
            });
            cursor.close();
            result = true;
        }
        db.close();
        return result;
    }
}