package com.saboo.mlearning;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sohail on 15-04-2017.
 */

public class FeedbackProfAdapter extends BaseAdapter {

    FeedbackProfs fps[];
    int resId;
    Context context;

    public FeedbackProfAdapter( Context context, int resId, FeedbackProfs[] fps ){
        this.context = context;
        this.resId = resId;
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
        RelativeLayout rl = (RelativeLayout) View.inflate( context, resId, null );

        TextView tv_staff_name = (TextView) rl.findViewById( R.id.tv_staff_name );
        TextView tv_feedback_status = (TextView) rl.findViewById( R.id.tv_feedback_status );
        TextView tv_is_feedback_given = (TextView) rl.findViewById( R.id.tv_is_feedback_given );
        TextView tv_username = (TextView) rl.findViewById( R.id.tv_username );

        tv_staff_name.setText( fps[ i ].getStaffName() );
        tv_feedback_status.setText( fps[ i ].getStatus() );
        tv_is_feedback_given.setText( String.valueOf( fps[ i ].is_feedback_given() ) );
        //Toast.makeText( context, String.valueOf( fps[ i ].is_feedback_given() ), Toast.LENGTH_SHORT ).show();
        tv_username.setText( fps[ i ].getUsername() );

        return rl;
    }
}
