package com.saboo.mlearning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class AnnouncementsActivity extends Activity {

    final static String TAG = "AnnouncementsActivity";
    Context context = this;
    ListView lv_announcements;
    ProgressDialog pd;
    String result = "";
    AnnouncementsAdapter aia;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_announcements );

        init();


    }

    private void init(){
        initViews();

        getAnnouncements();
        announcementClickListener();

    }

    private void initViews(){
        lv_announcements = (ListView) findViewById( R.id.lv_announcements );
    }

    private void getAnnouncements(){
        pd = new ProgressDialog( context );
        pd.setMessage( "Loading List of Professors !" );
        pd.setIndeterminate( true );
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_announcements" },
                        { "username" , LoginSession.getUsernameOfLoggedInUser( context )},
                } ) );

                Log.d( TAG, "result : "+result );

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( result != null )
                            processResult( result );
                        else{
                            Toast.makeText( context, "Failed to connect to the Server !", Toast.LENGTH_LONG ).show();
                            pd.hide();
                            finish();
                        }
                    }
                });

            }
        }).start();
    }

    private void processResult( String result ){
        String info = null;
        try{
            JSONArray jsonArray = new JSONArray( result );
            JSONObject jsonObject = jsonArray.getJSONObject( 0 );
            String type = jsonObject.getString( "type" );
            info = jsonObject.getString( "info" );

            if( type.equals( "error" ) ){
                Log.e( TAG, info );
                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                jsonArray = new JSONArray( info );
                AnnouncementItems ai[] = new AnnouncementItems[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    ai[ i ] = new AnnouncementItems();
                    ai[ i ].setAnnouncementBy( "Prof. "+ jsonObject.getString( "fname" ) + " " +jsonObject.getString( "lname" ) );
                    ai[ i ].setAnnouncement( jsonObject.getString( "announcement" ) );
                }

                aia = new AnnouncementsAdapter( context, R.layout.announcement_view, ai );
                lv_announcements.setAdapter( aia );

                pd.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }

    private void announcementClickListener(){
        lv_announcements.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout rl = (RelativeLayout) view;
                TextView tv_announcement = (TextView) rl.findViewById( R.id.tv_announcement );

                String announcement = tv_announcement.getText().toString();

                AlertDialog.Builder ab = new AlertDialog.Builder( context );
                ab.setMessage( announcement );
                ab.show();
            }
        });

    }
}
