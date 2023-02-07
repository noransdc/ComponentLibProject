package com.intertive.tab;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.intertive.tab.listener.OnTabSelectListener;
import com.intertive.tab.utils.InterMsgUtils;
import com.intertive.tab.widget.InterMsgView;
import com.intertive.tablayout.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 滑动TabLayout,对于ViewPager的依赖性强
 * https://github.com/H07000223/FlycoTabLayout
 */
public class InterTabLayout extends HorizontalScrollView implements ViewPager.OnPageChangeListener {

    private final Context mContext;
    private ViewPager mViewPager;
    private List<InterTabTitle> mTitleList = new ArrayList<>();
    private final LinearLayout mTabsContainer;
    private int mCurrentTab;
    private float mCurrentPositionOffset;
    private int mTabCount;
    /**
     * 用于绘制显示器
     */
    private final Rect mIndicatorRect = new Rect();
    /**
     * 用于实现滚动居中
     */
    private final Rect mTabRect = new Rect();

    private final Paint mRectPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mDividerPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint mTrianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Path mTrianglePath = new Path();
    private static final int STYLE_NORMAL = 0;
    private static final int STYLE_TRIANGLE = 1;
    private static final int STYLE_BLOCK = 2;
    private int mIndicatorStyle = STYLE_NORMAL;

    private float mTabPaddingWidth;
    private boolean mTabSpaceEqual;
    private float mTabWidth;

    /**
     * indicator
     */
    private int mIndicatorColor;
    private float mIndicatorHeight;
    private float mIndicatorWidth;
    private float mIndicatorCornerRadius;
    private float mIndicatorMarginLeft;
    private float mIndicatorMarginTop;
    private float mIndicatorMarginRight;
    private float mIndicatorMarginBottom;
    private int mIndicatorGravity;
    private boolean mIndicatorWidthEqualTitle;
    private final GradientDrawable mIndicatorDrawable = new GradientDrawable();
    private Drawable indicatorDrawableRes;

    /**
     * underline
     */
    private int mUnderlineColor;
    private float mUnderlineHeight;
    private int mUnderlineGravity;

    /**
     * divider
     */
    private int mDividerColor;
    private float mDividerWidth;
    private float mDividerPadding;

    /**
     * title
     */
    private static final int TEXT_BOLD_NONE = 0;
    private static final int TEXT_BOLD_WHEN_SELECT = 1;
    private static final int TEXT_BOLD_BOTH = 2;
//    private float mTextSize, mTextSize2;
    private float mTextSizeSelect, mTextSizeUnselect, mTextSizeSelect2, mTextSizeUnselect2;
    private int mTextSelectColor, mTextUnselectColor, mTextSelectColor2, mTextUnselectColor2;
    private int mTextBold, mTextBold2;
    private boolean mTextAllCaps, mTextScale, mTextAllCaps2, mTextScale2;
    private float mTextScaleValue, mTextScaleValue2;
    private int textPaddingLeft, textPaddingRight, textPaddingTop, textPaddingBottom;
    private int textPaddingLeft2, textPaddingRight2, textPaddingTop2, textPaddingBottom2;
    private int textBackground;

    private int mLastScrollX;
    private int mHeight;
    private boolean mSnapOnTabClick;

    private Typeface titleTypeFace2;


    public InterTabLayout(Context context) {
        this(context, null, 0);
    }

    public InterTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public InterTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setFillViewport(true);//设置滚动视图是否可以伸缩其内容以填充视口
        setWillNotDraw(false);//重写onDraw方法,需要调用这个方法来清除flag
        setClipChildren(false);
        setClipToPadding(false);
        setOverScrollMode(OVER_SCROLL_NEVER);

        this.mContext = context;
        mTabsContainer = new LinearLayout(context);
        addView(mTabsContainer);

        obtainAttributes(context, attrs);

        //get layout_height
        String height = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "layout_height");
        if (!height.equals(ViewGroup.LayoutParams.MATCH_PARENT + "") && !height.equals(ViewGroup.LayoutParams.WRAP_CONTENT + "")) {
            int[] systemAttrs = {android.R.attr.layout_height};
            TypedArray a = context.obtainStyledAttributes(attrs, systemAttrs);
            mHeight = a.getDimensionPixelSize(0, ViewGroup.LayoutParams.WRAP_CONTENT);
            a.recycle();
        }
    }

    private void obtainAttributes(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.InterTabLayout);

        mIndicatorStyle = ta.getInt(R.styleable.InterTabLayout_tl_inter_indicator_style, STYLE_NORMAL);
        mIndicatorColor = ta.getColor(R.styleable.InterTabLayout_tl_inter_indicator_color, Color.parseColor(mIndicatorStyle == STYLE_BLOCK ? "#4B6A87" : "#ffffff"));
        mIndicatorHeight = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_height,
                dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 4 : (mIndicatorStyle == STYLE_BLOCK ? -1 : 2)));
        mIndicatorWidth = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_width, dp2px(mIndicatorStyle == STYLE_TRIANGLE ? 10 : -1));
        mIndicatorCornerRadius = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_corner_radius, dp2px(mIndicatorStyle == STYLE_BLOCK ? -1 : 0));
        mIndicatorMarginLeft = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_margin_left, dp2px(0));
        mIndicatorMarginTop = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_margin_top, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorMarginRight = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_margin_right, dp2px(0));
        mIndicatorMarginBottom = ta.getDimension(R.styleable.InterTabLayout_tl_inter_indicator_margin_bottom, dp2px(mIndicatorStyle == STYLE_BLOCK ? 7 : 0));
        mIndicatorGravity = ta.getInt(R.styleable.InterTabLayout_tl_inter_indicator_gravity, Gravity.BOTTOM);
        mIndicatorWidthEqualTitle = ta.getBoolean(R.styleable.InterTabLayout_tl_inter_indicator_width_equal_title, false);
        indicatorDrawableRes = ta.getDrawable(R.styleable.InterTabLayout_tl_inter_indicator_drawable);

        mUnderlineColor = ta.getColor(R.styleable.InterTabLayout_tl_inter_underline_color, Color.parseColor("#ffffff"));
        mUnderlineHeight = ta.getDimension(R.styleable.InterTabLayout_tl_inter_underline_height, dp2px(0));
        mUnderlineGravity = ta.getInt(R.styleable.InterTabLayout_tl_inter_underline_gravity, Gravity.BOTTOM);

        mDividerColor = ta.getColor(R.styleable.InterTabLayout_tl_inter_divider_color, Color.parseColor("#ffffff"));
        mDividerWidth = ta.getDimension(R.styleable.InterTabLayout_tl_inter_divider_width, dp2px(0));
        mDividerPadding = ta.getDimension(R.styleable.InterTabLayout_tl_inter_divider_padding, dp2px(12));

        //title
//        mTextSize = ta.getDimension(R.styleable.InterTabLayout_tl_inter_text_size, sp2px(14));
        mTextSizeSelect = ta.getDimension(R.styleable.InterTabLayout_tl_inter_text_size_select, sp2px(14));
        mTextSizeUnselect = ta.getDimension(R.styleable.InterTabLayout_tl_inter_text_size_unselect, sp2px(14));
        mTextSelectColor = ta.getColor(R.styleable.InterTabLayout_tl_inter_text_select_color, Color.parseColor("#ffffff"));
        mTextUnselectColor = ta.getColor(R.styleable.InterTabLayout_tl_inter_text_unselect_color, Color.parseColor("#AAffffff"));
        mTextScale = ta.getBoolean(R.styleable.InterTabLayout_tl_inter_text_select_scale, false);
        mTextScaleValue = ta.getFloat(R.styleable.InterTabLayout_tl_inter_text_select_scale_value, 1.1f);
        mTextBold = ta.getInt(R.styleable.InterTabLayout_tl_inter_text_bold, TEXT_BOLD_NONE);
        mTextAllCaps = ta.getBoolean(R.styleable.InterTabLayout_tl_inter_text_all_caps, false);
        textPaddingLeft = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_left, 0);
        textPaddingTop = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_top, 0);
        textPaddingRight = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_right, 0);
        textPaddingBottom = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_bottom, 0);
        textBackground = ta.getResourceId(R.styleable.InterTabLayout_tl_inter_text_background, 0);

        //title2
//        mTextSize2 = ta.getDimension(R.styleable.InterTabLayout_tl_inter_text_size2, sp2px(14));
        mTextSizeSelect2 = ta.getDimension(R.styleable.InterTabLayout_tl_inter_text_size_select2, sp2px(14));
        mTextSizeUnselect2 = ta.getDimension(R.styleable.InterTabLayout_tl_inter_text_size_unselect2, sp2px(14));
        mTextSelectColor2 = ta.getColor(R.styleable.InterTabLayout_tl_inter_text_select_color2, Color.parseColor("#ffffff"));
        mTextUnselectColor2 = ta.getColor(R.styleable.InterTabLayout_tl_inter_text_unselect_color2, Color.parseColor("#AAffffff"));
        mTextBold2 = ta.getInt(R.styleable.InterTabLayout_tl_inter_text_bold2, TEXT_BOLD_NONE);
        mTextAllCaps2 = ta.getBoolean(R.styleable.InterTabLayout_tl_inter_text_all_caps2, false);
        mTextScale2 = ta.getBoolean(R.styleable.InterTabLayout_tl_inter_text_select_scale2, false);
        mTextScaleValue2 = ta.getFloat(R.styleable.InterTabLayout_tl_inter_text_select_scale_value2, 1.1f);
        textPaddingLeft2 = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_left2, 0);
        textPaddingTop2 = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_top2, 0);
        textPaddingRight2 = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_right2, 0);
        textPaddingBottom2 = ta.getDimensionPixelSize(R.styleable.InterTabLayout_tl_inter_text_padding_bottom2, 0);

        mTabSpaceEqual = ta.getBoolean(R.styleable.InterTabLayout_tl_inter_tab_space_equal, false);
        mTabWidth = ta.getDimension(R.styleable.InterTabLayout_tl_inter_tab_width, dp2px(-1));
        mTabPaddingWidth = ta.getDimension(R.styleable.InterTabLayout_tl_inter_tab_padding_width, mTabSpaceEqual || mTabWidth > 0 ? dp2px(0) : dp2px(20));

        ta.recycle();
    }

    /**
     * 关联ViewPager,用于不想在ViewPager适配器中设置titles数据的情况
     */
    public void setViewPagerTab(ViewPager vp, List<InterTabTitle> titleList) {
        if (vp == null || vp.getAdapter() == null) {
            throw new IllegalStateException("ViewPager or ViewPager adapter can not be NULL !");
        }

        if (titleList == null || titleList.isEmpty()) {
            throw new IllegalStateException("Titles can not be EMPTY !");
        }

        if (titleList.size() != vp.getAdapter().getCount()) {
            throw new IllegalStateException("Titles length must be the same as the page count !");
        }

        this.mViewPager = vp;
//        this.mTitleList = titleList;
        mTitleList.clear();
        mTitleList.addAll(titleList);

        this.mViewPager.removeOnPageChangeListener(this);
        this.mViewPager.addOnPageChangeListener(this);
        notifyDataSetChanged();
    }

    public void setViewPager(ViewPager vp, List<String> titles) {
        List<InterTabTitle> list = new ArrayList<>();
        for (String title : titles) {
            list.add(new InterTabTitle(title));
        }
        setViewPagerTab(vp, list);
    }

    public void setViewPager(ViewPager vp, String[] titles) {
        List<InterTabTitle> list = new ArrayList<>();
        for (String title : titles) {
            list.add(new InterTabTitle(title));
        }
        setViewPagerTab(vp, list);
    }

    public void updateTitleText(List<InterTabTitle> titleList) {
        if (titleList == null || titleList.isEmpty() || mTitleList == null || mTabsContainer == null) {
            return;
        }
        mTitleList.clear();
        mTitleList.addAll(titleList);

        int childCount = mTabsContainer.getChildCount();
        for (int i = 0; i < childCount; i++) {
            View tabLayout = mTabsContainer.getChildAt(i);
            if (tabLayout != null) {
                TextView textView1 = tabLayout.findViewById(R.id.tv_tab_title);
                TextView textView2 = tabLayout.findViewById(R.id.tv_tab_title2);
                if (i < titleList.size()) {
                    InterTabTitle title = titleList.get(i);
                    if (title != null) {
                        if (textView1 != null) {
                            if (!TextUtils.isEmpty(title.getTitle1())) {
                                textView1.setText(title.getTitle1());
                            } else {
                                textView1.setText("");
                            }
                        }
                        if (textView2 != null) {
                            if (!TextUtils.isEmpty(title.getTitle2())) {
                                textView2.setText(title.getTitle2());
                            } else {
                                textView2.setText("");
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 更新数据
     */
    public void notifyDataSetChanged() {
        mTabsContainer.removeAllViews();
        PagerAdapter pagerAdapter = mViewPager.getAdapter();
        if (pagerAdapter == null || mTitleList == null) {
            return;
        }
        this.mTabCount = pagerAdapter.getCount();
        View tabLayout;
        for (int i = 0; i < mTabCount; i++) {
            tabLayout = View.inflate(mContext, R.layout.layout_tab, null);
            String title1, title2;
            if (mTitleList != null && i < mTitleList.size()) {
                InterTabTitle interTabTitle = mTitleList.get(i);
                if (interTabTitle != null) {
                    title1 = interTabTitle.getTitle1();
                    title2 = interTabTitle.getTitle2();
                } else {
                    title1 = "";
                    title2 = "";
                }
            } else {
                title1 = "";
                title2 = "";
            }
            addTab(i, title1, title2, tabLayout);
        }
        updateTabStyles();
    }

    public void addNewTab(String title) {
        if (mTitleList == null) {
            return;
        }

        View tabView = View.inflate(mContext, R.layout.layout_tab, null);
        mTitleList.add(new InterTabTitle(title));

        if (mTabCount >= mTitleList.size()) {
            mTabCount = mTitleList.size() - 1;
        }
        InterTabTitle interTabTitle = mTitleList.get(mTabCount);
        if (interTabTitle == null) {
            addTab(mTabCount, "", "", tabView);
        } else {
            addTab(mTabCount, interTabTitle.getTitle1(), interTabTitle.getTitle2(), tabView);
        }
        this.mTabCount = mTitleList.size();
        updateTabStyles();
    }

    /**
     * 创建并添加tab
     */
    private void addTab(final int position, String title1, String title2, View tabLayout) {
        TextView textView1 = (TextView) tabLayout.findViewById(R.id.tv_tab_title);
        TextView textView2 = tabLayout.findViewById(R.id.tv_tab_title2);
        if (textView1 != null && !TextUtils.isEmpty(title1)) {
            textView1.setText(title1);
            textView1.setSelected(position == mCurrentTab);
        }
        if (textView2 != null) {
            if (!TextUtils.isEmpty(title2)) {
                textView2.setVisibility(VISIBLE);
                textView2.setText(title2);
                textView2.setSelected(position == mCurrentTab);
            } else {
                textView2.setVisibility(GONE);
            }
        }
        tabLayout.setOnClickListener(view -> {
            int position1 = mTabsContainer.indexOfChild(view);
            if (position1 != -1) {
                if (mViewPager.getCurrentItem() != position1) {
                    if (mSnapOnTabClick) {
                        mViewPager.setCurrentItem(position1, false);
                    } else {
                        mViewPager.setCurrentItem(position1);
                    }

                    if (mListener != null) {
                        mListener.onTabSelect(position1);
                    }
                } else {
                    if (mListener != null) {
                        mListener.onTabReselect(position1);
                    }
                }
            }
        });

        // 每一个Tab的布局参数 */
        LinearLayout.LayoutParams layoutParams = mTabSpaceEqual ?
                new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f) :
                new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        if (mTabWidth > 0) {
            layoutParams = new LinearLayout.LayoutParams((int) mTabWidth, LayoutParams.MATCH_PARENT);
        }

        mTabsContainer.addView(tabLayout, position, layoutParams);
    }

    private void updateTabStyles() {
        for (int i = 0; i < mTabCount; i++) {
            View v = mTabsContainer.getChildAt(i);
            v.setPadding((int) mTabPaddingWidth, v.getPaddingTop(), (int) mTabPaddingWidth, v.getPaddingBottom());
            TextView textView1 = (TextView) v.findViewById(R.id.tv_tab_title);
            TextView textView2 = (TextView) v.findViewById(R.id.tv_tab_title2);

            boolean isSelect = i == mCurrentTab;

            if (textView1 != null) {
                textView1.setTextColor(i == mCurrentTab ? mTextSelectColor : mTextUnselectColor);
                if (isSelect){
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeSelect);
                } else {
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeUnselect);
                }
                textView1.setPadding(textPaddingLeft, textPaddingTop, textPaddingRight, textPaddingBottom);
                textView1.setBackgroundResource(textBackground);
                if (mTextAllCaps) {
                    textView1.setText(textView1.getText().toString().toUpperCase());
                }
                if (mTextBold == TEXT_BOLD_BOTH) {
                    textView1.getPaint().setFakeBoldText(true);
                } else if (mTextBold == TEXT_BOLD_NONE) {
                    textView1.getPaint().setFakeBoldText(false);
                }
            }
            if (textView2 != null) {
                textView2.setTextColor(i == mCurrentTab ? mTextSelectColor2 : mTextUnselectColor2);
                if (isSelect){
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeSelect2);
                } else {
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeUnselect2);
                }
                textView2.setPadding(textPaddingLeft2, textPaddingTop2, textPaddingRight2, textPaddingBottom2);
                textView2.setBackgroundResource(textBackground);
                if (mTextAllCaps2) {
                    textView2.setText(textView2.getText().toString().toUpperCase());
                }
                if (mTextBold2 == TEXT_BOLD_BOTH) {
                    textView2.getPaint().setFakeBoldText(true);
                } else if (mTextBold2 == TEXT_BOLD_NONE) {
                    textView2.getPaint().setFakeBoldText(false);
                }
                if (titleTypeFace2 != null){
                    textView2.setTypeface(titleTypeFace2);
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        //position:当前View的位置
        //mCurrentPositionOffset:当前View的偏移量比例.[0,1)
        this.mCurrentTab = position;
        this.mCurrentPositionOffset = positionOffset;
        scrollToCurrentTab();
        invalidate();
    }

    @Override
    public void onPageSelected(int position) {
        updateTabSelection(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    /**
     * HorizontalScrollView滚到当前tab,并且居中显示
     */
    private void scrollToCurrentTab() {
        if (mTabCount <= 0) {
            return;
        }

        int offset = (int) (mCurrentPositionOffset * mTabsContainer.getChildAt(mCurrentTab).getWidth());
        //当前Tab的left+当前Tab的Width乘以positionOffset*/
        int newScrollX = mTabsContainer.getChildAt(mCurrentTab).getLeft() + offset;

        if (mCurrentTab > 0 || offset > 0) {
            //HorizontalScrollView移动到当前tab,并居中*/
            newScrollX -= getWidth() / 2 - getPaddingLeft();
            calcIndicatorRect();
            newScrollX += ((mTabRect.right - mTabRect.left) / 2);
        }

        if (newScrollX != mLastScrollX) {
            mLastScrollX = newScrollX;
            //scrollTo（int x,int y）:x,y代表的不是坐标点,而是偏移量
            //x:表示离起始位置的x水平方向的偏移量
            //y:表示离起始位置的y垂直方向的偏移量
            scrollTo(newScrollX, 0);
        }
    }

    private void updateTabSelection(int position) {
        for (int i = 0; i < mTabCount; ++i) {
            View tabLayout = mTabsContainer.getChildAt(i);
            final boolean isSelect = i == position;
            TextView textView1 = (TextView) tabLayout.findViewById(R.id.tv_tab_title);
            TextView textView2 = (TextView) tabLayout.findViewById(R.id.tv_tab_title2);

            if (textView1 != null) {
                textView1.setTextColor(isSelect ? mTextSelectColor : mTextUnselectColor);
                textView1.setSelected(isSelect);
                if (mTextBold == TEXT_BOLD_WHEN_SELECT) {
                    textView1.getPaint().setFakeBoldText(isSelect);
                }
                if (mTextScale) {
                    if (isSelect) {
                        textView1.setScaleY(mTextScaleValue);
                        textView1.setScaleX(mTextScaleValue);
                    } else {
                        textView1.setScaleY(1);
                        textView1.setScaleX(1);
                    }
                }
                if (isSelect){
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeSelect);
                } else {
                    textView1.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeUnselect);
                }
            }
            if (textView2 != null) {
                textView2.setTextColor(isSelect ? mTextSelectColor2 : mTextUnselectColor2);
                textView2.setSelected(isSelect);
                if (mTextBold2 == TEXT_BOLD_WHEN_SELECT) {
                    textView2.getPaint().setFakeBoldText(isSelect);
                }
                if (mTextScale2) {
                    if (isSelect) {
                        textView2.setScaleY(mTextScaleValue2);
                        textView2.setScaleX(mTextScaleValue2);

                    } else {
                        textView2.setScaleY(1);
                        textView2.setScaleX(1);
                    }
                }
                if (isSelect){
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeSelect2);
                } else {
                    textView2.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTextSizeUnselect2);
                }
            }
        }

    }

    private float margin;

    private void calcIndicatorRect() {
        View currentTabView = mTabsContainer.getChildAt(this.mCurrentTab);
        float left = currentTabView.getLeft();
        float right = currentTabView.getRight();

        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            TextView textView = (TextView) currentTabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextSizeUnselect);
            float textWidth = mTextPaint.measureText(textView.getText().toString());
            margin = (right - left - textWidth) / 2;
        }

        if (this.mCurrentTab < mTabCount - 1) {
            View nextTabView = mTabsContainer.getChildAt(this.mCurrentTab + 1);
            float nextTabLeft = nextTabView.getLeft();
            float nextTabRight = nextTabView.getRight();

            left = left + mCurrentPositionOffset * (nextTabLeft - left);
            right = right + mCurrentPositionOffset * (nextTabRight - right);

            //for mIndicatorWidthEqualTitle
            if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
                TextView next_tab_title = (TextView) nextTabView.findViewById(R.id.tv_tab_title);
                mTextPaint.setTextSize(mTextSizeUnselect);
                float nextTextWidth = mTextPaint.measureText(next_tab_title.getText().toString());
                float nextMargin = (nextTabRight - nextTabLeft - nextTextWidth) / 2;
                margin = margin + mCurrentPositionOffset * (nextMargin - margin);
            }
        }

        mIndicatorRect.left = (int) left;
        mIndicatorRect.right = (int) right;
        //for mIndicatorWidthEqualTitle
        if (mIndicatorStyle == STYLE_NORMAL && mIndicatorWidthEqualTitle) {
            mIndicatorRect.left = (int) (left + margin - 1);
            mIndicatorRect.right = (int) (right - margin - 1);
        }

        mTabRect.left = (int) left;
        mTabRect.right = (int) right;

        //indicatorWidth小于0时,原jpardogo's PagerSlidingTabStrip
        //indicatorWidth大于0时,圆角矩形以及三角形
        if (mIndicatorWidth >= 0) {
            float indicatorLeft = currentTabView.getLeft() + (currentTabView.getWidth() - mIndicatorWidth) / 2;

            if (this.mCurrentTab < mTabCount - 1) {
                View nextTab = mTabsContainer.getChildAt(this.mCurrentTab + 1);
                indicatorLeft = indicatorLeft + mCurrentPositionOffset * (currentTabView.getWidth() / 2f + nextTab.getWidth() / 2f);
            }

            mIndicatorRect.left = (int) indicatorLeft;
            mIndicatorRect.right = (int) (mIndicatorRect.left + mIndicatorWidth);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isInEditMode() || mTabCount <= 0) {
            return;
        }

        int height = getHeight();
        int paddingLeft = getPaddingLeft();
        // draw divider
        if (mDividerWidth > 0) {
            mDividerPaint.setStrokeWidth(mDividerWidth);
            mDividerPaint.setColor(mDividerColor);
            for (int i = 0; i < mTabCount - 1; i++) {
                View tab = mTabsContainer.getChildAt(i);
                canvas.drawLine(paddingLeft + tab.getRight(), mDividerPadding, paddingLeft + tab.getRight(), height - mDividerPadding, mDividerPaint);
            }
        }

        // draw underline
        if (mUnderlineHeight > 0) {
            mRectPaint.setColor(mUnderlineColor);
            if (mUnderlineGravity == Gravity.BOTTOM) {
                canvas.drawRect(paddingLeft, height - mUnderlineHeight, mTabsContainer.getWidth() + paddingLeft, height, mRectPaint);
            } else {
                canvas.drawRect(paddingLeft, 0, mTabsContainer.getWidth() + paddingLeft, mUnderlineHeight, mRectPaint);
            }
        }

        //draw indicator line
        calcIndicatorRect();
        switch (mIndicatorStyle) {
            case STYLE_TRIANGLE:
                if (mIndicatorHeight > 0) {
                    mTrianglePaint.setColor(mIndicatorColor);
                    mTrianglePath.reset();
                    mTrianglePath.moveTo(paddingLeft + mIndicatorRect.left, height);
                    mTrianglePath.lineTo(paddingLeft + mIndicatorRect.left / 2f + mIndicatorRect.right / 2f, height - mIndicatorHeight);
                    mTrianglePath.lineTo(paddingLeft + mIndicatorRect.right, height);
                    mTrianglePath.close();
                    canvas.drawPath(mTrianglePath, mTrianglePaint);
                }
                break;

            case STYLE_BLOCK:
                if (mIndicatorHeight < 0) {
                    mIndicatorHeight = height - mIndicatorMarginTop - mIndicatorMarginBottom;
                }

                if (mIndicatorHeight > 0) {
                    if (mIndicatorCornerRadius < 0 || mIndicatorCornerRadius > mIndicatorHeight / 2) {
                        mIndicatorCornerRadius = mIndicatorHeight / 2;
                    }

                    int left = paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left;
                    int top = (int) mIndicatorMarginTop;
                    int right = (int) (paddingLeft + mIndicatorRect.right - mIndicatorMarginRight);
                    int bottom = (int) (mIndicatorMarginTop + mIndicatorHeight);

                    if (indicatorDrawableRes != null) {
                        indicatorDrawableRes.setBounds(left, top, right, bottom);
                        indicatorDrawableRes.draw(canvas);

                    } else {
                        mIndicatorDrawable.setColor(mIndicatorColor);
                        mIndicatorDrawable.setBounds(left, top, right, bottom);
                        mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                        mIndicatorDrawable.draw(canvas);
                    }
                }
                break;

            default:
                if (mIndicatorHeight > 0) {
                    mIndicatorDrawable.setColor(mIndicatorColor);

                    int left, top, right, bottom;

                    if (mIndicatorGravity == Gravity.BOTTOM) {
                        left = paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left;
                        top = height - (int) mIndicatorHeight - (int) mIndicatorMarginBottom;
                        right = paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight;
                        bottom = height - (int) mIndicatorMarginBottom;

                    } else {
                        left = paddingLeft + (int) mIndicatorMarginLeft + mIndicatorRect.left;
                        top = (int) mIndicatorMarginTop;
                        right = paddingLeft + mIndicatorRect.right - (int) mIndicatorMarginRight;
                        bottom = (int) mIndicatorHeight + (int) mIndicatorMarginTop;
                    }

                    if (indicatorDrawableRes != null) {
                        indicatorDrawableRes.setBounds(left, top, right, bottom);
                        indicatorDrawableRes.draw(canvas);
                    } else {
                        mIndicatorDrawable.setBounds(left, top, right, bottom);
                        mIndicatorDrawable.setCornerRadius(mIndicatorCornerRadius);
                        mIndicatorDrawable.draw(canvas);
                    }
                }
                break;
        }

    }

    //setter and getter
    public void setCurrentTab(int currentTab) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab);
        updateTabStyles();

    }

    public void setCurrentTab(int currentTab, boolean smoothScroll) {
        this.mCurrentTab = currentTab;
        mViewPager.setCurrentItem(currentTab, smoothScroll);
    }

    public void setIndicatorStyle(int indicatorStyle) {
        this.mIndicatorStyle = indicatorStyle;
        invalidate();
    }

    public void setTabPadding(float tabPadding) {
        this.mTabPaddingWidth = dp2px(tabPadding);
        updateTabStyles();
    }

    public void setTabSpaceEqual(boolean tabSpaceEqual) {
        this.mTabSpaceEqual = tabSpaceEqual;
        updateTabStyles();
    }

    public void setTabWidth(float tabWidth) {
        this.mTabWidth = dp2px(tabWidth);
        updateTabStyles();
    }

    public void setIndicatorColor(int indicatorColor) {
        this.mIndicatorColor = indicatorColor;
        invalidate();
    }

    public void setIndicatorHeight(float indicatorHeight) {
        this.mIndicatorHeight = dp2px(indicatorHeight);
        invalidate();
    }

    public void setIndicatorWidth(float indicatorWidth) {
        this.mIndicatorWidth = dp2px(indicatorWidth);
        invalidate();
    }

    public void setIndicatorCornerRadius(float indicatorCornerRadius) {
        this.mIndicatorCornerRadius = dp2px(indicatorCornerRadius);
        invalidate();
    }

    public void setIndicatorGravity(int indicatorGravity) {
        this.mIndicatorGravity = indicatorGravity;
        invalidate();
    }

    public void setIndicatorMargin(float indicatorMarginLeft, float indicatorMarginTop,
                                   float indicatorMarginRight, float indicatorMarginBottom) {
        this.mIndicatorMarginLeft = dp2px(indicatorMarginLeft);
        this.mIndicatorMarginTop = dp2px(indicatorMarginTop);
        this.mIndicatorMarginRight = dp2px(indicatorMarginRight);
        this.mIndicatorMarginBottom = dp2px(indicatorMarginBottom);
        invalidate();
    }

    public void setIndicatorWidthEqualTitle(boolean indicatorWidthEqualTitle) {
        this.mIndicatorWidthEqualTitle = indicatorWidthEqualTitle;
        invalidate();
    }

    public void setIndicatorDrawable(Drawable drawable){
        indicatorDrawableRes = drawable;
        invalidate();
    }

    public void setTitleTypeFace2(Typeface typeface){
        this.titleTypeFace2 = typeface;
    }

    public void setUnderlineColor(int underlineColor) {
        this.mUnderlineColor = underlineColor;
        invalidate();
    }

    public void setUnderlineHeight(float underlineHeight) {
        this.mUnderlineHeight = dp2px(underlineHeight);
        invalidate();
    }

    public void setUnderlineGravity(int underlineGravity) {
        this.mUnderlineGravity = underlineGravity;
        invalidate();
    }

    public void setDividerColor(int dividerColor) {
        this.mDividerColor = dividerColor;
        invalidate();
    }

    public void setDividerWidth(float dividerWidth) {
        this.mDividerWidth = dp2px(dividerWidth);
        invalidate();
    }

    public void setDividerPadding(float dividerPadding) {
        this.mDividerPadding = dp2px(dividerPadding);
        invalidate();
    }

//    public void setTextSize(float textSize) {
//        this.mTextSize = sp2px(textSize);
//        updateTabStyles();
//    }

    public void setTextSelectColor(int textSelectColor) {
        this.mTextSelectColor = textSelectColor;
        updateTabStyles();
    }

    public void setTextUnselectColor(int textUnselectColor) {
        this.mTextUnselectColor = textUnselectColor;
        updateTabStyles();
    }

    public void setTextBold(int textBold) {
        this.mTextBold = textBold;
        updateTabStyles();
    }

    public void setTextAllCaps(boolean textAllCaps) {
        this.mTextAllCaps = textAllCaps;
        updateTabStyles();
    }

    public void setSnapOnTabClick(boolean snapOnTabClick) {
        mSnapOnTabClick = snapOnTabClick;
    }

    public int getTabCount() {
        return mTabCount;
    }

    public int getCurrentTab() {
        return mCurrentTab;
    }

    public int getIndicatorStyle() {
        return mIndicatorStyle;
    }

    public float getTabPadding() {
        return mTabPaddingWidth;
    }

    public boolean isTabSpaceEqual() {
        return mTabSpaceEqual;
    }

    public float getTabWidth() {
        return mTabWidth;
    }

    public int getIndicatorColor() {
        return mIndicatorColor;
    }

    public float getIndicatorHeight() {
        return mIndicatorHeight;
    }

    public float getIndicatorWidth() {
        return mIndicatorWidth;
    }

    public float getIndicatorCornerRadius() {
        return mIndicatorCornerRadius;
    }

    public float getIndicatorMarginLeft() {
        return mIndicatorMarginLeft;
    }

    public float getIndicatorMarginTop() {
        return mIndicatorMarginTop;
    }

    public float getIndicatorMarginRight() {
        return mIndicatorMarginRight;
    }

    public float getIndicatorMarginBottom() {
        return mIndicatorMarginBottom;
    }

    public int getUnderlineColor() {
        return mUnderlineColor;
    }

    public float getUnderlineHeight() {
        return mUnderlineHeight;
    }

    public int getDividerColor() {
        return mDividerColor;
    }

    public float getDividerWidth() {
        return mDividerWidth;
    }

    public float getDividerPadding() {
        return mDividerPadding;
    }

//    public float getTextSize() {
//        return mTextSize;
//    }

    public int getTextSelectColor() {
        return mTextSelectColor;
    }

    public int getTextUnselectColor() {
        return mTextUnselectColor;
    }

    public int getTextBold() {
        return mTextBold;
    }

    public boolean isTextAllCaps() {
        return mTextAllCaps;
    }

    public TextView getTitleView(int tab) {
        View tabView = mTabsContainer.getChildAt(tab);
        return (TextView) tabView.findViewById(R.id.tv_tab_title);
    }

    //setter and getter

    // show MsgTipView
    private final Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final SparseBooleanArray mInitSetMap = new SparseBooleanArray();

    /**
     * 显示未读消息
     *
     * @param position 显示tab位置
     * @param num      num小于等于0显示红点,num大于0显示数字
     */
    public void showMsg(int position, int num) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        InterMsgView tipView = (InterMsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            InterMsgUtils.show(tipView, num);

            if (mInitSetMap.get(position)) {
                return;
            }

            setMsgMargin(position, 4, 2);
            mInitSetMap.put(position, true);
        }
    }

    /**
     * 显示未读红点
     *
     * @param position 显示tab位置
     */
    public void showDot(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        showMsg(position, 0);
    }

    /**
     * 隐藏未读消息
     */
    public void hideMsg(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }

        View tabView = mTabsContainer.getChildAt(position);
        InterMsgView tipView = (InterMsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            tipView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置未读消息偏移,原点为文字的右上角.当控件高度固定,消息提示位置易控制,显示效果佳
     */
    public void setMsgMargin(int position, float leftPadding, float bottomPadding) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        InterMsgView tipView = (InterMsgView) tabView.findViewById(R.id.rtv_msg_tip);
        if (tipView != null) {
            TextView tv_tab_title = (TextView) tabView.findViewById(R.id.tv_tab_title);
            mTextPaint.setTextSize(mTextSizeUnselect);
            float textWidth = mTextPaint.measureText(tv_tab_title.getText().toString());
            float textHeight = mTextPaint.descent() - mTextPaint.ascent();
            MarginLayoutParams lp = (MarginLayoutParams) tipView.getLayoutParams();
            lp.leftMargin = mTabWidth >= 0 ? (int) (mTabWidth / 2 + textWidth / 2 + dp2px(leftPadding)) : (int) (mTabPaddingWidth + textWidth + dp2px(leftPadding));
            lp.topMargin = mHeight > 0 ? (int) (mHeight - textHeight) / 2 - dp2px(bottomPadding) : 0;
            tipView.setLayoutParams(lp);
        }
    }

    /**
     * 当前类只提供了少许设置未读消息属性的方法,可以通过该方法获取MsgView对象从而各种设置
     */
    public InterMsgView getMsgView(int position) {
        if (position >= mTabCount) {
            position = mTabCount - 1;
        }
        View tabView = mTabsContainer.getChildAt(position);
        return (InterMsgView) tabView.findViewById(R.id.rtv_msg_tip);
    }

    private OnTabSelectListener mListener;

    public void setOnTabSelectListener(OnTabSelectListener listener) {
        this.mListener = listener;
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("instanceState", super.onSaveInstanceState());
        bundle.putInt("mCurrentTab", mCurrentTab);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            mCurrentTab = bundle.getInt("mCurrentTab");
            state = bundle.getParcelable("instanceState");
            if (mCurrentTab != 0 && mTabsContainer.getChildCount() > 0) {
                updateTabSelection(mCurrentTab);
                scrollToCurrentTab();
            }
        }
        super.onRestoreInstanceState(state);
    }

    protected int dp2px(float dp) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }

    protected int sp2px(float sp) {
        final float scale = this.mContext.getResources().getDisplayMetrics().scaledDensity;
        return (int) (sp * scale + 0.5f);
    }


}
