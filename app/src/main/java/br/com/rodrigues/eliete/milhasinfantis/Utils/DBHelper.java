package br.com.rodrigues.eliete.milhasinfantis.Utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by eliete on 8/16/15.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static DBHelper sInstance;
    private static final String DATABASE_NAME = "milhasInfantisDatabase";
    private static final int DATABASE_VERSION = 17;

    public static synchronized DBHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBContract.ParentsTable.CREATE_TABLE);
        db.execSQL(DBContract.ChildTable.CREATE_TABLE);
        db.execSQL(DBContract.AwardsTable.CREATE_TABLE);
        db.execSQL(DBContract.CategoryTable.CREATE_TABLE);
        db.execSQL(DBContract.ActivityTable.CREATE_TABLE);
        db.execSQL(DBContract.PenaltyTable.CREATE_TABLE);
        db.execSQL(DBContract.AssociationTable.CREATE_TABLE);
        db.execSQL(DBContract.RealizationTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ChildTable.NAME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ParentsTable.NAME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.ActivityTable.NAME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.CategoryTable.NAME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.PenaltyTable.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.AwardsTable.NAME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.AssociationTable.NAME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.RealizationTable.NAME_TABLE + ";");
        onCreate(db);

    }
}
