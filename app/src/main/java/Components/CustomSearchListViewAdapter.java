package Components;


import android.app.Activity;
import android.content.Context;
import android.graphics.Movie;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.R;

public class CustomSearchListViewAdapter extends BaseAdapter {
    private ActivityMain activity;
    private LayoutInflater inflater;
    private List<String> movieItems;
    private ListView lvList;
    Context context;

    public CustomSearchListViewAdapter(ActivityMain activity, ListView lst, List<String> Items) {
        this.activity = activity;
        this.movieItems = Items;
        lvList = lst;
        context = activity;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        return movieItems.size();
    }

    @Override
    public Object getItem(int location)
    {
        return movieItems.get(location);

    }

    @Override
    public long getItemId(int position)
    {
        return position;
    }

    public class Holder
    {
        View vLine;
        ImageView img;
        TextViewFont tv;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        Holder holder = new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.lst_search_layout, null);

        holder.img = (ImageView) rowView.findViewById(R.id.img_custom_search_list);
        holder.tv = (TextViewFont) rowView.findViewById(R.id.txt_custom_search_list);
        holder.tv.setText(movieItems.get(position));

        return rowView;
    }
}