package br.com.rodrigues.eliete.milhasinfantis.Dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.Model.Realizates;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBContract;
import br.com.rodrigues.eliete.milhasinfantis.Utils.DBHelper;
import br.com.rodrigues.eliete.milhasinfantis.Utils.Utils;

/**
 * Created by eliete on 10/26/15.
 */
public class RealizationDAO {

    private Context context;

    public RealizationDAO(Context context){
        this.context = context;
    }

    public boolean inserir(int filhoId, int acaoId, int acaoCatId, String tipoAçao, int ponto, String tipoPonto, String data, String hora){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID, filhoId); // id do filho
        values.put(DBContract.TabelaRealizacao.REALIZACAO_ACAO_ID, acaoId); // id de: atividade, categoria, penalizacao, associacao
        values.put(DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID, acaoCatId);//atributo nulo, somente para atividade
        values.put(DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO, tipoAçao); // cadastrar. editar, excluir, penalizar, associar. Qq ação do app
        values.put(DBContract.TabelaRealizacao.REALIZACAO_PONTO, ponto);// atributo nulo. Só para bonificar, penalizar ou premiar, correponde ao vcalor do ponto
        values.put(DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO, tipoPonto); // atributo nulo. Só para bonificar, penalizar ou premiar. O Tipo do ponto pode ser: verde, verm, amarelo ou azul
        values.put(DBContract.TabelaRealizacao.REALIZACAO_DATA, data);
        values.put(DBContract.TabelaRealizacao.REALIZACAO_HORA, hora);
        db.insert(DBContract.TabelaRealizacao.NOME_TABLE, null, values);
        db.close();
        return true;
    }

    public List<Realizates> consultarRealizacaoPerChild(int idChild){
        List<Realizates> list = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBContract.TabelaRealizacao.NOME_TABLE + ", " +
                DBContract.TabelaFilho.NOME_TABLE + " WHERE " + DBContract.TabelaFilho.FILHO_ID +
                " = " + DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " AND " +
                DBContract.TabelaFilho.FILHO_ID + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return list;

        Realizates realizates;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_ID));
            String filhoNome = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaFilho.FILHO_NOME));
            int ponto = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_PONTO));
            String tipoPonto = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO));
            int acaoId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_ACAO_ID));
            int catAcaoId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID));
            String tipoAcao = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO));
            String d = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_DATA));
            String data = Utils.formatDate(d);
            String hora =  c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_HORA));
            realizates = new Realizates(idd, filhoNome, ponto, tipoPonto, acaoId, catAcaoId, tipoAcao, data, hora);
            list.add(realizates);
        }while (c.moveToNext());
        c.close();
        db.close();
        return list;
    }

    public List<Realizates> consultarRealizacao(){
        List<Realizates> list = new ArrayList<>();
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT * FROM " + DBContract.TabelaRealizacao.NOME_TABLE + ", " +
                DBContract.TabelaFilho.NOME_TABLE + " WHERE " + DBContract.TabelaFilho.FILHO_ID +
                " = " + DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return list;

        Realizates realizates;
        do{
            int idd = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_ID));
            int idFilho = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID));
            String filhoNome = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaFilho.FILHO_NOME));
            int ponto = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_PONTO));
            String tipoPonto = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO));
            int acaoId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_ACAO_ID));
            int catAcaoId = c.getInt(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID));
            String tipoAcao = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO));
            String d = c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_DATA));
            String data = Utils.formatDate(d);
            String hora =  c.getString(c.getColumnIndexOrThrow(DBContract.TabelaRealizacao.REALIZACAO_HORA));
            realizates = new Realizates(idd, idFilho, filhoNome, ponto, tipoPonto, acaoId, catAcaoId, tipoAcao, data, hora);
            list.add(realizates);
        }while (c.moveToNext());
        c.close();
        db.close();
        return list;
    }

    public int consultarTotalPointsPerChild(int idChild){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT SUM (" + DBContract.TabelaRealizacao.REALIZACAO_PONTO + ") FROM " + DBContract.TabelaRealizacao.NOME_TABLE
                + " WHERE " + DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarTotalPointsPerChildToday(int idChild, String date){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT SUM (" + DBContract.TabelaRealizacao.REALIZACAO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " + DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID
                + " = " + idChild + " AND strftime('%Y-%m-%d', " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                ")=strftime('%Y-%m-%d','" + date + "')" ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarTotalPointsPerChildWeek(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT SUM (" + DBContract.TabelaRealizacao.REALIZACAO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE strftime('%Y-%m-%d', "
                + DBContract.TabelaRealizacao.REALIZACAO_DATA + ")>=strftime('%Y-%m-%d','" +
                iniDate + "') AND strftime('%Y-%m-%d', " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                ")<=strftime('%Y-%m-%d','" + endDate + "') AND " + DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID
                + " = " + idChild;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarTotalRedActions(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'vermelho' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild
                  + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'"
                ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarTotalYellowActions(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'amarelo' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'"
                ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarTotalGreenActions(int idChild, String iniDate, String endDate){
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'verde' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }


    public int consultarBonificationRedActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'vermelho' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);

    }

    public int consultarBonificationYellowActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'amarelo' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar'"
                + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'"
                ;

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarBonificationGreenActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'verde' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar'"
                 + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarExtraPointsRedActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'vermelho' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'ponto_extra'"
                + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarExtraPointsGreenActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE "
                + DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'verde' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'ponto_extra'" +
                " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarPenActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'penalizar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild +
                " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int consultarBonActions(int idChild, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild +
                " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int CatRedActions(int idChild, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'vermelho' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID + " = " + catId +
                " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int CatYellowActions(int idChild, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'amarelo' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID + " = " + catId + " AND "
                + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int CatGreenActions(int idChild, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'verde' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID + " = " + catId
                + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int GoalRedActions(int idChild, int idGoal, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'vermelho' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID + " = " + catId + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_ID + " = " + idGoal
                + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int GoalYellowActions(int idChild, int idGoal, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'amarelo' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID + " = " + catId + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_ID + " = " + idGoal +
                " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";
        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }

    public int GoalGreenActions(int idChild, int idGoal, int catId, String iniDate, String endDate) {
        DBHelper helper = DBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String query = "SELECT COUNT (" + DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + ") FROM " +
                DBContract.TabelaRealizacao.NOME_TABLE + " WHERE " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_ACAO + " = 'bonificar' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_TIPO_PONTO + " = 'verde' AND " +
                DBContract.TabelaRealizacao.REALIZACAO_FILHO_ID + " = " + idChild + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_CAT_ID + " = " + catId + " AND " +
                DBContract.TabelaRealizacao.REALIZACAO_ACAO_ID + " = " + idGoal
                + " AND " + DBContract.TabelaRealizacao.REALIZACAO_DATA +
                " BETWEEN '" + iniDate + "' AND '" + endDate + "'";

        Cursor c = db.rawQuery(query, null);

        if(c != null)
            c.moveToFirst();

        if (c.getCount() == 0)
            return 0;

        return c.getInt(0);
    }
}
