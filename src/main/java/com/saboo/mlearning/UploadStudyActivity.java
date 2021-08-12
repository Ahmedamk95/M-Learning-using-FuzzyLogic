package com.saboo.mlearning;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import util.LoginSession;
import util.URLFunctions;

public class UploadStudyActivity extends AppCompatActivity {

    final static String TAG = "UploadStudyActivity";
    Context context = this;
    Spinner sp_sem;
    ProgressDialog pd;
    String result = "";
    String selected_sem = "sem3";

    EditText et_name;
    Button bt_browse, bt_upload;
    TextView tv_selected_path;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_upload_study );

        init();

    }

    private void init(){
        initViews();

        spinnerItemSelected();
        browseButtonHandler();
        uploadButtonListener();
    }

    private void initViews(){
        sp_sem = (Spinner) findViewById( R.id.sp_sem );
        et_name = (EditText) findViewById( R.id.et_name );
        bt_browse = (Button) findViewById( R.id.bt_browse );
        bt_upload = (Button) findViewById( R.id.bt_upload );
        tv_selected_path = (TextView) findViewById( R.id.tv_selected_path );
    }

    private void spinnerItemSelected(){
        String[] semesters = new String[]{ "sem3", "sem4", "sem5", "sem6", "sem7", "sem8" };
        ArrayAdapter<String> sps = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, semesters );
        sp_sem.setAdapter( sps );

        sp_sem.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l ) {
                selected_sem = sp_sem.getSelectedItem().toString();
                // Toast.makeText( context, selected_sem , Toast.LENGTH_LONG ).show();
                //getStudyMaterials();
            }

            @Override
            public void onNothingSelected( AdapterView<?> adapterView ) {

            }
        });
    }

    private void browseButtonHandler(){
        bt_browse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                intent.putExtra(Intent.EXTRA_LOCAL_ONLY, true);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult( requestCode, resultCode, data );

        Uri uri = data.getData();
        String uriString = uri.getPath();
        File file = new File(uriString);
        String path = file.getAbsolutePath();
        Log.d( TAG, "-----> " + uriString );

        tv_selected_path.setText( path );

    }

    private void uploadButtonListener(){
        bt_upload.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View view ) {
                pd = new ProgressDialog( context );
                pd.setMessage( "Loading Study Materials !" );
                pd.setIndeterminate( true );
                pd.show();

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String url = String.format( URLFunctions.getWebserviceURLPath() + "?what_do_you_want=%s&sem=%s&name=%s&username=%s", "upload_study_material", selected_sem, et_name.getText().toString(), LoginSession.getUsernameOfLoggedInUser( context ) );

                        result = URLFunctions.uploadFile( url, "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                                { "what_do_you_want" , "upload_study_material" },
                                { "sem" , selected_sem },
                                { "name" , et_name.getText().toString() },
                                { "username" , LoginSession.getUsernameOfLoggedInUser( context ) }
                        } ), tv_selected_path.getText().toString() );

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
                pd.hide();
                return;
            }
            else if( type.equals( "success" ) ){
                Log.d( TAG, info );

                Toast.makeText( context, info, Toast.LENGTH_LONG ).show();

                finish();

                /*jsonArray = new JSONArray( info );
                StudyMaterial sm[] = new StudyMaterial[ jsonArray.length() ];
                for( int i = 0 ; i < jsonArray.length() ; i++ ){
                    jsonObject = jsonArray.getJSONObject( i );
                    sm[ i ] = new StudyMaterial();
                    sm[ i ].setProfName( "Prof. "+ jsonObject.getString( "fname" ) + " " +jsonObject.getString( "lname" ) );
                    sm[ i ].setFileName( jsonObject.getString( "file_name" ) );
                    sm[ i ].setName( jsonObject.getString( "name" ) );
                    sm[ i ].setFilePath( jsonObject.getString( "file_path" ) );
                }

                sma = new StudyMaterialsAdapter( context, R.layout.study_material, sm );
                lv_study_materials.setAdapter( sma );*/

                pd.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }


}
