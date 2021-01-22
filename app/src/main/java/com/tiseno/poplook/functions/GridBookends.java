package com.tiseno.poplook.functions;

/**
 * Created by DEV POPLOOK 2 on 5/3/2016.
 */

        import androidx.annotation.NonNull;
        import androidx.recyclerview.widget.GridLayoutManager;
        import androidx.recyclerview.widget.RecyclerView;

        import com.tumblr.bookends.Bookends;

/**
 * An implementation of {@link Bookends} that accounts for spans in a {@link GridLayoutManager} when
 * creating headers and footers.
 *
 * This class performs a {@link GridLayoutManager.SpanSizeLookup} using either
 * the span count returned by the {@link GridLayoutManager} or a custom implementation. By default
 * headers and footers will fill the span count of the {@link GridLayoutManager}.
 *
 * Created by kevincoughlin on 5/21/15.
 */
public class GridBookends extends Bookends {
    /**
     * Constructor.
     *
     * @param base
     * 		the adapter to wrap
     */
    public GridBookends(RecyclerView.Adapter base) {
        super(base);
    }

    /**
     * Constructor.
     *
     * @param base
     * 		the adapter to wrap
     * @param gridLayoutManager
     * 		the grid layout manager to perform a span size lookup on
     */
    public GridBookends(final RecyclerView.Adapter base, @NonNull final GridLayoutManager gridLayoutManager) {
        this(base);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (position < getHeaderCount() || position >= getItemCount() - getFooterCount())
                        ? gridLayoutManager.getSpanCount()
                        : 1;
            }
        });
    }

    /**
     * Constructor.
     *
     * @param base
     * 		the adapter to wrap
     * @param gridLayoutManager
     * 		the grid layout manager to perform a span size lookup on
     * @param spanSizeLookup
     * 		the span size lookup to perform
     */
    public GridBookends(final RecyclerView.Adapter base, @NonNull final GridLayoutManager gridLayoutManager,
                        @NonNull GridLayoutManager.SpanSizeLookup spanSizeLookup) {
        this(base);
        gridLayoutManager.setSpanSizeLookup(spanSizeLookup);
    }
}