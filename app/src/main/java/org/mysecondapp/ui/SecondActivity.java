package org.mysecondapp.ui;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.support.v7.widget.helper.ItemTouchUIUtil;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import org.mysecondapp.R;
import org.mysecondapp.UserDBHelper;
import org.mysecondapp.UserData;

/**
 * Created by Shawn Li on 10/5/2016.
 */
public class SecondActivity extends AppCompatActivity {

    private UserDBHelper helper = new UserDBHelper(this);
    private UserData selectedUser = null;
    private final int REQUEST_ADD_OR_EDIT = 2;
    private Intent thisIntent;
    private boolean isAdmin;
    private RecyclerView mRecyclerView;

    @TargetApi(16)
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_2);
        thisIntent = getIntent();
        if (thisIntent.hasExtra("userData")) {
            selectedUser = thisIntent.getParcelableExtra("userData");
            if (thisIntent.getBooleanExtra("new", true))
                helper.addRecord(selectedUser);
            else
                helper.updateRecord(selectedUser);
        }
        isAdmin = thisIntent.getBooleanExtra("isAdmin", false);


        Button newBtn = (Button) findViewById(R.id.newBtn);
        if (!isAdmin) {
            newBtn.setEnabled(false);
        }
        final ArrayList<UserData> list = helper.getAllRecords();
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(new CardAdapter(list, this));
        setUpItemTouchHelper();
        setUpAnimationDecoratorHelper();

        thisIntent.removeExtra("userData"); // to prevent duplication of records should screen orientation change (Activity destroys and recreates itself)


        newBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSend = new Intent(getBaseContext(), MyActivity.class);
                intSend.putExtra("userData", (Parcelable) null);
                intSend.putExtra("new", true);
                startActivity(intSend);
            }
        });
    }

    // swipe-to-delete inspired by https://github.com/nemanja-kovacevic/recycler-view-swipe-to-delete/blob/master/app/src/main/java/net/nemanjakovacevic/recyclerviewswipetodelete/MainActivity.java
        private void setUpItemTouchHelper() {



            ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {



                // we want to cache these and not allocate anything repeatedly in the onChildDraw method

                Drawable background;

                Drawable xMark;

                int xMarkMargin;

                boolean initiated;



                private void init() {

                    background = new ColorDrawable(Color.WHITE);

                    xMark = ContextCompat.getDrawable(SecondActivity.this, android.R.drawable.ic_delete);

                    xMark.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);

                    xMarkMargin = (int) SecondActivity.this.getResources().getDimension(android.R.dimen.app_icon_size);

                    initiated = true;

                }



                // not important, we don't want drag & drop

                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                    return false;

                }



                @Override
                public int getSwipeDirs(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                    int position = viewHolder.getAdapterPosition();

                    CardAdapter testAdapter = (CardAdapter) recyclerView.getAdapter();

                    if (testAdapter.isUndoOn() && testAdapter.isPendingRemoval(position)) {

                        return 0;

                    }

                    return super.getSwipeDirs(recyclerView, viewHolder);

                }



                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {

                    int swipedPosition = viewHolder.getAdapterPosition();

                    CardAdapter adapter = (CardAdapter)mRecyclerView.getAdapter();

                    boolean undoOn = adapter.isUndoOn();

                    if (undoOn) {

                        adapter.pendingRemoval(swipedPosition);

                    } else {
                        UserData removed = adapter.remove(swipedPosition);
                        helper.deleteRecord(removed.getID());
                    }

                }



                @Override
                public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {

                    View itemView = viewHolder.itemView;



                    // not sure why, but this method get's called for viewholder that are already swiped away

                    if (viewHolder.getAdapterPosition() == -1) {

                        // not interested in those

                        return;

                    }



                    if (!initiated) {

                        init();

                    }



                    // draw red background

                    background.setBounds(itemView.getRight() + (int) dX, itemView.getTop(), itemView.getRight(), itemView.getBottom());

                    background.draw(c);



                    // draw x mark

                    int itemHeight = itemView.getBottom() - itemView.getTop();

                    int intrinsicWidth = xMark.getIntrinsicWidth();

                    int intrinsicHeight = xMark.getIntrinsicWidth();



                    int xMarkLeft = itemView.getRight() - xMarkMargin - intrinsicWidth;

                    int xMarkRight = itemView.getRight() - xMarkMargin;

                    int xMarkTop = itemView.getTop() + (itemHeight - intrinsicHeight)/2;

                    int xMarkBottom = xMarkTop + intrinsicHeight;

                    xMark.setBounds(xMarkLeft, xMarkTop, xMarkRight, xMarkBottom);



                    xMark.draw(c);



                    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

                }



            };

            ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);

            mItemTouchHelper.attachToRecyclerView(mRecyclerView);

        }



        /**

         * We're gonna setup another ItemDecorator that will draw the red background in the empty space while the items are animating to thier new positions

         * after an item is removed.

         */

        private void setUpAnimationDecoratorHelper() {

            mRecyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {



                // we want to cache this and not allocate anything repeatedly in the onDraw method

                Drawable background;

                boolean initiated;



                private void init() {

                    background = new ColorDrawable(Color.rgb(240, 240, 240));

                    initiated = true;

                }



                @Override

                public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {



                    if (!initiated) {

                        init();

                    }



                    // only if animation is in progress

                    if (parent.getItemAnimator().isRunning()) {



                        // some items might be animating down and some items might be animating up to close the gap left by the removed item

                        // this is not exclusive, both movement can be happening at the same time

                        // to reproduce this leave just enough items so the first one and the last one would be just a little off screen

                        // then remove one from the middle



                        // find first child with translationY > 0

                        // and last one with translationY < 0

                        // we're after a rect that is not covered in recycler-view views at this point in time

                        View lastViewComingDown = null;

                        View firstViewComingUp = null;



                        // this is fixed

                        int left = 0;

                        int right = parent.getWidth();



                        // this we need to find out

                        int top = 0;

                        int bottom = 0;



                        // find relevant translating views

                        int childCount = parent.getLayoutManager().getChildCount();

                        for (int i = 0; i < childCount; i++) {

                            View child = parent.getLayoutManager().getChildAt(i);

                            if (child.getTranslationY() < 0) {

                                // view is coming down

                                lastViewComingDown = child;

                            } else if (child.getTranslationY() > 0) {

                                // view is coming up

                                if (firstViewComingUp == null) {

                                    firstViewComingUp = child;

                                }

                            }

                        }



                        if (lastViewComingDown != null && firstViewComingUp != null) {

                            // views are coming down AND going up to fill the void

                            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();

                            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();

                        } else if (lastViewComingDown != null) {

                            // views are going down to fill the void

                            top = lastViewComingDown.getBottom() + (int) lastViewComingDown.getTranslationY();

                            bottom = lastViewComingDown.getBottom();

                        } else if (firstViewComingUp != null) {

                            // views are coming up to fill the void

                            top = firstViewComingUp.getTop();

                            bottom = firstViewComingUp.getTop() + (int) firstViewComingUp.getTranslationY();

                        }



                        background.setBounds(left, top, right, bottom);

                        background.draw(c);



                    }

                    super.onDraw(c, parent, state);

                }



            });

        }



    @Override
    public void onDestroy() {
        super.onDestroy();
        helper.close();
    }

}
