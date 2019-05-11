package Components;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ir.iro.passenger.ActivityMain;
import ir.iro.passenger.R;

/**
 * Created by N550J on 5/28/2017.
 */
public class CustomListViewAdapter extends BaseAdapter
{
    String[] result;
    Context context;
    int[] imageId;
    String strLayout;
    private static LayoutInflater inflater = null;

    public CustomListViewAdapter(ActivityMain mainActivity, String[] prgmNameList, int[] prgmImages, String strLayoutName)
    {
        //TODO Auto-generated constructor stub
        result = prgmNameList;
        context = mainActivity;
        imageId = prgmImages;
        strLayout = strLayoutName;
        inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount()
    {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position)
    {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        View vLine;
        TextViewFont tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent)
    {
        // TODO Auto-generated method stub
        Holder holder = new Holder();
        View rowView;

        if(strLayout.equals("left"))
            rowView = inflater.inflate(R.layout.listview_custom_left, null);
        else
            rowView = inflater.inflate(R.layout.listview_custom_right, null);

        holder.vLine = (View)rowView.findViewById(R.id.listview_line);
        holder.tv = (TextViewFont) rowView.findViewById(R.id.listview_custom_text);
        holder.img = (ImageView) rowView.findViewById(R.id.listview_custom_image);
        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);

        // TODO : Enable line between rows in position 6
        if(position == 6)
        {
            holder.vLine.setVisibility(View.VISIBLE);
        }

        // TODO : Change text style and color in last row ,
        if(position == 7)
        {
            ViewGroup.LayoutParams tvParams = (ViewGroup.LayoutParams) holder.tv.getLayoutParams();
            tvParams.height = 100;
            holder.tv.setLayoutParams(tvParams);
            holder.tv.setTextColor(Color.GRAY);
            holder.tv.setTextSize(18);

            ViewGroup.LayoutParams imgParams = (ViewGroup.LayoutParams) holder.img.getLayoutParams();
            imgParams.height = 0;
            imgParams.width = 0;
            holder.img.setLayoutParams(imgParams);
            holder.img.setVisibility(View.GONE);

            return rowView;
        }
        /*rowView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked " + result[position], Toast.LENGTH_LONG).show();
                ActivityMain.drawerLayout.closeDrawer(ActivityMain.llMenuLeft);
                switch (position)
                {
                    case 1 :
                    {
                        v.setSelected(true);
                        ActivityMain.drawerLayout.closeDrawer(ActivityMain.llMenuLeft);
                        // Highlight item
                        // Change navigationdescription to savari
                        // Change navigation title to savari
                        // Change navigation Image to savari
                        // Change Service Switcher for Marker's Image to savari
                    }
                    break;
                    case 2 :
                    {
                        v.setSelected(true);
                        ActivityMain.drawerLayout.closeDrawer(ActivityMain.llMenuLeft);
                        // Highlight item
                        // Change navigationdescription to bari
                        // Change navigation title to bari
                        // Change navigation Image to bari
                        // Change Service Switcher for Marker's Image to bari
                    }
                    break;
                    case 3 :
                    {
                        v.setSelected(true);
                        ActivityMain.drawerLayout.closeDrawer(ActivityMain.llMenuLeft);
                        // Highlight item
                        // Change navigationdescription to peyk
                        // Change navigation title to peyk
                        // Change navigation Image to peyk
                        // Change Service Switcher for Marker's Image to peyk
                    }
                    break;
                }
            }
        });*/

        return rowView;
    }
}
