package com.saboo.mlearning;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class QueryActivity extends Activity {

    EditText et_ask_query;
    Spinner sp_faculties;
    RadioGroup rg_anonymous;
    RadioButton rb_yes, rb_no;
    ListView lv_all_queries;
    FacultySpinnerAdapter fspa;
    QueryListAdapter qla;
    Button bt_submit_query;

    TextView tv_query, tv_response, tv_status, tv_is_replied;

    final static String TAG = "QueryActivity";
    Context context = this;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_query );

        init();

    }

    private void init(){
        initViews();

        retrieveFaculties();
        // retrieve all Queries
        retrieveAllQueries();

        allQueriesItemClickListener();

        submitQueryButtonListener();
    }

    private void initViews(){
        et_ask_query = (EditText) findViewById( R.id.et_ask_query );
        sp_faculties = (Spinner) findViewById( R.id.sp_faculties );
        rg_anonymous = (RadioGroup) findViewById( R.id.rg_anonymous );
        rb_yes = (RadioButton) findViewById( R.id.rb_yes );
        rb_no = (RadioButton) findViewById( R.id.rb_no );
        lv_all_queries = (ListView) findViewById( R.id.lv_all_queries );

        tv_query = (TextView) findViewById( R.id.tv_query );
        tv_response = (TextView) findViewById( R.id.tv_response );
        tv_status = (TextView) findViewById( R.id.tv_status );
        tv_is_replied = (TextView) findViewById( R.id.tv_is_replied );

        bt_submit_query = (Button) findViewById( R.id.bt_submit_query );
    }

    ProgressDialog pd, pd1;
    String result = "";
    String result1 = "";


    private void retrieveFaculties(){
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
                    fps[ i ].setUsername( jsonObject.getString( "username" ) );
                }

                fspa = new FacultySpinnerAdapter( context, R.layout.faculty_spinner, fps );
                sp_faculties.setAdapter( fspa );

                pd.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }

    private void retrieveAllQueries(){
        pd1 = new ProgressDialog( context );
        pd1.setMessage( "Loading all your queries !" );
        pd1.setIndeterminate( true );
        pd1.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                result1 = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_student_queries" },
                        { "username" , LoginSession.getUsernameOfLoggedInUser( context )},
                } ) );

                Log.d( TAG, "result1 : "+result1 );


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if( result1 != null )
                            processResult1( result1 );
                        else{
                            Toast.makeText( context, "Failed to connect to the Server !", Toast.LENGTH_LONG ).show();
                            pd1.hide();
                            finish();
                        }
                    }
                });

            }
        }).start();
    }

    private void processResult1( String result ){
        String info = null;
        try{
            JSONArray jsonArray = new JSONArray( result );
            JSONObject jsonObject = jsonArray.getJSONObject( 0 );
            String type = jsonObject.getString( "type" );
            info = jsonObject.getString( "info" );

            if( type.equals( "error" ) ){
                Log.e( TAG, info );
                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();
                pd1.hide();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                jsonArray = new JSONArray( info );
                QueryItems qi[] = new QueryItems[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    //Log.d( TAG, jsonObject.toString() );
                    qi[ i ] = new QueryItems();
                    qi[ i ].setQuery( jsonObject.getString( "query" ) );
                    qi[ i ].setQueryStatus( jsonObject.getString( "is_replied" ).equals( "1" )?"Responded":"Pending" );
                    qi[ i ].setReplied( jsonObject.getString( "is_replied" ).equals( "1" )?true:false );
                    qi[ i ].setResponse( jsonObject.getString( "response" ) );

                }

                qla = new QueryListAdapter( context, R.layout.query_list_item, qi );
                lv_all_queries.setAdapter( qla );

                pd1.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }

    private void allQueriesItemClickListener(){

        lv_all_queries.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                RelativeLayout ll = (RelativeLayout) view;
                TextView tv_query = (TextView) ll.findViewById( R.id.tv_query );
                TextView tv_response = (TextView) ll.findViewById( R.id.tv_response );

                String query = tv_query.getText().toString();
                String response = tv_response.getText().toString();

                AlertDialog.Builder ab = new AlertDialog.Builder( context );
                String msg = String.format( "%s \n%s", query, response );
                ab.setMessage( msg );

                ab.show();
            }
        });

    }

    ProgressDialog pd2;

    String result2;
    LinearLayout ll_selected_faculty;
    private void submitQueryButtonListener(){

        ll_selected_faculty = (LinearLayout) sp_faculties.getSelectedItem();

        sp_faculties.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ll_selected_faculty = (LinearLayout) view;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        bt_submit_query.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                final String asked_query = et_ask_query.getText().toString();
                //LinearLayout ll = (LinearLayout) sp_faculties.getChildAt( sp_faculties.getSelectedItemPosition() );
                //TextView tv_faculty_name = (TextView) ll.findViewById( R.id.tv_faculty_name );
                TextView tv_username = (TextView) ll_selected_faculty.findViewById( R.id.tv_username );
                final String anonymity = ( rb_yes.isChecked() )?"1":"0";

                final String query_for = tv_username.getText().toString();

                // validate for emptiness
                if( asked_query.equals( "" ) ){
                    Toast.makeText( context, "Please enter some query !", Toast.LENGTH_LONG ).show();
                    return;
                }

                pd2 = new ProgressDialog( context );
                pd2.setMessage( "Submitting Query !" );
                pd2.setIndeterminate( true );
                pd2.setCancelable( false );
                pd2.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        result2 = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                { "what_do_you_want" , "submit_student_query" },
                                { "query_by" , LoginSession.getUsernameOfLoggedInUser( context )},
                                { "query_for" , query_for },
                                { "query" , asked_query },
                                { "anonymity" , anonymity },

                        } ) );

                        Log.d( TAG, "result2 : "+result2 );


                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if( result2 != null )
                                    processResult2( result2 );
                                else{
                                    Toast.makeText( context, "Failed to connect to the Server !", Toast.LENGTH_LONG ).show();
                                    pd2.hide();
                                    finish();
                                }
                            }
                        });

                    }
                }).start();

            }
        });

    }

    private void processResult2( String result ){
        String info = null;
        try{
            JSONArray jsonArray = new JSONArray( result );
            JSONObject jsonObject = jsonArray.getJSONObject( 0 );
            String type = jsonObject.getString( "type" );
            info = jsonObject.getString( "info" );

            if( type.equals( "error" ) ){
                Log.e( TAG, info );
                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();
                pd2.hide();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );


                // Toast.makeText( context, info, Toast.LENGTH_LONG ).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        recreate();
                    }
                }, 3000 );


                pd2.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }

}
