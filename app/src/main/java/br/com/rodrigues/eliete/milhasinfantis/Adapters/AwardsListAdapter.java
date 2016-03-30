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
import br.com.rodrigues.eliete.milhasinfantis.Model.Awards;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/19/15.
 */
public class AwardsListAdapter extends RecyclerView.Adapter<AwardsListAdapter.AwardsItemViewHolder> {

    private List<Awards> awardsList;
    private Context context;
    private SparseBooleanArray selectedItems;


    public AwardsListAdapter(List<Awards> awardsList){
        this.awardsList = awardsList;
        selectedItems = new SparseBooleanArray();
    }

    @Override
    public AwardsItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_awards, parent, false);
        return new AwardsItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AwardsItemViewHolder holder, int position) {
        Awards awards = awardsList.get(position);

        if(awards != null){
            holder.awdsDescriptionTextView.setText(awards.getDescription());
            holder.pointsAwdsTextView.setText(context.getResources().getString(R.string.rescue_point_textview) +
                    " " + awards.getPoints() + " " + context.getResources().getString(R.string.points));
            holder.itemView.setActivated(selectedItems.get(position, false));
        }
    }

    @Override
    public int getItemCount() {
        return awardsList.size();
    }

    public class AwardsItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.description_awardsTextView)
        TextView awdsDescriptionTextView;
        @Bind(R.id.points_awardsTextView)
        TextView pointsAwdsTextView;

        public AwardsItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
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
        awardsList.remove(position);
        notifyItemRemoved(position);
    }
}
