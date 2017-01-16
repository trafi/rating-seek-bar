package com.trafi.ratingseekbar;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.SeekBar;

import static android.view.MotionEvent.ACTION_CANCEL;
import static android.view.MotionEvent.ACTION_DOWN;
import static android.view.MotionEvent.ACTION_MOVE;
import static android.view.MotionEvent.ACTION_UP;

public class RatingSeekBar extends SeekBar {

    private final Paint paint;
    private final Paint textPaint;
    private final float textHeight;
    private final Path path;

    private boolean active;
    private float progressFraction;
    private float targetProgressFraction;
    private ObjectAnimator progressAnimator;

    private int width;
    private int height;
    private int radius;
    private int padding;
    private int widthMinusPadding;
    private int count;

    @ColorInt
    private int activeColor;
    @ColorInt
    private int inactiveColor;
    @ColorInt
    private int activeTextColor;
    @ColorInt
    private int inactiveTextColor;

    public RatingSeekBar(Context context) {
        this(context, null);
    }

    public RatingSeekBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatingSeekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        float textSize;
        TypedArray a = context.getTheme().obtainStyledAttributes(
                attrs,
                R.styleable.rsb_RatingSeekBar,
                defStyleAttr,
                0);

        try {
            activeColor =
                    a.getColor(R.styleable.rsb_RatingSeekBar_rsb_active_color, Color.RED);
            activeTextColor =
                    a.getColor(R.styleable.rsb_RatingSeekBar_rsb_active_text_color, Color.WHITE);
            inactiveColor =
                    a.getColor(R.styleable.rsb_RatingSeekBar_rsb_inactive_color, Color.GRAY);
            inactiveTextColor =
                    a.getColor(R.styleable.rsb_RatingSeekBar_rsb_inactive_text_color, Color.BLACK);
            textSize = a.getDimension(R.styleable.rsb_RatingSeekBar_rsb_text_size,
                    getResources().getDimensionPixelSize(R.dimen.rsb_default_label_text_size));
        } finally {
            a.recycle();
        }

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(textSize);
        textPaint.setTypeface(Typeface.DEFAULT_BOLD);

        Rect bounds = new Rect();
        textPaint.getTextBounds("1", 0, 1, bounds);
        textHeight = bounds.height();

        path = new Path();

        targetProgressFraction = (float) getProgress() / getMax();
        progressAnimator = ObjectAnimator.ofFloat(this, "progressFraction", 0f, 0f);
        progressAnimator.setInterpolator(new DecelerateInterpolator());
        progressAnimator.setDuration(100);

        active = false;
    }

    public void setProgress(int progress) {
        setTargetFraction((float) progress / getMax(), false);
    }

    @SuppressWarnings("unused")
    public float getProgressFraction() {
        return progressFraction;
    }

    @SuppressWarnings("unused")
    public void setProgressFraction(float progressFraction) {
        this.progressFraction = Math.max(0, Math.min(progressFraction, 1.f - 1.f / count));
        invalidate();
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int desiredHeight = getResources().getDimensionPixelSize(R.dimen.rsb_default_height);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        switch (heightMode) {
            case MeasureSpec.AT_MOST:
                desiredHeight = Math.min(desiredHeight, heightSize);
                // fall through
            case MeasureSpec.UNSPECIFIED:
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(desiredHeight, MeasureSpec.EXACTLY);
                break;
            case MeasureSpec.EXACTLY:
                break;
        }
        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
        radius = height / 2;
        padding = (int) ((float) (count * height - width) / (2 * (count - 1)));
        widthMinusPadding = width - 2 * padding;
        setPadding(padding, 0, padding, 0);
    }

    private void setTargetFraction(float newTargetProgressFraction, boolean animate) {
        active = true;
        if (newTargetProgressFraction != targetProgressFraction) {
            targetProgressFraction = newTargetProgressFraction;
            if (animate) {
                progressAnimator.setFloatValues(progressFraction, targetProgressFraction);
                progressAnimator.start();
            } else {
                setProgressFraction(targetProgressFraction);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case ACTION_DOWN:
                setTargetFraction(
                        (event.getX() - padding - widthMinusPadding / (count * 2))
                                / widthMinusPadding, true);
                break;
            case ACTION_MOVE:
                setTargetFraction(
                        (event.getX() - padding - widthMinusPadding / (count * 2))
                                / widthMinusPadding, false);
                break;
            case ACTION_UP:
            case ACTION_CANCEL:
                setTargetFraction((float) getProgress() / getMax(), true);
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public synchronized void setMax(int max) {
        super.setMax(max);
        count = max + 1;
    }

    @Override
    public synchronized int getMax() {
        return super.getMax() + 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // paint inactive track

        path.rewind();
        path.addCircle(radius, radius, radius, Path.Direction.CCW);
        path.addRect(radius, 0, width - radius, height, Path.Direction.CCW);
        path.addCircle(width - radius, radius, radius, Path.Direction.CCW);

        paint.setColor(inactiveColor);
        canvas.drawPath(path, paint);

        // paint active track

        float progressX = Math.min(radius + progressFraction * widthMinusPadding, width - radius);
        if (active) {
            path.rewind();
            path.addCircle(radius, radius, radius, Path.Direction.CCW);
            path.addRect(radius, 0, progressX, height, Path.Direction.CCW);
            path.addCircle(progressX, radius, radius, Path.Direction.CCW);

            paint.setColor(activeColor);
            canvas.drawPath(path, paint);
        }

        // paint masked labels
        // use simpler clipping operations on older API levels
        // https://developer.android.com/guide/topics/graphics/hardware-accel.html#unsupported
        textPaint.setColor(inactiveTextColor);
        canvas.save();
        if (active) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                canvas.clipPath(path, Region.Op.DIFFERENCE);
            } else {
                canvas.clipRect(progressX + radius, 0, width, height, Region.Op.INTERSECT);
            }
        }
        drawLabels(canvas, count);
        canvas.restore();

        if (active) {
            textPaint.setColor(activeTextColor);
            canvas.save();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                canvas.clipPath(path, Region.Op.INTERSECT);
            } else {
                canvas.clipRect(0, 0, progressX + radius, height, Region.Op.INTERSECT);
            }
            drawLabels(canvas, count);
            canvas.restore();
        }
    }

    private void drawLabels(Canvas canvas, int count) {
        float stepWidth = widthMinusPadding / count;
        for (int i = 0; i < count; i++) {
            canvas.drawText(
                    Integer.toString(i),
                    padding + i * stepWidth + stepWidth / 2,
                    height / 2 + textHeight / 2,
                    textPaint);
        }
    }
}
