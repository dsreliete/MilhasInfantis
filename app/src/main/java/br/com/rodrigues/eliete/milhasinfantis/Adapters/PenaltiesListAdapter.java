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
 * Created by eliete on 9/19/15.
 */
public class PenaltiesListAdapter extends RecyclerView.Adapter<PenaltiesListAdapter.PenaltiesItemViewHolder> {

    private List<Penalties> penaltiesList;
    private Context context;

    public PenaltiesListAdapter(List<Penalties> penaltiesList){
        this.penaltiesList = penaltiesList;
    }

    @Override
    public PenaltiesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_penalty, parent, false);
        return new PenaltiesItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PenaltiesItemViewHolder holder, int position) {
        Penalties penalties = penaltiesList.get(position);

        if(penalties != null){
            holder.descriptionTextView.setText(penalties.getDescription());
            holder.pointTextView.setText(penalties.getPoint() + " " + context.getResources().getString(R.string.points));
        }
    }

    @Override
    public int getItemCount() {
        return penaltiesList.size();
    }

    public class PenaltiesItemViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.points_penaltyTextView) TextView pointTextView;
        @Bind(R.id.description_penaltyTextView) TextView descriptionTextView;

        public PenaltiesItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
