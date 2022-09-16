package com.ve.lib.view.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ve.lib.view.R;


/**
 * @author weiyi
 */
public class HorizontalItemView extends RelativeLayout {
	String TAG ="HorizontalItemView";
	private Context mContext;

	/*
	 * 所有自定义属性
	 */
	private int mPaddingRight;
	private int mPaddingLeft;
	private int mPaddingTop;
	private int mPaddingBottom;

	private int mIconWidth;
	private int mIconHeight;
	private Drawable mIcon;
	private int mIconPaddingRight;

	private float mTileTextSize;
	private int mTileTextColor;
	private String mTileText;

	private int mTipTextSize;
	private int mTipTextColor;
	private String mTipText;
	private int mTipPaddingLeft;
	private boolean mTipVisiblity;

	private float mRightTextSize;
	private int mRightTextColor;
	private String mRightText;

	private Drawable mRightIcon;
	private int mRightIconWidth;
	private int mRightIconHeight;
	private int mRightIconMarginLeft;

	/*
	 *  所有自定义View
	 */
	private ImageView mTitleView;
	private TextView mTileView;
	private TextView mTipView;
	private TextView mRightView;
	private ImageView mRightImageView;

	public HorizontalItemView(Context context) {
		this(context, null);
	}

	public HorizontalItemView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public HorizontalItemView(Context context, AttributeSet attrs, int defaultStyle) {
		super(context, attrs, defaultStyle);
		mContext = context;
		TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.HorizontalItemView);
		mPaddingLeft = a.getLayoutDimension(R.styleable.HorizontalItemView_paddingLeft, 20);
		mPaddingRight = a.getLayoutDimension(R.styleable.HorizontalItemView_paddingRight, 20);
		mPaddingTop = a.getLayoutDimension(R.styleable.HorizontalItemView_paddingTop, 30);
		mPaddingBottom = a.getLayoutDimension(R.styleable.HorizontalItemView_paddingBottom, 30);
		//左边图标
		mIconWidth = a.getLayoutDimension(R.styleable.HorizontalItemView_hIconWidth, 50);
		mIconHeight = a.getLayoutDimension(R.styleable.HorizontalItemView_hIconHeight, 50);
		mIcon = a.getDrawable(R.styleable.HorizontalItemView_hIcon);
		mIconPaddingRight = a.getLayoutDimension(R.styleable.HorizontalItemView_iconPaddingRight, 20);

		mTileTextSize = a.getDimension(R.styleable.HorizontalItemView_tileTextSize, 15);
		mTileTextColor = a.getColor(R.styleable.HorizontalItemView_tileTextColor, 0xff333333);
		mTileText = a.getString(R.styleable.HorizontalItemView_tileText);

		mTipTextSize = a.getLayoutDimension(R.styleable.HorizontalItemView_hTipTextSize, 15);
		mTipTextColor = a.getColor(R.styleable.HorizontalItemView_hTipTextColor, 0xff333333);
		mTipText = a.getString(R.styleable.HorizontalItemView_hTipText);
		mTipVisiblity = a.getBoolean(R.styleable.HorizontalItemView_hTipVisiblity, false);
		mTipPaddingLeft = a.getLayoutDimension(R.styleable.HorizontalItemView_hTipPaddingLeft, 2);

		mRightIcon = a.getDrawable(R.styleable.HorizontalItemView_rightIcon);
		mRightIconWidth = a.getLayoutDimension(R.styleable.HorizontalItemView_rightIconWidth, 80);
		mRightIconHeight = a.getLayoutDimension(R.styleable.HorizontalItemView_rightIconHeight, 40);
		mRightIconMarginLeft =
				a.getLayoutDimension(R.styleable.HorizontalItemView_rightIconMarginLeft, 20);

		mRightTextSize = a.getDimension(R.styleable.HorizontalItemView_rightTextSize, 12);
		mRightTextColor = a.getColor(R.styleable.HorizontalItemView_rightTextColor, 0xff666666);
		mRightText = a.getString(R.styleable.HorizontalItemView_rightText);
		a.recycle();

		createView();
	}

	private void createView() {
		RelativeLayout rootLayout = new RelativeLayout(mContext);

		RelativeLayout layout = new RelativeLayout(mContext);
		LayoutParams layoutParams =
				new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		layoutParams.setMargins(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);

		//添加最左侧的图标
		mTitleView = new ImageView(mContext);
		mTitleView.setId(R.id.horizontal_image_id);
		mTitleView.setScaleType(ImageView.ScaleType.FIT_XY);
		mTitleView.setImageDrawable(mIcon);
		LayoutParams titleParams =
				new LayoutParams(mIconWidth, mIconHeight);
		titleParams.setMargins(0, 0, mIconPaddingRight, 0);
		titleParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layout.addView(mTitleView, titleParams);

		mTileView = new TextView(mContext);
		mTileView.setId(R.id.horizontal_tile_id);
		mTileView.setText(mTileText);
		mTileView.setTextColor(mTileTextColor);
		mTileView.setTextSize(mTileTextSize);
		LayoutParams tileParams =
				new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

		tileParams.addRule(RelativeLayout.RIGHT_OF, R.id.horizontal_image_id);
		tileParams.addRule(RelativeLayout.CENTER_VERTICAL);
		layout.addView(mTileView, tileParams);

		if (mTipVisiblity) {
			mTipView = new TextView(mContext);
            mTipView.setText(mTipText);
            mTipView.setTextColor(mTipTextColor);
            mTipView.getPaint().setTextSize(mTipTextSize);
            LayoutParams tipParams =
                    new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            tipParams.addRule(RelativeLayout.RIGHT_OF, R.id.horizontal_tile_id);
            tipParams.addRule(RelativeLayout.CENTER_VERTICAL);
            tipParams.setMargins(mTipPaddingLeft, 0, 0, 0);
            layout.addView(mTipView, tipParams);
        }
        //右边的TextView
        mRightView = new TextView(mContext);
        mRightView.setText(mRightText);
        mRightView.setTextColor(mRightTextColor);
        mRightView.setTextSize(mRightTextSize);
        LayoutParams rightParams =
                new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        //rightIcon不为空，添加到rightIcon左侧
        if (mRightIcon != null) {
            mRightImageView = new ImageView(mContext);
            mRightImageView.setId(R.id.horizontal_right_image_id);
			mRightImageView.setImageDrawable(mRightIcon);
			LayoutParams rightImageParams =
					new LayoutParams(mRightIconWidth, mRightIconHeight);
			rightImageParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			rightImageParams.addRule(RelativeLayout.CENTER_VERTICAL);
			rightImageParams.setMargins(mRightIconMarginLeft, 0, 0, 0);
			layout.addView(mRightImageView, rightImageParams);

			rightParams.addRule(RelativeLayout.LEFT_OF, R.id.horizontal_right_image_id);
			rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
			rightParams.setMargins(mRightIconMarginLeft, 0, 0, 0);
		} else {
            //添加到父布局左侧
            rightParams.addRule(RelativeLayout.ALIGN_PARENT_END);
            rightParams.addRule(RelativeLayout.CENTER_VERTICAL);
        }
        layout.addView(mRightView, rightParams);

        rootLayout.addView(layout, layoutParams);
        addView(rootLayout, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public TextView getTileView() {
        return mTileView;
    }
    public TextView getRightTextView() {
        return mRightView;
    }
	public ImageView getRightImageView() {
		return mRightImageView;
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
			// 子容器可以是声明大小内的任意大小
			case MeasureSpec.AT_MOST:
				result=specSize;
				break;
			//父容器已经为子容器设置了尺寸,子容器应当服从这些边界,不论子容器想要多大的空间.  比如EditTextView中的DrawLeft
			case MeasureSpec.EXACTLY:
				result=specSize;
				break;
			//父容器对于子容器没有任何限制,子容器想要多大就多大. 所以完全取决于子view的大小
			case MeasureSpec.UNSPECIFIED:
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
