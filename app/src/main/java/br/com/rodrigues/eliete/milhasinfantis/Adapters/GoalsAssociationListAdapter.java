package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.AssociationActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Associates;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/19/15.
 */
public class GoalsAssociationListAdapter extends RecyclerView.Adapter<GoalsAssociationListAdapter.GoalsItemViewHolder> {

    private List<Goals> goalsList;
    private List<Associates> associatesList;
    private Context context;
    private AssociationActivity.OnItemClickListener.OnItemClickCallback onItemClickCallback;

    public GoalsAssociationListAdapter(List<Goals> goalsList, List<Associates> associatesList,
                                       AssociationActivity.OnItemClickListener.OnItemClickCallback onItemClickCallback){
        this.goalsList = goalsList;
        this.associatesList = associatesList;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public GoalsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_association, parent, false);
        return new GoalsItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(GoalsItemViewHolder holder, int position) {
        Goals goals = goalsList.get(position);

        if(goals != null && getItemCount() > 0){
            holder.goalsDescriptionTextView.setText(goals.getDescription());
            holder.greenPoint.setText(goals.getGreenPoint() + " " + context.getResources().getString(R.string.points));
            holder.yellowPoint.setText(goals.getYellowPoint() + " " + context.getResources().getString(R.string.points));
            holder.redPoint.setText(goals.getRedPoint() + " " + context.getResources().getString(R.string.points));
            if (consultAssociation(goals)){
                holder.checkBox.setChecked(true);
                holder.checkBox.setOnCheckedChangeListener(new AssociationActivity.OnItemClickListener(position, onItemClickCallback, true));
            }
            else {
                holder.checkBox.setChecked(false);
                holder.checkBox.setOnCheckedChangeListener(new AssociationActivity.OnItemClickListener(position, onItemClickCallback, false));
            }
        }

    }

    private boolean consultAssociation(Goals goals) {
        for (int i = 0; i <associatesList.size(); i++){
            Associates a = associatesList.get(i);
            if (goals.getId() == a.getIdActivity()){
                if (a.getStatus() == 1){
                    return true;
                }else{
                    return false;
                }
            }

        }
        return false;
    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }



    public class GoalsItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.description_goalsTextView)
        TextView goalsDescriptionTextView;
        @Bind(R.id.redPoints_TextView)
        TextView redPoint;
        @Bind(R.id.yellowPoints_TextView)
        TextView yellowPoint;
        @Bind(R.id.greenPoints_TextView)
        TextView greenPoint;
        @Bind(R.id.check)
        CheckBox checkBox;

        public GoalsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }



}
