package org.mysecondapp.ui;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.mysecondapp.R;
import org.mysecondapp.UserAuth;
import org.mysecondapp.UserData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Shawn Li on 10/11/2016.
 */

// swipe-to-delete inspired by https://github.com/nemanja-kovacevic/recycler-view-swipe-to-delete/blob/master/app/src/main/java/net/nemanjakovacevic/recyclerviewswipetodelete/MainActivity.java
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {

    private List<UserData> items, itemsPendingRemoval;
    private int lastInsertedIndex;
    private Activity activity;
    boolean undoOn;
    private Handler handler = new Handler(); // hanlder for running delayed runnables

    HashMap<UserData, Runnable> pendingRunnables = new HashMap<>();

    public class CardViewHolder extends RecyclerView.ViewHolder {
        int position;
        TextView user;
        TextView details;
        ImageView image;
        public CardViewHolder(View view) {
            super(view);
            this.user = (TextView)view.findViewById(R.id.textView);
            this.details = (TextView)view.findViewById(R.id.textViewDetails);
            this.image = (ImageView)view.findViewById(R.id.imageView);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                            Intent intSend = new Intent(activity.getBaseContext(), MyActivity.class);
                            intSend.putExtra("userData", items.get(position));
                            intSend.putExtra("new", false);
                            activity.startActivity(intSend);
                        }
                    });
        }
    }

    public CardAdapter(List<UserData> data, Activity activity) {
        this.items = data;
        this.itemsPendingRemoval = new ArrayList<>();
        this.activity = activity;
    }
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view, parent, false);
        return new CardViewHolder(v);
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        CardViewHolder viewHolder = holder;
        viewHolder.position = position;
        final UserData item = items.get(position);
        holder.user.setText(item.toString());
        holder.details.setText(item.detailedToString());
        holder.image.setImageBitmap(item.getImage());
        if (itemsPendingRemoval.contains(item)) {

            // we need to show the "undo" state of the row

            viewHolder.itemView.setBackgroundColor(Color.rgb(240, 240, 240));

        } else {

            // we need to show the "normal" state

            viewHolder.itemView.setBackgroundColor(Color.WHITE);



        }
    }



    public void setUndoOn(boolean undoOn) {

        this.undoOn = undoOn;

    }



    public boolean isUndoOn() {

        return undoOn;

    }



    public void pendingRemoval(int position) {

        final UserData item = items.get(position);

        if (!itemsPendingRemoval.contains(item)) {

            itemsPendingRemoval.add(item);

            // this will redraw row in "undo" state

            notifyItemChanged(position);

            // let's create, store and post a runnable to remove the item

            Runnable pendingRemovalRunnable = new Runnable() {

                @Override

                public void run() {

                    remove(items.indexOf(item));

                }

            };

            handler.postDelayed(pendingRemovalRunnable, 2400);

            pendingRunnables.put(item, pendingRemovalRunnable);

        }

    }



    public UserData remove(int position) {

        UserData item = items.get(position);

        if (itemsPendingRemoval.contains(item)) {

            itemsPendingRemoval.remove(item);

        }

        if (items.contains(item)) {

            items.remove(position);

            notifyItemRemoved(position);
        }
        return item;
    }



    public boolean isPendingRemoval(int position) {

        UserData item = items.get(position);

        return itemsPendingRemoval.contains(item);

    }
}
