package com.saboo.mlearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class CreateAnnouncementActivity extends AppCompatActivity {

    final static String TAG = "AnnouncementActivity";
    Context context = this;
    EditText et_announcement;
    Button bt_create_announcement;

    String result = "";
    ProgressDialog pd;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_create_announcement );

        init();
    }

    private void init(){
        initViews();

        createAnnouncementListener();
    }

    private void initViews(){
        et_announcement = (EditText) findViewById( R.id.et_announcement );
        bt_create_announcement = (Button) findViewById( R.id.bt_create_announcement );

    }

    private void createAnnouncementListener(){

        bt_create_announcement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {

                if( et_announcement.getText().toString().trim().equals( "" ) ){
                    Toast.makeText( context, "Please enter some text !", Toast.LENGTH_LONG ).show();
                    return;
                }


                pd = new ProgressDialog( context );
                pd.setMessage( "Creating Announcement !" );
                pd.setIndeterminate( true );

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                { "what_do_you_want" , "create_announcement" },
                                { "username" , LoginSession.getUsernameOfLoggedInUser( context ) },
                                { "announcement_text" , et_announcement.getText().toString() }
                        }));

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
        });
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
                finish();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

                et_announcement.setText( "" );

                pd.hide();
                //finish();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }



}
