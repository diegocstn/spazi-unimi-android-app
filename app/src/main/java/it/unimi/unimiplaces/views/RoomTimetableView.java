package it.unimi.unimiplaces.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import it.unimi.unimiplaces.R;
import it.unimi.unimiplaces.core.model.RoomEvent;
import se.emilsjolander.stickylistheaders.StickyListHeadersAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * RoomTimetableView
 */
public class RoomTimetableView extends RelativeLayout {

    Context context;
    StickyListHeadersListView listView;

    public RoomTimetableView(Context context) {
        super(context);
        this.context = context;
        this.init();
    }

    public RoomTimetableView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        this.init();
    }

    private void init(){
        inflate(getContext(),R.layout.room_events,this);
        listView = (StickyListHeadersListView) findViewById(R.id.room_events_list);

    }

    private class RoomEventViewHeaderHolder {
        public TextView eventDay;
    }

    private class RoomEventViewEventHolder {
        public TextView eventName;
        public TextView eventTime;
    }

    public void setEventsList(List<RoomEvent> events){
        this.listView.setAdapter( new RoomTimetableAdapter(getContext(),events) );
    }

    private class RoomTimetableAdapter extends BaseAdapter implements StickyListHeadersAdapter{

        private LayoutInflater inflater;
        private List<RoomEvent> events;

        public RoomTimetableAdapter(Context context){
            this.inflater = LayoutInflater.from(context);
        }

        public RoomTimetableAdapter(Context context,List<RoomEvent> events){
            this.inflater    = LayoutInflater.from(context);
            this.events      = events;
        }

        @Override
        public int getCount() {
            return this.events.size();
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return this.events.get(position);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            RoomEventViewEventHolder holder;
            if( convertView==null ){
                holder = new RoomEventViewEventHolder();
                convertView = inflater.inflate(R.layout.room_event_list_item,parent,false);
                holder.eventName    = (TextView) convertView.findViewById(R.id.room_event_name);
                holder.eventTime    = (TextView) convertView.findViewById(R.id.room_event_time);
                convertView.setTag(holder);
            }else{
                holder = (RoomEventViewEventHolder) convertView.getTag();
            }

            holder.eventName.setText( events.get(position).getEventName() );
            holder.eventTime.setText( events.get(position).getTime() );

            return convertView;
        }

        @Override
        public View getHeaderView(int position, View convertView, ViewGroup parent) {
            RoomEventViewHeaderHolder holder;
            if( convertView==null ){
                holder = new RoomEventViewHeaderHolder();
                convertView = inflater.inflate(R.layout.room_event_list_item_day,parent,false);
                holder.eventDay    = (TextView) convertView.findViewById(R.id.room_event_day);
                convertView.setTag(holder);
            }else{
                holder = (RoomEventViewHeaderHolder) convertView.getTag();
            }

            holder.eventDay.setText( events.get(position).getDate() );

            return convertView;
        }

        @Override
        public long getHeaderId(int position) {
            return this.events.get(position).getDateId();
        }
    }
}
