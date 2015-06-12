package fragments;


import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.example.work.rss.R;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import customAdapter.CustomAdapter;
import dataBase.DatabaseHandler;
import rssReader.RssFeed;
import rssReader.RssItem;
import rssReader.RssReader;


public class Fragment1 extends Fragment
{
    Fragment2 fragment2;
    CustomAdapter customAdapter;
    ArrayList<RssItem> rssItems;
    DatabaseHandler db;
    ListView listView;
    Context context;
    MyTask mt;
    String link;


  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment1, null);
      listView = (ListView) view.findViewById(R.id.list);
    return view;

  }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        if (bundle != null)
        {
            link = bundle.getString("link");
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        context = getActivity();
        fragment2 = new Fragment2();
    }

    @Override
    public void onResume() {
        super.onResume();
    if(rssItems == null)
    {
        getData();
    }else
    {
        showData();
    }
    }

    public void getData()
    {
        if(isOnline(context))
        {
            mt = new MyTask();
            mt.execute(link,null,null);
        }else
        {
            db = new DatabaseHandler(context);
            rssItems = db.getAllItems();

            showData();
        }
    }

    public void showData()
    {
//        showItems = new ArrayList<String>();
//        for(RssItem rssItem : rssItems)
//        {
//            showItems.add(rssItem.getDay() + " " + rssItem.getTitle());
//        }
        //final ArrayAdapter<String> adapter;
       // adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, showItems);
        customAdapter = new CustomAdapter(context, rssItems);



        listView.setAdapter(customAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                if(isOnline(context))
                {
                    Bundle bundle = new Bundle();
                    bundle.putString("link", rssItems.get(position).getLink());
                    fragment2.setArguments(bundle);
                    FragmentTransaction fTrans = getFragmentManager().beginTransaction();
                    fTrans.replace(R.id.frgmCont, fragment2);
                    fTrans.addToBackStack(null);
                    fTrans.commit();

                }else
                {
                    Toast.makeText(context, R.string.noConnection, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public static boolean isOnline(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting())
        {
            return true;
        }
        return false;
    }

    public void rewriteDB()
    {
        db = new DatabaseHandler(context);
        db.deleteAll();
        for(int i = 0; i < rssItems.size(); i++)
        {
            db.addRssItem(new RssItem(rssItems.get(i).getTitle(),rssItems.get(i).getLink(),rssItems.get(i).getDay()));
        }
    }

    class MyTask extends AsyncTask<String, Integer, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(String... params)
        {
            try{
               URL url = new URL(params[0]);
                RssFeed feed = RssReader.read(url);
                rssItems = feed.getRssItems();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)
        {
            super.onPostExecute(result);
            showData();
            rewriteDB();
        }
    }
}