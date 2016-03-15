package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import br.com.rodrigues.eliete.milhasinfantis.R;
import br.com.rodrigues.eliete.milhasinfantis.Model.Categories;
import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by eliete on 9/20/15.
 */
public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.CategoriesItemViewHolder> {

private List<Categories> categoriesList;

public CategoriesListAdapter(List<Categories> categoriesList){
        this.categoriesList = categoriesList;
        }

@Override
public CategoriesItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_item_categories, parent, false);
        return new CategoriesItemViewHolder(view);
        }

@Override
public void onBindViewHolder(CategoriesItemViewHolder holder, int position) {
        Categories categories = categoriesList.get(position);

        if(categories != null && getItemCount() > 0)
            holder.categoriesDescriptionTextView.setText(categories.getDescription());

}


@Override
public int getItemCount() {
        return categoriesList.size();
        }

public class CategoriesItemViewHolder extends RecyclerView.ViewHolder{

    @Bind(R.id.description_categoriesTextView)
    TextView categoriesDescriptionTextView;

    public CategoriesItemViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}
}
