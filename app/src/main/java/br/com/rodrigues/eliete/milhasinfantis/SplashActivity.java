package br.com.rodrigues.eliete.milhasinfantis;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import br.com.rodrigues.eliete.milhasinfantis.Dao.AwardsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.CategoriesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.GoalsDAO;
import br.com.rodrigues.eliete.milhasinfantis.Dao.PenaltiesDAO;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;

/**
 * Created by eliete on 20/07/15.
 */
public class SplashActivity extends Activity {

    private boolean hasGoalsRegistered;
    private boolean hasCategoriesRegistered;
    private boolean hasPenaltiesRegistered;
    private boolean hasAwdsRegistered;
    private AwardsDAO awardsDAO;
    private CategoriesDAO categoriesDAO;
    private GoalsDAO goalsDAO;
    private Awards awards;
    private Categories categories;
    private Goals goals;
    private Penalties penalties;
    private PenaltiesDAO penaltiesDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        categoriesDAO = new CategoriesDAO(this);
        goalsDAO = new GoalsDAO(this);
        penaltiesDAO = new PenaltiesDAO(this);
        awardsDAO = new AwardsDAO(this);

        hasAwdsRegistered = awardsDAO.obtainAwards();
        if(!hasAwdsRegistered){
            awards = new Awards(getResources().getString(R.string.awards_desc1), 50);
            awardsDAO.insertAwards(awards);
            awards = new Awards(getResources().getString(R.string.awards_desc2), 30);
            awardsDAO.insertAwards(awards);
            awards = new Awards(getResources().getString(R.string.awards_desc3), 70);
            awardsDAO.insertAwards(awards);
            awards = new Awards(getResources().getString(R.string.awards_desc4), 200);
            awardsDAO.insertAwards(awards);
//            awards = new Awards(getResources().getString(R.string.awards_desc5), 150);
//            awardsDAO.insertChild(awards);
        }


        hasPenaltiesRegistered = penaltiesDAO.obtainPenalty();
        if(!hasPenaltiesRegistered){
            penalties = new Penalties(getResources().getString(R.string.penalties_desc1), -30);
            penaltiesDAO.insertPenalty(penalties);
            penalties = new Penalties(getResources().getString(R.string.penalties_desc2), -50);
            penaltiesDAO.insertPenalty(penalties);
            penalties = new Penalties(getResources().getString(R.string.penalties_desc3), -100);
            penaltiesDAO.insertPenalty(penalties);
        }

        hasCategoriesRegistered = categoriesDAO.obtainCategory();
        if(!hasCategoriesRegistered){
            categories = new Categories(getResources().getString(R.string.category_desc1));
            categoriesDAO.insertCategory(categories);
            categories = new Categories(getResources().getString(R.string.category_desc2));
            categoriesDAO.insertCategory(categories);
            categories = new Categories(getResources().getString(R.string.category_desc3));
            categoriesDAO.insertCategory(categories);
//            categories = new Categories(getResources().getString(R.string.category_desc4));
//            categoriesDAO.insertChild(categories);
        }

        hasGoalsRegistered = goalsDAO.obtainGoal();
        if(!hasGoalsRegistered){
            goals = new Goals(getResources().getString(R.string.goals_desc1), 0 ,  5 ,  35 , 1);
            goalsDAO.insertGoals(goals);
            goals = new Goals(getResources().getString(R.string.goals_desc2), 0 ,  5 ,  35 , 1);
            goalsDAO.insertGoals(goals);
            goals = new Goals(getResources().getString(R.string.goals_desc3), 0 ,  5 ,  20 , 1);
            goalsDAO.insertGoals(goals);

            goals = new Goals(getResources().getString(R.string.goals_desc4),  0 ,  5 ,  40 , 2);
            goalsDAO.insertGoals(goals);
            goals = new Goals(getResources().getString(R.string.goals_desc5),  0 ,  5 ,  50 , 2);
            goalsDAO.insertGoals(goals);
            goals = new Goals(getResources().getString(R.string.goals_desc6),  0 ,  5 ,  50 , 2);
            goalsDAO.insertGoals(goals);

            goals = new Goals(getResources().getString(R.string.goals_desc7),  0 ,  5 ,  50 , 3);
            goalsDAO.insertGoals(goals);
            goals = new Goals(getResources().getString(R.string.goals_desc8),  0 ,  5 ,  50 , 3);
            goalsDAO.insertGoals(goals);
            goals = new Goals(getResources().getString(R.string.goals_desc9),  0 ,  5 ,  100 , 3);
            goalsDAO.insertGoals(goals);

//            goals = new Goals(getResources().getString(R.string.goals_desc10),  0 ,  5 ,  50 , 4);
//            goalsDAO.insertChild(goals);
//            goals = new Goals(getResources().getString(R.string.goals_desc11),  0 ,  5 ,  25 , 4);
//            goalsDAO.insertChild(goals);
//            goals = new Goals(getResources().getString(R.string.goals_desc12),  0 ,  5 ,  35 , 4);
//            goalsDAO.insertChild(goals);

        }

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                finish();
                startActivity(i);
            }
        }, 1000);
    }

}
