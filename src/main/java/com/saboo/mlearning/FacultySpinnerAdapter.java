package com.saboo.mlearning;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sohail on 16-04-2017.
 */

public class FacultySpinnerAdapter extends BaseAdapter {

    int resId;
    Context context;
    FeedbackProfs fps[];

    public FacultySpinnerAdapter( Context context, int resId, FeedbackProfs fps[] ){
        this.resId = resId;
        this.context = context;
        this.fps = fps;
    }

    @Override
    public int getCount() {
        return fps.length;
    }

    @Override
    public Object getItem(int i) {
        return fps[ i ];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup ) {
        LinearLayout ll = (LinearLayout) View.inflate( context, resId, null );

        TextView tv_faculty_name = (TextView) ll.findViewById( R.id.tv_faculty_name );
        TextView tv_username = (TextView) ll.findViewById( R.id.tv_username );

        tv_faculty_name.setText( fps[ i ].getStaffName() );
        tv_username.setText( fps[ i ].getUsername() );

        return ll;
    }
}
