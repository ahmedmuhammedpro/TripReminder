package com.example.tripreminder.view.adapters;


        import android.content.Context;
        import android.graphics.Canvas;
        import android.graphics.Color;
        import android.graphics.Paint;
        import android.graphics.PorterDuff;
        import android.graphics.PorterDuffXfermode;
        import android.graphics.drawable.ColorDrawable;
        import android.graphics.drawable.Drawable;

        import android.view.View;
        import android.widget.TextView;

        import com.example.tripreminder.R;

        import androidx.annotation.NonNull;
        import androidx.core.content.ContextCompat;
        import androidx.recyclerview.widget.ItemTouchHelper;
        import androidx.recyclerview.widget.RecyclerView;

abstract public class SwipeToEditCallBack extends ItemTouchHelper.Callback {

    Context mContext;
    private Paint mClearPaint;
    private ColorDrawable mBackground;
    private int backgroundColor;
    private Drawable editDrawable;
    private int intrinsicWidth;
    private int intrinsicHeight;
    private TextView edit;


    public SwipeToEditCallBack(Context context) {
        mContext = context;
        mBackground = new ColorDrawable();
        backgroundColor = Color.parseColor("#c0cfff");
        mClearPaint = new Paint();
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.ADD));
        editDrawable = ContextCompat.getDrawable(mContext, R.drawable.ic_rename);
        edit = new TextView(mContext);
        intrinsicWidth = editDrawable.getIntrinsicWidth();
        intrinsicHeight = editDrawable.getIntrinsicHeight();


    }


    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(0, ItemTouchHelper.RIGHT);
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        View itemView = viewHolder.itemView;
        int itemHeight = itemView.getHeight();

        boolean isCancelled = dX == 0 && !isCurrentlyActive;

        if (isCancelled) {
            clearCanvas(c, itemView.getRight() + dX, (float) itemView.getTop(), (float) itemView.getRight(), (float) itemView.getBottom());
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
            return;
        }

        mBackground.setColor(backgroundColor);
        mBackground.setBounds(itemView.getLeft() , itemView.getTop(), itemView.getLeft() + (int) dX, itemView.getBottom());
        mBackground.draw(c);

        int editIconTop = itemView.getTop() + (itemHeight - intrinsicHeight) / 2;
        int editIconMargin = (itemHeight - intrinsicHeight) / 2;
        int editIconLeft = itemView.getLeft() - editIconMargin - intrinsicWidth;
        int editIconRight = itemView.getLeft() - editIconMargin;
        int editIconBottom = editIconTop + intrinsicHeight;


        editDrawable.setBounds(editIconLeft, editIconTop, editIconRight, editIconBottom);
       // editDrawable.draw(c);
        editDrawable.setBounds(editIconLeft - 20, editIconTop, editIconRight, editIconBottom);
        edit.setText("Edit");
        edit.draw(c);
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);


    }

    private void clearCanvas(Canvas c, Float left, Float top, Float right, Float bottom) {
        c.drawRect(left, top, right, bottom, mClearPaint);

    }

    @Override
    public float getSwipeThreshold(@NonNull RecyclerView.ViewHolder viewHolder) {
        return 0.7f;
    }
}

