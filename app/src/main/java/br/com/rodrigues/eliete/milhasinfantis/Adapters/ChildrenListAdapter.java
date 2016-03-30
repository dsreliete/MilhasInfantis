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
import br.com.rodrigues.eliete.milhasinfantis.Model.Children;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/9/15.
 */
public class ChildrenListAdapter extends RecyclerView.Adapter<ChildrenListAdapter.ChildItemViewHolder> {
    //Every adapter has three primary methods: onCreateViewHolder to inflate the item layout and create
    // the holder, onBindViewHolder to set the view attributes based on the data and
    // getItemCount to determine the number of items.

    private List<Children> childrenList;

    public ChildrenListAdapter(List<Children> list){
        childrenList = list;
    }

    @Override
    public ChildItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_child, parent, false);
        return new ChildItemViewHolder(view);

    }


    @Override
    public void onBindViewHolder(ChildItemViewHolder holder, int position) {
        Children children = childrenList.get(position);

        if(children != null) {
            holder.nameChildTextView.setText(children.getName());
            holder.pointsChildTextView.setText(children.getBirthDate());
            if (children.getGender().equals("F")) {
                holder.childImageView.setImageResource(R.drawable.maria);
            } else if (children.getGender().equals("M")) {
                holder.childImageView.setImageResource(R.drawable.joao);
            } else {
                holder.childImageView.setImageResource(R.drawable.joao);
            }
        }
    }

    @Override
    public int getItemCount() {
        return childrenList.size();
    }

    public class ChildItemViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.name_childTextView)
        TextView nameChildTextView;
        @Bind(R.id.points_childTextView)
        TextView pointsChildTextView;
        @Bind(R.id.child_image)
        ImageView childImageView;

        public ChildItemViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}
