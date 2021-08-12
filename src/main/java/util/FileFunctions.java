package util;

import android.os.Environment;
import android.webkit.MimeTypeMap;

import java.io.File;

/**
 * Created by Sohail on 17-04-2017.
 */

public class FileFunctions {
    public static boolean ifFileExist(  String dir_name, String file_name ){
        File dir = Environment.getExternalStoragePublicDirectory( dir_name );
        if( ! dir.exists() )
            dir.mkdirs();

        File file = new File( dir.getAbsolutePath() + File.separator + file_name );
        try{
            if( ! file.exists() )
                return false;
        }
        catch( Exception e ){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    // url = file path or whatever suitable URL you want.
    public static String getMimeType( String dir_name, String file_name ) {
        String type = null;
        File dir = Environment.getExternalStoragePublicDirectory( dir_name );
        File file = new File( dir.getAbsolutePath() + File.separator + file_name );

        String extension = MimeTypeMap.getFileExtensionFromUrl( file.getAbsolutePath() );
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
