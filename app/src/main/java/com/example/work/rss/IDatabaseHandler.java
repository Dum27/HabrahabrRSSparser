package com.example.work.rss;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Work on 30.05.2015.
 */public interface IDatabaseHandler
{
    public void addRssItem(RssItem item);
    //public Contact getContact(int id);
    public ArrayList<RssItem> getAllItems();
    public int getContactsCount();
    public void updateItem(RssItem item);
    public void deleteItem(RssItem item);
    public void deleteAll();
}