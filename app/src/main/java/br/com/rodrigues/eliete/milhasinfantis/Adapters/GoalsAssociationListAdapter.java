package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/19/15.
 */
public class GoalsAssociationListAdapter extends RecyclerView.Adapter<GoalsAssociationListAdapter.GoalsItemViewHolder> {

    private List<Goals> goalsList;
    private Context context;
    public static final String TAG = "MilhasApplication";

    public GoalsAssociationListAdapter(List<Goals> goalsList){
        this.goalsList = goalsList;
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
        }

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

        public GoalsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

//  method to access in activity after updating selection
    public List<Goals> getGoalsList() {
        return goalsList;
    }


}
