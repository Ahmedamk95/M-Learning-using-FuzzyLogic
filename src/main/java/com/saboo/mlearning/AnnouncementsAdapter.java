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

public class AnnouncementsAdapter extends BaseAdapter {

    int resId;
    Context context;
    AnnouncementItems ai[];

    public AnnouncementsAdapter(Context context, int resId, AnnouncementItems ai[] ){
        this.resId = resId;
        this.context = context;
        this.ai = ai;
    }

    @Override
    public int getCount() {
        return ai.length;
    }

    @Override
    public Object getItem(int i) {
        return ai[ i ];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        RelativeLayout rl = (RelativeLayout) View.inflate( context, resId, null );

        TextView tv_announcement_by = (TextView) rl.findViewById( R.id.tv_announcement_by );
        TextView tv_announcement = (TextView) rl.findViewById( R.id.tv_announcement );

        tv_announcement_by.setText( ai[ i ].getAnnouncementBy() );
        tv_announcement.setText( ai[ i ].getAnnouncement() );


        return rl;
    }
}
