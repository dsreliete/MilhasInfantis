package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;

/**
 * Created by eliete on 10/3/15.
 */
public class GoalsAssociationDAO {

    private Context context;

    public GoalsAssociationDAO(Context context){
        this.context = context;
    }

    public boolean inserir(Associates a){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaAssociacao.ASSOCIACAO_FILHO_ID, a.getIdChildren());
        values.put(DBContract.TabelaAssociacao.ASSOCIACAO_ATIVIDADE_ID, a.getIdActivity());
        values.put(DBContract.TabelaAssociacao.ASSOCIACAO_CAT_ID, a.getCatId());
        db.insert(DBContract.TabelaAssociacao.NOME_TABLE, null, values);
        db.close();
        return true;
    }

//    public List<Goals> consultarAssociatesListPerChild(int idChild){
//        List<Goals> lista = new ArrayList<>();
//        DBHelper helper = DBHelper.getInstance(context);
//        SQLiteDatabase db = helper.getReadableDatabase();
//
//        String query = "SELECT * FROM " + DBContract.TabelaAtividade.NOME_TABLE + ", " +
//                DBContract.TabelaAssociacao.NOME_TABLE + " WHERE " + DBContract.TabelaAtividade.ATIVIDADE_ID +
//                " = " + DBContract.TabelaAssociacao.ASSOCIACAO_ATIVIDADE_ID + " AND " +
//                DBContract.TabelaAssociacao.ASSOCIACAO_FILHO_ID + " = " + idChild;
//
//        Cursor c = db.rawQuery(query, null);
//
//        if(c != null)
//            c.moveToFirst();
//
//        if (c.getCount() == 0)
//            return lista;
//
//        Goals goals;
//        do{
//            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_ID));
//            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_DESC));
//            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERM));
//            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PAMA));
//            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERD));
//            int catId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_CAT_ID));
//            goals = new Goals(idd, desc, red, yellow, green, catId);
//            lista.add(goals);
//        }while (c.moveToNext());
//        c.close();
//        db.close();
//        return lista;
//    }

    public List<Goals> consultarAssociatesListPerCategoryPerChild(int idCategory, int idChild){
        List<Goals> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBContract.TabelaAtividade.NOME_TABLE + ", " +
                DBContract.TabelaAssociacao.NOME_TABLE + " WHERE " + DBContract.TabelaAtividade.ATIVIDADE_ID +
                " = " + DBContract.TabelaAssociacao.ASSOCIACAO_ATIVIDADE_ID + " AND " +
                DBContract.TabelaAssociacao.ASSOCIACAO_CAT_ID + " = " + idCategory + " AND " +
                DBContract.TabelaAssociacao.ASSOCIACAO_FILHO_ID + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Goals goals;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_ID));
            String desc = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_DESC));
            int red = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERM));
            int yellow = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PAMA));
            int green = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_PVERD));
            int catId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAtividade.ATIVIDADE_CAT_ID));
            goals = new Goals(idd, desc, red, yellow, green, catId);
            lista.add(goals);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public List<Associates> consultarAssociatesListPerCategory(int idCategory){
        List<Associates> lista = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaAssociacao.NOME_TABLE, DBContract.TabelaAssociacao.ASSOCIACAO_COLS,
                DBContract.TabelaAssociacao.ASSOCIACAO_CAT_ID + "=" + idCategory, null, null, null, null, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return lista;

        Associates a;
        do{
            int idChild = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAssociacao.ASSOCIACAO_FILHO_ID));
            int idActivity = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaAssociacao.ASSOCIACAO_ATIVIDADE_ID));
            a = new Associates(idChild, idActivity, idCategory);
            lista.add(a);
        }while (c.moveToNext());
        c.close();
        db.close();
        return lista;
    }

    public boolean obterCadastroAssociacao(){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.query(DBContract.TabelaAssociacao.NOME_TABLE, DBContract.TabelaAssociacao.ASSOCIACAO_COLS, null, null, null, null, null);

        if (c != null && c.getCount() > 0){
            return true;
        }else{
            return false;
        }
    }

     public boolean deletarId(Associates a) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        return db.delete(DBContract.TabelaAssociacao.NOME_TABLE,
                DBContract.TabelaAssociacao.ASSOCIACAO_FILHO_ID + "=" + a.getIdChildren() + " AND " +
                        DBContract.TabelaAssociacao.ASSOCIACAO_ATIVIDADE_ID + "=" + a.getIdActivity(), null) > 0;
    }
}
