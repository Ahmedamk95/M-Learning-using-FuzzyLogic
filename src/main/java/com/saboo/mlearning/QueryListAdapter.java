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

public class QueryListAdapter extends BaseAdapter {

    int resId;
    Context context;
    QueryItems qi[];

    public QueryListAdapter( Context context, int resId, QueryItems qi[] ){
        this.resId = resId;
        this.context = context;
        this.qi = qi;
    }

    @Override
    public int getCount() {
        return qi.length;
    }

    @Override
    public Object getItem(int i) {
        return qi[ i ];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout rl = (RelativeLayout) View.inflate( context, resId, null );

        TextView tv_query = (TextView) rl.findViewById( R.id.tv_query );
        TextView tv_response = (TextView) rl.findViewById( R.id.tv_response );
        TextView tv_status = (TextView) rl.findViewById( R.id.tv_status );
        TextView tv_is_replied = (TextView) rl.findViewById( R.id.tv_is_replied );

        tv_query.setText( "Query : " + qi[ i ].getQuery() );
        tv_response.setText( "Response : " + qi[ i ].getResponse() );
        tv_status.setText( qi[ i ].getQueryStatus() );
        tv_is_replied.setText( String.valueOf( qi[ i ].isReplied() ) );

        return rl;
    }
}
