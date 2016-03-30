package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Model.Penalties;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/21/15.
 */
public class DislikeListAdapter extends RecyclerView.Adapter<DislikeListAdapter.UnlikeItemViewHolder> {

    private List<Penalties> penaltiesList;

    public DislikeListAdapter(List<Penalties> penList) {
        this.penaltiesList = penList;
    }

    @Override
    public DislikeListAdapter.UnlikeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_dislike, parent, false);
        return new UnlikeItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(DislikeListAdapter.UnlikeItemViewHolder holder, int position) {
        Penalties penalties = penaltiesList.get(position);

        if(penalties != null) {
            holder.descriptionTextView.setText(penalties.getDescription());
        }
    }

    @Override
    public int getItemCount() {
        return penaltiesList.size();
    }


    public static class UnlikeItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.description_penaltyTextView)
        TextView descriptionTextView;

        public UnlikeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

    }

}
