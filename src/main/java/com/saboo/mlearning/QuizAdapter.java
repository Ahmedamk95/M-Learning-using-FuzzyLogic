package com.saboo.mlearning;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Sohail on 23-04-2017.
 */

public class QuizAdapter extends BaseAdapter {

    Context context;
    int resId;
    QuizItems qits[];

    public QuizAdapter( Context context, int resId, QuizItems qits[] ){
        this.context = context;
        this.resId = resId;
        this.qits = qits;
    }

    @Override
    public int getCount() {
        return qits.length;
    }

    @Override
    public Object getItem( int i ) {
        return qits[ i ];
    }

    @Override
    public long getItemId( int i ) {
        return i;
    }

    @Override
    public View getView( int i, View view, ViewGroup viewGroup ) {
        RelativeLayout rl = (RelativeLayout) View.inflate( context, resId, null );
        TextView tv_quiz_by = (TextView) rl.findViewById( R.id.tv_quiz_by );
        TextView tv_quiz_title = (TextView) rl.findViewById( R.id.tv_quiz_title );
        TextView tv_quiz_status = (TextView) rl.findViewById( R.id.tv_quiz_status );
        TextView tv_quiz_given = (TextView) rl.findViewById( R.id.tv_quiz_given );
        TextView tv_quiz_id = (TextView) rl.findViewById( R.id.tv_quiz_id );
        TextView tv_quiz_content = (TextView) rl.findViewById( R.id.tv_quiz_content );

        tv_quiz_by.setText( "Quiz By : " + qits[ i ].getQuizBy() );
        tv_quiz_title.setText( qits[ i ].getQuizTitle() );
        tv_quiz_status.setText( qits[ i ].isQuizGiven()?"Given":"Not Given" );
        tv_quiz_given.setText( String.valueOf( qits[ i ].isQuizGiven() ) );
        tv_quiz_id.setText( qits[ i ].getQuizId() );
        tv_quiz_content.setText( qits[ i ].getQuizContent() );

        return rl;
    }
}
