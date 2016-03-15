package br.com.rodrigues.eliete.milhasinfantis.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import br.com.rodrigues.eliete.milhasinfantis.R;

/**
 * Created by eliete on 9/23/15.
 */
public class SpinnerStringAdapter extends ArrayAdapter<Object> {
    public SpinnerStringAdapter(Context context, Object[] objects) {
        super(context, android.R.layout.simple_spinner_item, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView ==null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_item, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.spinner_text);
            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }

        String text = String.valueOf(getItem(position));

        if(getItem(position) instanceof Integer && ((int)getItem(position)) == -1){
            text = "";
        }

        holder.textView.setText(text);

        return convertView;
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.spinner_dropdown_item, parent, false);
            holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.spinner_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String text = String.valueOf(getItem(position));

        if(getItem(position) instanceof Integer && ((int)getItem(position)) == -1){
            text = "";
        }

        holder.textView.setText(text);

        return convertView;
    }

    @Override
    public Object getItem(int position) {
        return super.getItem(position);
    }

    class ViewHolder{
        TextView textView;
    }
}
