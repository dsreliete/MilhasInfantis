package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Dao.RealizationDAO;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.ChildrenRescuePointsFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/30/15.
 */
public class RescueAdapter extends RecyclerView.Adapter<RescueAdapter.RescueItemViewHolder> {

    private List<Awards> awardsList;
    private ChildrenRescuePointsFragment.OnItemClickListener.OnItemClickCallback onItemClickCallback;
    private Context context;
    private int idChildren;
    private boolean rescue;

    public RescueAdapter(Context c, List<Awards> list, ChildrenRescuePointsFragment.OnItemClickListener.OnItemClickCallback onItemClickCallback, int id){
        this.context = c;
        this.awardsList = list;
        this.onItemClickCallback = onItemClickCallback;
        this.idChildren = id;
    }

    @Override
    public RescueAdapter.RescueItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_rescue, parent, false);
        return new RescueItemViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RescueAdapter.RescueItemViewHolder holder, int position) {
        Awards awards = awardsList.get(position);

        int childrenPoint = getChildrenTotalPoints();

        int pointsCalculated = getPointsAvailable(awards.getPoints(), childrenPoint);


        if (awards != null){
            holder.descriptionAwards.setText(awards.getDescription());
            if (rescue){
                holder.pointsAwards.setText(pointsCalculated + " pontos");
                holder.rescueButton.setOnClickListener(new ChildrenRescuePointsFragment.OnItemClickListener(position, onItemClickCallback, childrenPoint, awards.getPoints(), awards.getId()));
            }else{
                if(pointsCalculated == 1){
                    holder.pointsAwards.setText("falta " + pointsCalculated + " ponto para resgate");
                }else{
                    holder.pointsAwards.setText("faltam " + pointsCalculated + " pontos para resgate");
                }
                holder.rescueButton.setBackgroundResource(R.drawable.bg_grey);
            }

        }
    }

    private int getPointsAvailable(int awardsPoint, int childrenPoint) {
        if(childrenPoint >= awardsPoint){
            rescue = true;
            return awardsPoint;
        }else{
            rescue = false;
            return awardsPoint - childrenPoint;
        }
    }

    @Override
    public int getItemCount() {
        return awardsList.size();
    }

    public int getChildrenTotalPoints(){
        RealizationDAO realizationDAO = new RealizationDAO(context);
        return realizationDAO.consultarTotalPointsPerChild(idChildren);
    }


    public class RescueItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.description_awardsTextView)
        TextView descriptionAwards;
        @Bind(R.id.points_awardsTextView)
        TextView pointsAwards;
        @Bind(R.id.rescue_button)
        Button rescueButton;

        public RescueItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
