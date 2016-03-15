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
public class UnlikeListAdapter extends RecyclerView.Adapter<UnlikeListAdapter.UnlikeItemViewHolder> {

    private List<Penalties> penaltiesList;

    public UnlikeListAdapter(List<Penalties> penList) {
        this.penaltiesList = penList;
    }

    @Override
    public UnlikeListAdapter.UnlikeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_unlike, parent, false);
        return new UnlikeItemViewHolder(view);
    }



    @Override
    public void onBindViewHolder(UnlikeListAdapter.UnlikeItemViewHolder holder, int position) {
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
