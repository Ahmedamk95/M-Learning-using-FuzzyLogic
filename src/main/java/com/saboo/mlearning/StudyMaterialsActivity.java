package com.saboo.mlearning;

import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.excel.excelclasslibrary.UtilURL;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;

import util.FileFunctions;
import util.URLFunctions;

public class StudyMaterialsActivity extends Activity {

    final static String TAG = "StudyMaterialsActivity";
    Context context = this;
    Spinner sp_sem;
    ListView lv_study_materials;
    ProgressDialog pd;
    String result = "";
    String selected_sem = "sem3";
    StudyMaterialsAdapter sma;
    long downloadReference = -1;
    DownloadManager downloadManager;

    TextView tv_material_by, tv_name, tv_file_name, tv_file_path;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_study_materials );

        init();

    }

    private void init(){
        initViews();

        spinnerItemSelected();
        registerDownloadCompleteReceiver();
        listViewItemClickListener();
    }

    private void initViews(){
        sp_sem = (Spinner) findViewById( R.id.sp_sem );
        lv_study_materials = (ListView) findViewById( R.id.lv_study_materials );
        tv_material_by = (TextView) findViewById( R.id.tv_material_by );
        tv_name = (TextView) findViewById( R.id.tv_name );
        tv_file_name = (TextView) findViewById( R.id.tv_file_name );
        tv_file_path = (TextView) findViewById( R.id.tv_file_path );

        downloadManager = (DownloadManager) getSystemService( Context.DOWNLOAD_SERVICE );
    }

    private void spinnerItemSelected(){
        String[] semesters = new String[]{ "sem3", "sem4", "sem5", "sem6", "sem7", "sem8" };
        ArrayAdapter<String> sps = new ArrayAdapter<String>( context, android.R.layout.simple_list_item_1, semesters );
        sp_sem.setAdapter( sps );

        sp_sem.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected( AdapterView<?> adapterView, View view, int i, long l ) {
                selected_sem = sp_sem.getSelectedItem().toString();
                // Toast.makeText( context, selected_sem , Toast.LENGTH_LONG ).show();
                getStudyMaterials();
            }

            @Override
            public void onNothingSelected( AdapterView<?> adapterView ) {

            }
        });
    }

    private void getStudyMaterials(){
        // lv_study_materials.removeViews( 0, lv_study_materials.getCount() );
        lv_study_materials.setAdapter( null );

        pd = new ProgressDialog( context );
        pd.setMessage( "Loading Study Materials !" );
        pd.setIndeterminate( true );
        pd.show();

        new Thread(new Runnable() {
            @Override
            public void run() {

                result = URLFunctions.makeRequestForData(URLFunctions.getWebserviceURLPath(), "POST", UtilURL.getURLParamsFromPairs( new String[][]{
                        { "what_do_you_want" , "get_study_materials" },
                        { "sem" , selected_sem },
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

                jsonArray = new JSONArray( info );
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
                lv_study_materials.setAdapter( sma );

                pd.hide();
                return;
            }
        }
        catch ( Exception e ){
            e.printStackTrace();
            // return;
        }

    }

    private void listViewItemClickListener(){
        lv_study_materials.setOnItemClickListener( new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                RelativeLayout rl = (RelativeLayout) view;

                TextView tv_material_by = (TextView) rl.findViewById( R.id.tv_material_by );
                TextView tv_name = (TextView) rl.findViewById( R.id.tv_name );
                TextView tv_file_name = (TextView) rl.findViewById( R.id.tv_file_name );
                TextView tv_file_path = (TextView) rl.findViewById( R.id.tv_file_path );
                TextView tv_study_material_id = (TextView) rl.findViewById( R.id.tv_study_material_id );

                String file_name = tv_file_name.getText().toString();
                File dir = Environment.getExternalStoragePublicDirectory( "MLearning" );
                File file = new File( dir.getAbsolutePath() + File.separator + file_name );

                if( FileFunctions.ifFileExist( "MLearning", file_name ) ){
                    Intent intent = new Intent( Intent.ACTION_VIEW );

                    Uri data = Uri.fromFile( file );
                    intent.setDataAndType(data, FileFunctions.getMimeType( "MLearning", file_name ) );

                    startActivity( intent );
                }
                else{
                    String sm_id = tv_study_material_id.getText().toString();

                    downloadStudyMaterial( tv_file_path.getText().toString(), file.getAbsolutePath() );


                }



            }

        });
    }

    ProgressDialog pd_downloading;
    private void downloadStudyMaterial( String file_path, String file_save_path ){
        pd_downloading = new ProgressDialog( context );
        pd_downloading.setTitle( "Downloading" );
        pd_downloading.setMessage( "Please wait while the file is being downloaded !" );
        pd_downloading.setCancelable( false );
        pd_downloading.show();

        Uri uri = Uri.parse( URLFunctions.getServerRootPath() + file_path );
        DownloadManager.Request request = new DownloadManager.Request( uri );
        request.setNotificationVisibility( DownloadManager.Request.VISIBILITY_HIDDEN );

        request.setDestinationUri( Uri.fromFile( new File( file_save_path ) ) );
        downloadReference = downloadManager.enqueue( request );

    }

    BroadcastReceiver receiverDownloadComplete;

    private void  registerDownloadCompleteReceiver(){
        IntentFilter intentFilter = new IntentFilter( DownloadManager.ACTION_DOWNLOAD_COMPLETE );
        receiverDownloadComplete = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                long reference = intent.getLongExtra( DownloadManager.EXTRA_DOWNLOAD_ID, -1 );
                // String extraID = DownloadManager.EXTRA_NOTIFICATION_CLICK_DOWNLOAD_IDS;
                // long[] references = intent.getLongArrayExtra( extraID );
                //for( int i = 0 ; i < downloadReferences.length ; i++ ){
                    long ref = downloadReference;
                    //for( long ref : downloadReferences ){
                    if( ref == reference ){
                        //
                        DownloadManager.Query query = new DownloadManager.Query();
                        query.setFilterById( ref );
                        Cursor cursor = downloadManager.query( query );
                        cursor.moveToFirst();
                        int status = cursor.getInt( cursor.getColumnIndex( DownloadManager.COLUMN_STATUS ) );
                        String savedFilePath = cursor.getString( cursor.getColumnIndex( DownloadManager.COLUMN_LOCAL_FILENAME ) );
                        savedFilePath = savedFilePath.substring( savedFilePath.lastIndexOf( "/" ) + 1, savedFilePath.length() );
                        switch( status ){
                            case DownloadManager.STATUS_SUCCESSFUL:
                                Log.i( TAG, savedFilePath + " downloaded successfully !" );
                                Toast.makeText( context, "Downloaded Successfully !", Toast.LENGTH_LONG ).show();
                                pd_downloading.hide();
                                break;
                            case DownloadManager.STATUS_FAILED:
                                Log.e( TAG, savedFilePath + " failed to download !" );
                                Toast.makeText( context, "Failed to Download File !", Toast.LENGTH_LONG ).show();
                                pd_downloading.hide();
                                break;
                            case DownloadManager.STATUS_PAUSED:
                                Log.e( TAG, savedFilePath + " download paused !" );
                                break;
                            case DownloadManager.STATUS_PENDING:
                                Log.e( TAG, savedFilePath + " download pending !" );
                                break;
                            case DownloadManager.STATUS_RUNNING:
                                Log.d( TAG, savedFilePath + " downloading !" );
                                break;


                        }
                        downloadReference = -1;
                    }
                //}
            }
        };
        registerReceiver( receiverDownloadComplete, intentFilter );
    }
}
