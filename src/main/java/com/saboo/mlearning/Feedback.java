package com.saboo.mlearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

public class Feedback extends AppCompatActivity {

    final static String TAG = "Feedback";
    Context context = this;
    ListView lv_feedback_profs;
    FeedbackProfAdapter fdpa;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_feedback );

        init();


        listViewClickListener();

    }

    private void init(){
        initViews();
    }

    private void initViews(){
        lv_feedback_profs = (ListView) findViewById( R.id.lv_feedback_profs );

    }

    @Override
    protected void onResume() {
        super.onResume();

        getProfessorListForFeedback();

    }

    String result = "";
    ProgressDialog pd, pd1;

    private void getProfessorListForFeedback(){
        pd = new ProgressDialog( context );
        pd.setMessage( "Loading List of Professors !" );
        pd.setIndeterminate( true );
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_profs_for_feedback" },
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
                FeedbackProfs fps[] = new FeedbackProfs[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    fps[ i ] = new FeedbackProfs();
                    fps[ i ].setStaffNname( "Prof. "+ jsonObject.getString( "fname" ) + " " +jsonObject.getString( "lname" ) );
                    boolean status = jsonObject.getString( "feedback_given" ).equals( "true" )?true:false;
                    fps[ i ].setIs_feedback_given( status );

                    fps[ i ].setStatus( (status)?"Done":"Pending" );
                    fps[ i ].setUsername( jsonObject.getString( "username" ) );
                }

                fdpa = new FeedbackProfAdapter( context, R.layout.feedback_prof, fps );
                lv_feedback_profs.setAdapter( fdpa );

                pd.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }

    private void listViewClickListener(){
        lv_feedback_profs.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                RelativeLayout rl = (RelativeLayout) view;
                TextView tv_username = (TextView)  rl.findViewById( R.id.tv_username );
                TextView tv_is_feedback_given = (TextView) rl.findViewById( R.id.tv_is_feedback_given );
                TextView tv_staff_name = (TextView)  rl.findViewById( R.id.tv_staff_name );

                String username = tv_username.getText().toString();
                String staff_name = tv_staff_name.getText().toString();
                boolean is_feedback_given = tv_is_feedback_given.getText().toString().equals( "true" )?true:false;


                if( is_feedback_given ){
                    Toast.makeText( context, "You have already given feedback to "+staff_name, Toast.LENGTH_LONG ).show();
                    return;
                }

                Intent in = new Intent( context, FeedbackFormActivity.class );
                in.putExtra( "username", username );
                in.putExtra( "current_sem", LoginSession.getSemesterOfLoggedInUser( context ) );
                startActivity( in );

            }
        });
    }


}

