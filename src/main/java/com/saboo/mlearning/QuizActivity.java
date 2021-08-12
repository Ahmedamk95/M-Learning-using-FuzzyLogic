package com.saboo.mlearning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class QuizActivity extends Activity {

    String quiz_id = "";
    Context context = this;
    final static String TAG = "AllQuizzesActivity";

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_quiz );

        init();
    }

    private void init(){
        initViews();

        getQuizContent();
    }

    private void initViews(){
        Intent in = getIntent();
        quiz_id = in.getStringExtra( "quiz_id" );


    }

    String result = "";
    ProgressDialog pd;

    private void getQuizContent(){

        pd = new ProgressDialog( context );
        pd.setMessage( "Loading Quiz !" );
        pd.setIndeterminate( true );
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = URLFunctions.makeRequestForData( URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_quiz" },
                        { "quiz_id" , quiz_id },
                        { "username" , LoginSession.getUsernameOfLoggedInUser( context ) }
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
                pd.hide();

                finish();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

                jsonObject = new JSONObject( info );
                

                jsonArray = new JSONArray( info );
                qits = new QuizItems[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    qits[ i ] = new QuizItems();
                    qits[ i ].setQuizBy( "Prof. " + jsonObject.getString( "fname" ) + " " + jsonObject.getString( "lname" ) );
                    qits[ i ].setQuizId( jsonObject.getString( "id" ) );
                    qits[ i ].setQuizGiven( jsonObject.getString( "quiz_given" ).equals( "true" )?true:false );
                    qits[ i ].setQuizTitle( jsonObject.getString( "quiz_title" ) );
                    qits[ i ].setQuizSem( jsonObject.getString( "sem" ) );
                    qits[ i ].setQuizContent( jsonObject.getString( "quiz_content" ) );
                }

                qa = new QuizAdapter( context, R.layout.quiz_item, qits );
                lv_all_quizzes.setAdapter( qa );


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
