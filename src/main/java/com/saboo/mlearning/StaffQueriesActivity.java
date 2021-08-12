package com.saboo.mlearning;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import util.LoginSession;
import util.URLFunctions;

public class StaffQueriesActivity extends AppCompatActivity {

    final static String TAG = "StaffQueriesActivity";
    Context context = this;
    ListView lv_all_queries;
    ProgressDialog pd;
    String result = "";
    StaffQueryAdapter sqa;

    TextView tv_query_by, tv_query, tv_query_status, tv_is_responded, tv_query_id, tv_query_response;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_staff_queries );

        init();
    }

    private void init(){
        initViews();

        getAllQueriesForStaff();

        queryReplyListener();
    }

    private void initViews(){
        lv_all_queries = (ListView) findViewById( R.id.lv_all_queries );
    }

    private void getAllQueriesForStaff(){
        pd = new ProgressDialog( context );
        pd.setMessage( "Loading !" );
        pd.setIndeterminate( true );
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                //String url = String.format( URLFunctions.getWebserviceURLPath() + "?what_do_you_want=%s&sem=%s&name=%s&username=%s", "upload_study_material", selected_sem, et_name.getText().toString(), LoginSession.getUsernameOfLoggedInUser( context ) );

                result = URLFunctions.makeRequestForData( URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_staff_queries" },
                        { "username" , LoginSession.getUsernameOfLoggedInUser( context ) }
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
                pd.hide();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                //Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

                jsonArray = new JSONArray( info );
                StaffQuery sq[] = new StaffQuery[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    sq[ i ] = new StaffQuery();
                    sq[ i ].setQueryBy( "Query By : " + jsonObject.getString( "query_by" ) );
                    sq[ i ].setQuery( jsonObject.getString( "query" ) );
                    sq[ i ].setQueryResponse( jsonObject.getString( "query_response" ) );
                    sq[ i ].setQueryStatus( jsonObject.getString( "is_responded" ).equals( "true" )?"Respnded":"Pending" );
                    sq[ i ].setQueryID( jsonObject.getString( "query_id" ) );
                    sq[ i ].setResponded( jsonObject.getString( "is_responded" ).equals( "true" )?true:false );
                }

                sqa = new StaffQueryAdapter( context, R.layout.staff_query, sq );
                lv_all_queries.setAdapter( sqa );

                pd.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
        }

    }

    ProgressDialog pd1;

    private void queryReplyListener(){



        lv_all_queries.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                RelativeLayout rl = (RelativeLayout) view;

                TextView tv_query_by = (TextView) rl.findViewById( R.id.tv_query_by );
                TextView tv_query = (TextView) rl.findViewById( R.id.tv_query );
                TextView tv_query_status = (TextView) rl.findViewById( R.id.tv_query_status );
                TextView tv_is_responded = (TextView) rl.findViewById( R.id.tv_is_responded );
                final TextView tv_query_id = (TextView) rl.findViewById( R.id.tv_query_id );
                TextView tv_query_response = (TextView) rl.findViewById( R.id.tv_query_response );

                boolean is_responded = tv_is_responded.getText().toString().equals( "true" )?true:false;
                if( is_responded ){
                    AlertDialog.Builder ab1 = new AlertDialog.Builder( context );
                    ab1.setTitle( tv_query_by.getText().toString() );
                    String message = String.format( "Query : %s\n\nResponse : %s", tv_query.getText().toString(), tv_query_response.getText().toString() );
                    ab1.setMessage( message );
                    ab1.show();
                }
                else{
                    AlertDialog.Builder ab2 = new AlertDialog.Builder( context );
                    ab2.setTitle( tv_query_by.getText().toString() );
                    String message = String.format( "Query : %s", tv_query.getText().toString() );
                    final EditText et_response = new EditText( context );
                    et_response.setHint( "Enter the response" );
                    ab2.setView( et_response );
                    ab2.setMessage( message );

                    ab2.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            // Submit the response to the server

                            if( et_response.getText().toString().trim().equals( "" ) ){
                                Toast.makeText( context, "Please enter a response !", Toast.LENGTH_SHORT ).show();
                                return;
                            }

                            pd1 = new ProgressDialog( context );
                            pd1.setMessage( "Please wait !" );
                            pd1.setIndeterminate( true );
                            pd1.show();


                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                                    result = URLFunctions.makeRequestForData( URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                            { "what_do_you_want" , "respond_to_query" },
                                            { "query_id" , tv_query_id.getText().toString() },
                                            { "response" , et_response.getText().toString() }
                                    } ) );

                                    Log.d( TAG, "result : "+result );

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if( result != null )
                                                processResult1( result );
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

                    ab2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });

                    ab2.show();
                }

            }

        });

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

                //Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

                /*jsonArray = new JSONArray( info );
                StaffQuery sq[] = new StaffQuery[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    sq[ i ] = new StaffQuery();
                    sq[ i ].setQueryBy( "Query By : " + jsonObject.getString( "query_by" ) );
                    sq[ i ].setQuery( jsonObject.getString( "query" ) );
                    sq[ i ].setQueryStatus( jsonObject.getString( "is_responded" ).equals( "true" )?"Respnded":"Pending" );
                    sq[ i ].setQueryID( jsonObject.getString( "query_id" ) );
                    sq[ i ].setResponded( jsonObject.getString( "is_responded" ).equals( "true" )?true:false );
                }*/


                pd1.hide();

                recreate();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
        }

    }

}
