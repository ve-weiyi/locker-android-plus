package com.ve.module.locker.ui.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ve.module.locker.R;


/**
 * @author weiyi
 */
public class PrivacyItemView extends RelativeLayout {
    String TAG ="PrivacyItemView";
    private Context mContext;

    /*
     * 所有自定义属性
     */
    /**
     * layout padding
     */
    private int mPaddingRight;
    private int mPaddingLeft;
    private int mPaddingTop;
    private int mPaddingBottom;

    /**
     * 标题
     */
    private float mTitleTextSize;
    private int mTitleTextColor;
    private String mTitleText;

    /**
     * 编辑框内容
     */
    private float mEditTextSize;
    private int mEditTextColor;
    private String mEditText;
    private int mEditHint;
    private Drawable mEditBackGround;
    /**
     * 复制
     */
    private float mRightTextSize;
    private int mRightTextColor;
    private String mRightText;

    /**
     * 显示/隐藏图片
     */
    private Drawable mRightIcon;
    private int mRightIconWidth;
    private int mRightIconHeight;
    private int mRightIconMarginLeft;

    /*
     *  所有自定义View
     */
    private TextView mTitleTextView;
    private EditText mEditView;
    private TextView mRightTextView;
    private ImageView mRightImageView;
    
    
    public PrivacyItemView(Context context) {
        this(context, null);
    }

    public PrivacyItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PrivacyItemView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.PrivacyItemView);
        mPaddingLeft = a.getLayoutDimension(R.styleable.PrivacyItemView_paddingLeft, 20);
        mPaddingRight = a.getLayoutDimension(R.styleable.PrivacyItemView_paddingRight, 20);
        mPaddingTop = a.getLayoutDimension(R.styleable.PrivacyItemView_paddingTop, 20);
        mPaddingBottom = a.getLayoutDimension(R.styleable.PrivacyItemView_paddingBottom, 20);
        
        //标题
        mTitleTextSize = a.getDimension(R.styleable.PrivacyItemView_titleTextSize, 15);
        mTitleTextColor = a.getColor(R.styleable.PrivacyItemView_titleTextColor, 0xff333333);
        mTitleText = a.getString(R.styleable.PrivacyItemView_titleText);

        //编辑框内容
        mEditTextSize = a.getLayoutDimension(R.styleable.PrivacyItemView_editTextSize, 15);
        mEditTextColor = a.getColor(R.styleable.PrivacyItemView_editTextColor, 0xff333333);
        mEditText = a.getString(R.styleable.PrivacyItemView_editText);
        mEditBackGround=a.getDrawable(R.styleable.PrivacyItemView_editTextBackground);

        //右边图片
        mRightIcon = a.getDrawable(R.styleable.PrivacyItemView_rightIcon);
        mRightIconWidth = a.getLayoutDimension(R.styleable.PrivacyItemView_rightIconWidth, 80);
        mRightIconHeight = a.getLayoutDimension(R.styleable.PrivacyItemView_rightIconHeight, 40);
        mRightIconMarginLeft =
                a.getLayoutDimension(R.styleable.PrivacyItemView_rightIconMarginLeft, 20);

        //右边文字
        mRightTextSize = a.getDimension(R.styleable.PrivacyItemView_rightTextSize, 12);
        mRightTextColor = a.getColor(R.styleable.PrivacyItemView_rightTextColor, 0xff666666);
        mRightText = a.getString(R.styleable.PrivacyItemView_rightText);
        a.recycle();

        createView();
    }

    private void createView() {
        RelativeLayout rootLayout = new RelativeLayout(mContext);

        RelativeLayout layout = new RelativeLayout(mContext);
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.setMargins(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);

        //添加左侧的标题
        mTitleTextView=new TextView(mContext);
        mTitleTextView.setId(R.id.privacy_title_id);
        mTitleTextView.setText(mTitleText);
        mTitleTextView.setTextColor(mTitleTextColor);
        mTitleTextView.setTextSize(mTitleTextSize);
        mTitleTextView.setTypeface(null, Typeface.BOLD);
        //参数
        LayoutParams titleParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleParams.setMargins(0, 0, 0, 0);
        layout.addView(mTitleTextView, titleParams);



        LinearLayout linearLayout= new LinearLayout(mContext);
        linearLayout.setGravity(Gravity.END);
        LayoutParams linearLayoutParams=
                new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        linearLayoutParams.addRule(RelativeLayout.BELOW,R.id.privacy_title_id);
        //编辑框
        mEditView=new EditText(mContext);
        mEditView.setHint(mEditText);
        mEditView.setBackground(mEditBackGround);
        int padding=20;
        mEditView.setPadding(padding,padding,padding,padding);

        LinearLayout.LayoutParams layoutParams1=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT);
        layoutParams1.weight=1;
        layoutParams1.gravity=Gravity.CENTER_VERTICAL;
        linearLayout.addView(mEditView,layoutParams1);


        mRightImageView=new ImageView(mContext);
        mRightImageView.setImageDrawable(mRightIcon);
        LinearLayout.LayoutParams layoutParams2=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams2.gravity=Gravity.CENTER_VERTICAL;
        layoutParams2.setMargins(10,0,10,0);
        linearLayout.addView(mRightImageView,layoutParams2);

        //右边的TextView
        mRightTextView = new TextView(mContext);
        mRightTextView.setText(mRightText);
        mRightTextView.setTextColor(mRightTextColor);
        mRightTextView.setTextSize(mRightTextSize);
        mRightTextView.setTypeface(null, Typeface.BOLD);
        LinearLayout.LayoutParams layoutParams3=
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layoutParams3.gravity=Gravity.CENTER_VERTICAL;

        linearLayout.addView(mRightTextView,layoutParams3);



        layout.addView(linearLayout,linearLayoutParams);
        rootLayout.addView(layout, layoutParams);
        addView(rootLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measureWidth = measureDimension(getMaxChildWidth(), widthMeasureSpec);
        int measureHeight = measureDimension(getTotleHeight(), heightMeasureSpec);
        // 设置自定义的控件MyViewGroup的大小
        setMeasuredDimension(measureWidth, measureHeight);
    }

    public int measureDimension(int defaultSize, int measureSpec) {
        int result=0; //结果

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        switch (specMode) {
            case MeasureSpec.AT_MOST:  // 子容器可以是声明大小内的任意大小
//				LogUtil.e(TAG, "子容器可以是声明大小内的任意大小");
//				LogUtil.e(TAG, "大小为:"+specSize);
                result=specSize;
                break;
            case MeasureSpec.EXACTLY: //父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
//				LogUtil.e(TAG, "父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间");
//				LogUtil.e(TAG, "大小为:"+specSize);
                result=specSize;
                break;
            case MeasureSpec.UNSPECIFIED:  //父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
//				LogUtil.e(TAG, "父容器对于子容器没有任何限制,子容器想要多大就多大");
//				LogUtil.e(TAG, "大小为:"+specSize);
//				LogUtil.e(TAG, "默认大小为:"+defaultSize);
                result=defaultSize;
                break;
            default:
                break;
        }
        return result;
    }

    /***
     * 获取子View中宽度最大的值
     */
    private int getMaxChildWidth() {
        int childCount = getChildCount();
        int maxWidth = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView.getMeasuredWidth() > maxWidth) {
                maxWidth = childView.getMeasuredWidth();
            }
        }
        return maxWidth;
    }

    /***
     * 将所有子View的高度相加
     **/
    private int getTotleHeight() {
        int childCount = getChildCount();
        int height = 0;
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            height += childView.getMeasuredHeight();
        }
        //LogUtil.e("将所有子View的高度相加= "+height);
        return height;
    }
}
