package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/19/15.
 */
public class GoalsListAdapter extends RecyclerView.Adapter<GoalsListAdapter.GoalsItemViewHolder> {

    private List<Goals> goalsList;
    private Context context;
    private SparseBooleanArray selectedItems;

    public GoalsListAdapter(List<Goals> goalsList){
        this.goalsList = goalsList;
        selectedItems = new SparseBooleanArray();
    }


    @Override
    public GoalsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_goals, parent, false);
        return new GoalsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GoalsItemViewHolder holder, int position) {
        Goals goals = goalsList.get(position);

        if(goals != null && getItemCount() > 0){
            holder.goalsDescriptionTextView.setText(goals.getDescription());
            holder.itemView.setActivated(selectedItems.get(position, false));
            int green = goals.getGreenPoint();
            int yellow = goals.getYellowPoint();
            int red = goals.getRedPoint();

            if (green == 1){
                holder.greenPoint.setText(green + " " + context.getResources().getString(R.string.point));
            }else{
                holder.greenPoint.setText(green + " " + context.getResources().getString(R.string.points));
            }

            if (yellow == 1){
                holder.yellowPoint.setText(yellow + " " + context.getResources().getString(R.string.point));
            }else{
                holder.yellowPoint.setText(yellow + " " + context.getResources().getString(R.string.points));

            }

            if(red == 1){
                holder.redPoint.setText(red + " " + context.getResources().getString(R.string.point));
            }else{
                holder.redPoint.setText(goals.getRedPoint() + " " + context.getResources().getString(R.string.points));
            }

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

    public List<Goals> getGoalsList() {
        return goalsList;
    }

    public void toggleSelection(int pos) {
        if (selectedItems.get(pos, false)) {
            selectedItems.delete(pos);
        }
        else {
            selectedItems.put(pos, true);
        }
        notifyItemChanged(pos);
    }

    public void clearSelections() {
        selectedItems.clear();
        notifyDataSetChanged();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<Integer>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }

    public void removeItem(int position) {
        goalsList.remove(position);
        notifyItemRemoved(position);
    }


}
