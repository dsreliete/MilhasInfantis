package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.LikeActivity;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import br.com.rodrigues.eliete.milhasinfantis.R;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/10/15.
 */
public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.LikeItemViewHolder> {

    private List<Goals> goalsList;
    private LikeActivity.OnItemClickListener.OnItemClickCallback onItemClickCallback;

    public LikeListAdapter(List<Goals> goalsList, LikeActivity.OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.goalsList = goalsList;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public LikeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_like, parent, false);

        LikeItemViewHolder likeItemViewHolder = new LikeItemViewHolder(view);
        return likeItemViewHolder;
    }


    @Override
    public void onBindViewHolder(LikeItemViewHolder holder, int position) {
        final Goals goals = goalsList.get(position);
        if (goals != null && getItemCount() > 0) {
            holder.goalsDescriptionTextView.setText(goals.getDescription());
            holder.happyButton.setOnClickListener(new LikeActivity.OnItemClickListener(position, onItemClickCallback));
            holder.sosoButton.setOnClickListener(new LikeActivity.OnItemClickListener(position, onItemClickCallback));
            holder.angryButton.setOnClickListener(new LikeActivity.OnItemClickListener(position, onItemClickCallback));

        }

    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }


    public static class LikeItemViewHolder extends RecyclerView.ViewHolder {
        @Bind(R.id.description_goalsTextView)
        TextView goalsDescriptionTextView;
        @Bind(R.id.happy)
        ImageView happyButton;
        @Bind(R.id.soso)
        ImageView sosoButton;
        @Bind(R.id.angry)
        ImageView angryButton;

        public LikeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

}
