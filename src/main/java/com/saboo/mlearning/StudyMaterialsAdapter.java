package com.saboo.mlearning;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sohail on 16-04-2017.
 */

public class StudyMaterialsAdapter extends BaseAdapter {

    int resId;
    Context context;
    StudyMaterial sm[];

    public StudyMaterialsAdapter(Context context, int resId, StudyMaterial sm[] ){
        this.resId = resId;
        this.context = context;
        this.sm = sm;
    }

    @Override
    public int getCount() {
        return sm.length;
    }

    @Override
    public Object getItem(int i) {
        return sm[ i ];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout rl = (RelativeLayout) View.inflate( context, resId, null );

        TextView tv_material_by = (TextView) rl.findViewById( R.id.tv_material_by );
        TextView tv_name = (TextView) rl.findViewById( R.id.tv_name );
        TextView tv_file_name = (TextView) rl.findViewById( R.id.tv_file_name );
        TextView tv_file_path = (TextView) rl.findViewById( R.id.tv_file_path );;

        tv_material_by.setText( sm[ i ].getProfName() );
        tv_name.setText( sm[ i ].getName() );
        tv_file_name.setText( sm[ i ].getFileName() );
        tv_file_path.setText( sm[ i ].getFilePath() );

        return rl;
    }
}
