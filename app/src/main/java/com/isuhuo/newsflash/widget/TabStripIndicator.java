package com.isuhuo.newsflash.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-08-28.
 */
public class TabStripIndicator extends LinearLayout {

    private static final float DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS = 0.5f;
    private static final byte DEFAULT_BOTTOM_BORDER_COLOR_ALPHA = 0x26;

    private static final int SELECTED_INDICATOR_THICKNESS_DIPS = 2;

    private static final float DEFAULT_DIVIDER_THICKNESS_DIPS = 0.5f;

    private static final int TAB_VIEW_PADDING_DIPS = 10;
    private static final int TAB_VIEW_TEXT_SIZE_SP = 14;

    private final int bottomBorderThickness;
    private final Paint bottomBorderPaint;

    private final int selectedIndicatorThickness;
    private final Paint selectedIndicatorPaint;

    private final Paint dividerPaint;
    private final float dividerHeight = 0.4f;

    private int selectedPosition;

    private boolean bottomBorderColorIsDefault = true;

    private boolean showDivider = false;

    private int tabViewLayoutId;
    private int tabViewTextViewId;

    private int textViewPosition;
    private int textViewSize;
    private int defaultTextColor;
    private int indicatorColor;
    private int dividerColor;

    private ViewPager viewPager;
    private ViewPager.OnPageChangeListener viewPagerPageChangeListener;

    public int getPosition() {
        return selectedPosition;
    }

    public TabStripIndicator(Context context) {
        this(context, null);
    }

    public TabStripIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);

        setWillNotDraw(false);

        final float density = getResources().getDisplayMetrics().density;

        TypedValue outValue = new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground, outValue, true);
        final int themeForegroundColor = outValue.data;

        final int bottomBorderColor = setColorAlpha(themeForegroundColor,
                DEFAULT_BOTTOM_BORDER_COLOR_ALPHA);

        bottomBorderThickness = (int) (DEFAULT_BOTTOM_BORDER_THICKNESS_DIPS * density);
        bottomBorderPaint = new Paint();
        bottomBorderPaint.setColor(bottomBorderColor);

        selectedIndicatorThickness = (int) (SELECTED_INDICATOR_THICKNESS_DIPS * density);
        selectedIndicatorPaint = new Paint();

        dividerPaint = new Paint();
        dividerPaint.setStrokeWidth((int) (DEFAULT_DIVIDER_THICKNESS_DIPS * density));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height = getHeight();
        final int childCount = getChildCount();
        // Thick colored underline below the current selection
        if (childCount > 0) {

            View selectedTitle = getChildAt(selectedPosition);
            int left = selectedTitle.getLeft();
            int right = selectedTitle.getRight();

            selectedIndicatorPaint.setColor(indicatorColor);

            canvas.drawRect(left, height - selectedIndicatorThickness, right,
                    height, selectedIndicatorPaint);
        }

        // Thin underline along the entire bottom edge
        if(!bottomBorderColorIsDefault){
            bottomBorderPaint.setColor(indicatorColor);
        }
        canvas.drawRect(0, height - bottomBorderThickness, getWidth(), height, bottomBorderPaint);

        if(showDivider){
            final int dividerHeightPx = (int) (Math.min(Math.max(0f, dividerHeight), 1f) * height);
            // 设置标题分隔符
            int separatorTop = (height - dividerHeightPx) / 2;
            for (int i = 0; i < childCount - 1; i++) {
                View child = getChildAt(i);
                if(dividerColor != 0){
                    dividerPaint.setColor(dividerColor);
                }
                canvas.drawLine(child.getRight(), separatorTop, child.getRight(),
                        separatorTop + dividerHeightPx, dividerPaint);
            }
        }
    }

    public List<Fragment> forAddFragment(int size, Class<?> cls) {
        List<Fragment> list = new ArrayList<Fragment>();
        for (int i = 0; i < size; i++) {
            try {
                Fragment f = (Fragment) cls.newInstance();
                list.add(f);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public void setIndicatorColor(int indicatorColor) {
        this.indicatorColor = indicatorColor;
    }

    public void setDividerColor(int dividerColor) {
        this.dividerColor = dividerColor;
    }

    public void setTextViewSize(int textViewSize) {
        this.textViewSize = textViewSize;
    }

    public void setDefaultTextColor(int defaultTextColor) {
        this.defaultTextColor = defaultTextColor;
    }

    public void setBottomBorderColorIsDefault(boolean bottomBorderColorIsDefault) {
        this.bottomBorderColorIsDefault = bottomBorderColorIsDefault;
    }

    public void setShowDivider(boolean showDivider) {
        this.showDivider = showDivider;
    }

    public void onViewPagerPageChanged(int position) {
        selectedPosition = position;
        invalidate();
    }

    protected TextView createDefaultTabView(Context context) {
        TextView textView = new TextView(context);
        textView.setGravity(Gravity.CENTER);
        if(textViewSize != 0){
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textViewSize);
        }else {
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, TAB_VIEW_TEXT_SIZE_SP);
        }
        if(defaultTextColor != 0 ){
            textView.setTextColor(defaultTextColor);
        }else {
            textView.setTextColor(Color.GRAY);
        }
        textView.setLayoutParams(new LinearLayout.LayoutParams(android.widget.LinearLayout.LayoutParams.WRAP_CONTENT,
                android.widget.LinearLayout.LayoutParams.WRAP_CONTENT, 1));

        int padding = (int) (TAB_VIEW_PADDING_DIPS * getResources().getDisplayMetrics().density);
        textView.setPadding(padding, padding, padding, padding);
        return textView;
    }

    private void populateTabStrip(PagerAdapter adapter) {
//        final PagerAdapter adapter = viewPager.getAdapter();
        final int count = adapter.getCount();
        if(count > 0){
            final View.OnClickListener tabClickListener = new TabClickListener();
            for (int i = 0; i < count; i++) {
                View tabView = null;
                TextView tabTitleView = null;

                if (tabViewLayoutId != 0) {
                    // If there is a custom tab view layout id set, try and inflate it
                    tabView = LayoutInflater.from(getContext()).inflate(tabViewLayoutId, this, false);
                    tabTitleView = (TextView) tabView.findViewById(tabViewTextViewId);
                }

                if (tabView == null) {
                    tabView = createDefaultTabView(getContext());
                }

                if (tabTitleView == null && TextView.class.isInstance(tabView)) {
                    tabTitleView = (TextView) tabView;
                }

                tabTitleView.setText(adapter.getPageTitle(i));
                tabView.setOnClickListener(tabClickListener);

                addView(tabView);
            }
        }
    }


    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        viewPagerPageChangeListener = listener;
    }

    /**
     * Set the custom layout to be inflated for the tab views.
     *
     * @param layoutResId Layout id to be inflated
     * @param textViewId id of the {@link TextView} in the inflated view
     */
    public void setCustomTabView(int layoutResId, int textViewId) {
        tabViewLayoutId = layoutResId;
        tabViewTextViewId = textViewId;
    }

    /**
     * Sets the associated view pager. Note that the assumption here is that the pager content
     * (number of tabs and tab titles) does not change after this call has been made.
     */
    public void setViewPager(ViewPager vp) {
        removeAllViews();
        viewPager = vp;
        if (viewPager != null) {
            viewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip(viewPager.getAdapter());
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        //设置标题选中的颜色同指示器颜色相同
        if(viewPager != null)
            ((TextView)getChildAt(viewPager.getCurrentItem())).setTextColor(indicatorColor);
    }
    /**
     * Set the alpha value of the {@code color} to be the given {@code alpha} value.
     */
    private int setColorAlpha(int color, byte alpha) {
        return Color.argb(alpha, Color.red(color), Color.green(color), Color.blue(color));
    }

    public class InternalViewPagerListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageSelected(int position) {
            onViewPagerPageChanged(position);

            if(defaultTextColor != 0){
                ((TextView)getChildAt(textViewPosition)).setTextColor(defaultTextColor);
            }else {
                ((TextView)getChildAt(textViewPosition)).setTextColor(Color.GRAY);
            }
            ((TextView) getChildAt(position)).setTextColor(indicatorColor);
            //先设置索引在交换位置
            textViewPosition = position;

            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageSelected(position);
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageScrolled(position, positionOffset,
                        positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (viewPagerPageChangeListener != null) {
                viewPagerPageChangeListener.onPageScrollStateChanged(state);
            }
        }

    }

    private class TabClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                if (v == getChildAt(i)) {
                    viewPager.setCurrentItem(i);
                    return;
                }
            }
        }
    }

}

