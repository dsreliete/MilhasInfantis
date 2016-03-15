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
    private static final int DATABASE_VERSION = 15;

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
        db.execSQL(DBContract.TabelaPai.CREATE_TABLE);
        db.execSQL(DBContract.TabelaFilho.CREATE_TABLE);
        db.execSQL(DBContract.TabelaPremio.CREATE_TABLE);
        db.execSQL(DBContract.TabelaCategoria.CREATE_TABLE);
        db.execSQL(DBContract.TabelaAtividade.CREATE_TABLE);
        db.execSQL(DBContract.TabelaPenalidade.CREATE_TABLE);
        db.execSQL(DBContract.TabelaAssociacao.CREATE_TABLE);
        db.execSQL(DBContract.TabelaRealizacao.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaFilho.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaPai.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaAtividade.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaCategoria.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaPenalidade.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaPremio.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaAssociacao.NOME_TABLE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + DBContract.TabelaRealizacao.NOME_TABLE + ";");
        onCreate(db);

    }
}
