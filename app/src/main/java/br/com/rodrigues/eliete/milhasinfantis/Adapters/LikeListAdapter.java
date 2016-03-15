package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Fragments.LikeFragment;
import br.com.rodrigues.eliete.milhasinfantis.Model.Goals;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 10/10/15.
 */
public class LikeListAdapter extends RecyclerView.Adapter<LikeListAdapter.LikeItemViewHolder> {

    private List<Goals> goalsList;
//    private int selectedItem;
//    private static int position;
    private LikeFragment.OnItemClickListener.OnItemClickCallback onItemClickCallback;

    public LikeListAdapter(List<Goals> goalsList, LikeFragment.OnItemClickListener.OnItemClickCallback onItemClickCallback) {
        this.goalsList = goalsList;
        this.onItemClickCallback = onItemClickCallback;
    }

    @Override
    public LikeItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_like, parent, false);

        LikeItemViewHolder likeItemViewHolder = new LikeItemViewHolder(view);
//                , new LikeItemViewHolder.VHClickListener() {
//            @Override
//            public void onHappySelected() {
//
//                setToggleSelection(1);
//            }
//
//            @Override
//            public void onSosoSelected() {
//
//                setToggleSelection(2);
//            }
//
//            @Override
//            public void onAngrySelected() {
//
//                setToggleSelection(3);
//            }
//        });

        return likeItemViewHolder;
    }


    @Override
    public void onBindViewHolder(LikeItemViewHolder holder, int position) {
        final Goals goals = goalsList.get(position);
        if (goals != null && getItemCount() > 0) {
            holder.goalsDescriptionTextView.setText(goals.getDescription());
            holder.happyButton.setOnClickListener(new LikeFragment.OnItemClickListener(position, onItemClickCallback));
            holder.sosoButton.setOnClickListener(new LikeFragment.OnItemClickListener(position, onItemClickCallback));
            holder.angryButton.setOnClickListener(new LikeFragment.OnItemClickListener(position, onItemClickCallback));

        }

    }

    @Override
    public int getItemCount() {
        return goalsList.size();
    }


    public static class LikeItemViewHolder extends RecyclerView.ViewHolder {
//        public static class LikeItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @Bind(R.id.description_goalsTextView)
        TextView goalsDescriptionTextView;
        @Bind(R.id.happy)
        ImageView happyButton;
        @Bind(R.id.soso)
        ImageView sosoButton;
        @Bind(R.id.angry)
        ImageView angryButton;
//        VHClickListener listener;


        public LikeItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);


        }

//        public LikeItemViewHolder(View itemView, VHClickListener listener) {
//            super(itemView);
//            this.listener = listener;
//            ButterKnife.bind(this, itemView);
//            happyButton.setOnClickListener(this);
//            sosoButton.setOnClickListener(this);
//            angryButton.setOnClickListener(this);
//
//        }


//            @Override
//        public void onClick(View v) {
//           if(listener != null){
//               position = getAdapterPosition();
//               switch(v.getId()){
//                   case R.id.happy:
//                       listener.onHappySelected();
//                       break;
//                   case R.id.soso:
//                       listener.onSosoSelected();
//                       break;
//                   case R.id.angry:
//                       listener.onAngrySelected();
//                       break;
//               }
//           }
//        }

//        public interface VHClickListener{
//            void onHappySelected();
//            void onSosoSelected();
//            void onAngrySelected();
//        }
    }

        //  method to access in activity after updating selection
    public List<Goals> getGoalsList() {
        return goalsList;
    }



}
