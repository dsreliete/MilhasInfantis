package br.com.rodrigues.eliete.milhasinfantis.Utils;

import android.provider.BaseColumns;

/**
 * Created by eliete on 8/16/15.
 */
public class DBContract {

    public static class TabelaPai implements BaseColumns{
        public static final String NOME_TABLE = "pai";
        public static final String PAI_ID = "_id";
        public static final String PAI_NOME = "nome";
        public static final String PAI_FAMILIA = "familia";
        public static final String PAI_COLS [] = {PAI_ID, PAI_NOME, PAI_FAMILIA};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" + PAI_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PAI_NOME + " VARCHAR(30) NOT NULL, "+ PAI_FAMILIA +
                " VARCHAR(30) NOT NULL)";

    }

    public static class TabelaFilho implements BaseColumns{
        public static final String NOME_TABLE = "filho";
        public static final String FILHO_ID = "_id";
        public static final String FILHO_NOME = "nome";
        public static final String FILHO_DT_NASC = "dtNasc";
        public static final String FILHO_SEXO = "sexo";
        public static final String FILHO_COLS [] = {FILHO_ID, FILHO_NOME, FILHO_DT_NASC, FILHO_SEXO};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                FILHO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FILHO_NOME + " VARCHAR(30) NOT NULL, "+ FILHO_DT_NASC +
                " DATE NOT NULL," + FILHO_SEXO + " CHARACTER(1))";
    }

    public static class TabelaPremio implements BaseColumns{
        public static final String NOME_TABLE = "premio";
        public static final String PREMIO_ID = "_id";
        public static final String PREMIO_DESC = "desc";
        public static final String PREMIO_PONTO = "ponto";
        public static final String PREMIO_COLS [] = {PREMIO_ID, PREMIO_DESC, PREMIO_PONTO};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                PREMIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PREMIO_DESC + " VARCHAR(30) NOT NULL, "+ PREMIO_PONTO + " INTEGER NOT NULL)";
    }

    public static class TabelaCategoria implements BaseColumns{
        public static final String NOME_TABLE = "categoria";
        public static final String CATEGORIA_ID = "_id";
        public static final String CATEGORIA_DESC = "desc";
        public static final String CATEGORIA_COLS [] = {CATEGORIA_ID, CATEGORIA_DESC};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                CATEGORIA_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORIA_DESC + " VARCHAR(30) NOT NULL)";
    }

    public static class TabelaAtividade implements BaseColumns{
        public static final String NOME_TABLE = "atividade";
        public static final String ATIVIDADE_ID = "_id";
        public static final String ATIVIDADE_DESC = "desc";
        public static final String ATIVIDADE_PVERM = "ponto_vermelho";
        public static final String ATIVIDADE_PVERD = "ponto_verde";
        public static final String ATIVIDADE_PAMA = "ponto_amarelo";
             public static final String ATIVIDADE_CAT_ID = "atividade_categoria_id";

        public static final String ATIVIDADE_COLS [] = {ATIVIDADE_ID, ATIVIDADE_DESC,
                ATIVIDADE_PVERM, ATIVIDADE_PAMA, ATIVIDADE_PVERD, ATIVIDADE_CAT_ID};

        public static final String CREATE_TABLE = " CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                ATIVIDADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ATIVIDADE_DESC + " VARCHAR(30) NOT NULL, " +
                ATIVIDADE_PVERM + " INTEGER NOT NULL, " +
                ATIVIDADE_PAMA + " INTEGER NOT NULL, " +
                ATIVIDADE_PVERD + " INTEGER NOT NULL, " +
                ATIVIDADE_CAT_ID + " INTEGER, FOREIGN KEY(" + ATIVIDADE_CAT_ID + ") REFERENCES " +
                TabelaCategoria.NOME_TABLE + "(" + TabelaCategoria.CATEGORIA_ID + "))";
    }

    public static class TabelaPenalidade implements BaseColumns{
        public static final String NOME_TABLE = "penalidade";
        public static final String PENALIDADE_ID = "_id";
        public static final String PENALIDADE_DESC = "desc";
        public static final String PENALIDADE_PONTO = "ponto";
        public static final String PENALIDADE_COLS [] = {PENALIDADE_ID, PENALIDADE_DESC,
                PENALIDADE_PONTO};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                PENALIDADE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                PENALIDADE_DESC + " VARCHAR(30) NOT NULL, " + PENALIDADE_PONTO +
                " INTEGER NOT NULL)";

    }

    public static class TabelaAssociacao implements BaseColumns{
        public static final String NOME_TABLE = "associacao";
        public static final String ASSOCIACAO_FILHO_ID = "assoc_filho_id";
        public static final String ASSOCIACAO_ATIVIDADE_ID = "assoc_ativ_id";
        public static final String ASSOCIACAO_CAT_ID = "assoc_cat_id";
        public static final String ASSOCIACAO_COLS [] = {ASSOCIACAO_FILHO_ID,
                ASSOCIACAO_ATIVIDADE_ID, ASSOCIACAO_CAT_ID};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                ASSOCIACAO_FILHO_ID + " INTEGER NOT NULL, " +
                ASSOCIACAO_ATIVIDADE_ID + " INTEGER NOT NULL, " +
                ASSOCIACAO_CAT_ID + " INTEGER NOT NULL, " +
                "FOREIGN KEY(" + ASSOCIACAO_FILHO_ID + ") REFERENCES " +
                TabelaFilho.NOME_TABLE + "(" + TabelaFilho.FILHO_ID + "), " +
                "FOREIGN KEY(" + ASSOCIACAO_ATIVIDADE_ID + ") REFERENCES " +
                TabelaAtividade.NOME_TABLE + "(" + TabelaAtividade.ATIVIDADE_ID + "),"  +
                "FOREIGN KEY(" + ASSOCIACAO_CAT_ID + ") REFERENCES " +
                TabelaCategoria.NOME_TABLE + "(" + TabelaCategoria.CATEGORIA_ID + ")," +
                "PRIMARY KEY(" + ASSOCIACAO_FILHO_ID + ", " +  ASSOCIACAO_ATIVIDADE_ID + "))";


    }

    public static class TabelaRealizacao implements BaseColumns{
        public static final String NOME_TABLE = "realizacao";
        public static final String REALIZACAO_ID = "realizacao_id";
        public static final String REALIZACAO_FILHO_ID = "realizacao_filho_id"; //id filho
        public static final String REALIZACAO_ACAO_ID = "realizacao_acao_id"; //id de atividade, penalidade, premio, categoria, filho, bonificaça etc
        public static final String REALIZACAO_ACAO_CAT_ID = "realizacao_acao_cat_id"; //atributo nulo, somente para atividade
        public static final String REALIZACAO_TIPO_ACAO = "realizacao_acao_tipo"; // cadastrar. editar, excluir, penalizar, associar. Qq ação do app
        public static final String REALIZACAO_PONTO = "realizacao_ponto"; // valor do ponto
        public static final String REALIZACAO_TIPO_PONTO = "realizacao_ponto_tipo"; // atributo nulo. Só para bonificar, penalizar ou premiar. O Tipo do ponto pode ser: verde, verm, amarelo ou azul
        public static final String REALIZACAO_DATA = "realizacao_data";
        public static final String REALIZACAO_HORA = "realizacao_hora";
        public static final String REALIZACAO_COLS [] = {REALIZACAO_ID, REALIZACAO_FILHO_ID,
                REALIZACAO_ACAO_ID, REALIZACAO_ACAO_CAT_ID, REALIZACAO_TIPO_ACAO, REALIZACAO_PONTO, REALIZACAO_TIPO_PONTO,
                REALIZACAO_DATA, REALIZACAO_HORA};
        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + NOME_TABLE + " (" +
                REALIZACAO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + REALIZACAO_ACAO_ID +
                " INTEGER NOT NULL, " + REALIZACAO_ACAO_CAT_ID + " INTEGER, " + REALIZACAO_TIPO_ACAO + " VARCHAR(15) NOT NULL, " +
                REALIZACAO_FILHO_ID + " INTEGER NOT NULL, " + REALIZACAO_PONTO + " REAL, "
                + REALIZACAO_TIPO_PONTO + " VARCHAR(15), " + REALIZACAO_DATA + " TEXT NOT NULL, " +
                REALIZACAO_HORA + " VARCHAR(4))";

    }

}
