package com.tiseno.poplook.functions;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public abstract class  EndlessScrollListener extends RecyclerView.OnScrollListener {
    public static String TAG = EndlessScrollListener.class.getSimpleName();

    private static int firstVisibleLine;

    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 2; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;

    private int current_page = 1;

    private GridLayoutManager mLinearLayoutManager;

    public EndlessScrollListener(GridLayoutManager linearLayoutManager) {
        this.mLinearLayoutManager = linearLayoutManager;

    }


    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);

        System.out.println("new state = " + newState);

        if(recyclerView.SCROLL_STATE_DRAGGING==newState){

        }
        if(recyclerView.SCROLL_STATE_IDLE==newState){


        }
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        firstVisibleLine = dy;

        visibleItemCount = recyclerView.getChildCount();
        totalItemCount = mLinearLayoutManager.getItemCount();
        firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition();

        if (loading) {
            if (totalItemCount > previousTotal+2) {
                loading = false;
                previousTotal = totalItemCount;
            }
        }
        if (!loading && (totalItemCount - visibleItemCount)
                <= (firstVisibleItem + visibleThreshold)) {
            // End has been reached

            // Do something
            current_page++;

            onLoadMore(current_page);

            loading = true;
        }
        int visibility = (mLinearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0) ? View.VISIBLE : View.GONE;
//        buttonVisibility(visibility);
    }

    public abstract void onLoadMore(int current_page);



}