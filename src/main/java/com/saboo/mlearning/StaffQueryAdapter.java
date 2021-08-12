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

public class StaffQueryAdapter extends BaseAdapter {

    int resId;
    Context context;
    StaffQuery sq[];

    public StaffQueryAdapter( Context context, int resId, StaffQuery sq[] ){
        this.resId = resId;
        this.context = context;
        this.sq = sq;
    }

    @Override
    public int getCount() {
        return sq.length;
    }

    @Override
    public Object getItem(int i) {
        return sq[ i ];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout rl = (RelativeLayout) View.inflate( context, resId, null );

        TextView tv_query_by = (TextView) rl.findViewById( R.id.tv_query_by );
        TextView tv_query = (TextView) rl.findViewById( R.id.tv_query );
        TextView tv_query_status = (TextView) rl.findViewById( R.id.tv_query_status );
        TextView tv_is_responded = (TextView) rl.findViewById( R.id.tv_is_responded );
        TextView tv_query_id = (TextView) rl.findViewById( R.id.tv_query_id );
        TextView tv_query_response = (TextView) rl.findViewById( R.id.tv_query_response );

        tv_query_by.setText( sq[ i ].getQueryBy() );
        tv_query.setText( sq[ i ].getQuery() );
        tv_query_status.setText( sq[ i ].getQueryStatus() );
        tv_is_responded.setText( String.valueOf( sq[ i ].isResponded() ) );
        tv_query_id.setText( sq[ i ].getQueryID() );
        tv_query_response.setText( sq[ i ].getQueryResponse() );

        return rl;
    }
}
