package com.example.realnapster.jsonparser;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final String _url="http://api.androidhive.info/contacts/";
    JSONArray contacts;
    ListView lv;
    private ProgressDialog pDialog;
    ArrayList<HashMap<String,String >> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv=(ListView)findViewById(R.id.lst);
        contacts=new JSONArray();
        contactList=new ArrayList<HashMap<String,String>>();
        new GetContacts().execute();
    }
    private class GetContacts extends AsyncTask<Void,Void,Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            String res = ServiceHandler.GETResult(_url);
            Log.d("THis is json string",res);
            Log.d("Response:", ">" + res);
            if (res != null) {
                try{
                    JSONObject jObj=new JSONObject(res);
                    //Getting JSON array node
                    contacts=jObj.getJSONArray(JsonTags.tag_contacts);
                    for(int i=0;i<contacts.length();i++){
                        JSONObject subObj=contacts.getJSONObject(i);
                        String id=subObj.optString(JsonTags.tag_id);
                        String name=subObj.optString(JsonTags.tag_name);
                        String email=subObj.optString(JsonTags.tag_email);
                        String address=subObj.optString(JsonTags.tag_address);
                        String gender=subObj.optString(JsonTags.tag_gender);
                        JSONObject phone=subObj.getJSONObject(JsonTags.tag_phone);
                        String mobile=subObj.optString(JsonTags.tag_mobile);
                        String home=subObj.optString(JsonTags.tag_home);
                        String office=subObj.optString(JsonTags.tag_office);
                        //Hashmap for single contact
                        HashMap<String,String> contact=new HashMap<String,String>();
                        //adding each child node to hash map key
                        //key,value
                        contact.put(JsonTags.tag_id,id);
                        contact.put(JsonTags.tag_name,name);
                        contact.put(JsonTags.tag_email,email);
                        contact.put(JsonTags.tag_address,address);
                        contact.put(JsonTags.tag_gender,gender);
                        contact.put(JsonTags.tag_mobile,mobile);
                        contact.put(JsonTags.tag_home,home);
                        contact.put(JsonTags.tag_office,office);
                        contactList.add(contact);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else
            {
                Log.e("Service Handler","Couldn't Get any data from url");
            }
            return  null;

        }

        @Override
        protected void onPostExecute(Void result) {
              super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();
            String[] from=new String[]{JsonTags.tag_id,JsonTags.tag_name,JsonTags.tag_email,JsonTags.tag_address,JsonTags.tag_gender};
            int[] to=new int[]{R.id.id,R.id.name,R.id.email,R.id.address,R.id.gender};
            SimpleAdapter adapter=new SimpleAdapter(MainActivity.this,contactList,R.layout.list_item,from,to);
            lv.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
