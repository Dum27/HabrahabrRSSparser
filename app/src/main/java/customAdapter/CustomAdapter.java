package customAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.work.rss.R;

import java.util.ArrayList;

import rssReader.RssItem;

public class CustomAdapter extends BaseAdapter
{
    Context context;
    LayoutInflater lInflater;
    ArrayList<RssItem> itemsAr;

    String fontPath = "fonts/Contribute_FREE-version.ttf";

    public CustomAdapter(Context context, ArrayList<RssItem> itemsAr)
    {
        this.context = context;
        this.itemsAr = itemsAr;

        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

     // count of elements
     @Override public int getCount() {return itemsAr.size();}

     // element in position
     @Override public Object getItem(int position) {return itemsAr.get(position);}

     // id in position
     @Override public long getItemId(int position) {return position;}

    // filling and return view
     @Override public View getView(int position, View convertView, ViewGroup parent)
     {
         // now we using old view if he don't equals null.
         // But if we use more then twenty colors, would be better re inflate view (delete this checking)
//         if(convertView==null)
//         {
             convertView = lInflater.inflate(R.layout.item, parent, false);
      //   }

         RssItem item = getProduct(position);

         TextView txt_Day = (TextView) convertView.findViewById(R.id.txt_day);
         TextView txt_Title = (TextView) convertView.findViewById(R.id.txt_title);

         Typeface typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
         txt_Day.setTypeface(typeface);
         txt_Title.setTypeface(typeface);

         txt_Day.setText(item.getDay());
         txt_Title.setText(item.getTitle());
//         tvColor.setTextColor(Color.parseColor(c.getColorCode()));


         return convertView;
     }
    // item in position
    RssItem getProduct(int position)
    {
        return ((RssItem) getItem(position));
    }
}