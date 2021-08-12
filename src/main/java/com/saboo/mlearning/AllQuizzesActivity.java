package com.saboo.mlearning;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
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

public class AllQuizzesActivity extends Activity {

    Context context = this;
    final static String TAG = "AllQuizzesActivity";
    QuizAdapter qa;
    ListView lv_all_quizzes;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_all_quizzes );

        init();


    }

    private void init(){
        initViews();

        getQuizInfo();

        startQuizActivity();

        quizClickListener();

    }

    private void initViews(){
        lv_all_quizzes = (ListView) findViewById( R.id.lv_all_quizzes );
    }

    String result = "";
    ProgressDialog pd;


    private void getQuizInfo(){

        pd = new ProgressDialog( context );
        pd.setMessage( "Loading Quizzes !" );
        pd.setIndeterminate( true );

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_quiz_info" },
                        { "username" , LoginSession.getUsernameOfLoggedInUser( context ) },
                        { "current_sem" , LoginSession.getSemesterOfLoggedInUser( context ) }
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

    QuizItems qits[];

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

               // Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

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

    private void startQuizActivity(){
        lv_all_quizzes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                RelativeLayout rl = (RelativeLayout) view;
                /*TextView tv_quiz_by = (TextView) rl.findViewById( R.id.tv_quiz_by );
                TextView tv_quiz_title = (TextView) rl.findViewById( R.id.tv_quiz_title );
                TextView tv_quiz_status = (TextView) rl.findViewById( R.id.tv_quiz_status );*/
                TextView tv_quiz_given = (TextView) rl.findViewById(R.id.tv_quiz_given);/*
                TextView tv_quiz_id = (TextView) rl.findViewById( R.id.tv_quiz_id );*/
                TextView tv_quiz_content = (TextView) rl.findViewById( R.id.tv_quiz_content );


                if( tv_quiz_given.getText().toString().equals( "true" ) ){
                    Toast.makeText( context, "You have already given this quiz. Please try another !", Toast.LENGTH_LONG ).show();
                    return;
                }

                Intent in = new Intent( context, QuizActivity.class );
                in.putExtra( "quiz_content", tv_quiz_content.getText().toString() );
                startActivity( in );
            }
        });
    }

    private void quizClickListener(){
        lv_all_quizzes.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                RelativeLayout rl = (RelativeLayout) view;
                TextView tv_quiz_given = (TextView) rl.findViewById( R.id.tv_quiz_given );
                TextView tv_quiz_id = (TextView) rl.findViewById( R.id.tv_quiz_id );

                if( tv_quiz_given.getText().equals( "true" ) ){
                    Toast.makeText( context, "You have already given this quiz !", Toast.LENGTH_LONG ).show();
                    return;
                }

                Intent in = new Intent( context, QuizActivity.class );
                in.putExtra( "quiz_id", tv_quiz_id.getText().toString() );
                startActivity( in );

            }
        });
    }
}
