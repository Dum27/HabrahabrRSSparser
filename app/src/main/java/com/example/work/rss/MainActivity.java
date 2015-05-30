package com.example.work.rss;

import android.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import org.xml.sax.SAXException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends Activity
{
    ArrayList<RssItem> rssItems;
    ArrayList<String> showItems;
    DatabaseHandler db;
    ListView listView;
    MyTask mt;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.list);

        getData();
    }

    public void getData()
    {
        if(isOnline(this))
        {
           // Toast.makeText(MainActivity.this,"internet connection enable",Toast.LENGTH_LONG).show();
            mt = new MyTask();
            mt.execute();
        }else
        {
           // Toast.makeText(MainActivity.this,"internet connection disable",Toast.LENGTH_LONG).show();
            db = new DatabaseHandler(this);
            rssItems = db.getAllItems();
            showData();
        }
    }

    public void showData()
    {
        showItems = new ArrayList<String>();
        for(RssItem rssItem : rssItems)
        {
            Log.i("RSS Reader", rssItem.getTitle());
            showItems.add(rssItem.getDay() + " " + rssItem.getTitle());
        }
        final ArrayAdapter<String> adapter;
        adapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, showItems);
        // Привяжем массив через адаптер к ListView
        listView.setAdapter(adapter);
        // Добавляем слушателя
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

              if(isOnline(MainActivity.this)) {
                  Intent intent = new Intent(MainActivity.this, WebVievActivity.class);
                  intent.putExtra("link", rssItems.get(position).getLink());
                  startActivity(intent);
              }else
              {
                  Toast.makeText(MainActivity.this,"sorry but internet connection is disable",Toast.LENGTH_LONG).show();
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
        db = new DatabaseHandler(this);
        db.deleteAll();
        for(int i = 0; i < rssItems.size(); i++)
        {
            db.addRssItem(new RssItem(rssItems.get(i).getTitle(),rssItems.get(i).getLink(),rssItems.get(i).getDay()));
        }
 }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    class MyTask extends AsyncTask<Void, Integer, Void>
    {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }
        @Override
        protected Void doInBackground(Void... params)
        {
            URL url = null;
            try{
                url = new URL("http://habrahabr.ru/rss/hubs/");
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

