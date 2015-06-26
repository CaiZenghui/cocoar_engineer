package com.csd.android.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.csd.android.R;

public class SideBar extends View {

	private OnTouchingLetterChangedListener onTouchingLetterChangedListener;
	private int choose = -1; // 选中
	private Paint paint = new Paint();

	private TextView mTextDialog;

	public static Mode mode = Mode.Normal;
	public static List<String> bb;
	static {
		bb = new ArrayList<String>();
		bb.add("A");
		bb.add("B");
		bb.add("C");
		bb.add("D");
		bb.add("E");
		bb.add("F");
		bb.add("G");
		bb.add("H");
		bb.add("I");
		bb.add("J");
		bb.add("K");
		bb.add("L");
		bb.add("M");
		bb.add("N");
		bb.add("O");
		bb.add("P");
		bb.add("Q");
		bb.add("R");
		bb.add("S");
		bb.add("T");
		bb.add("U");
		bb.add("V");
		bb.add("W");
		bb.add("X");
		bb.add("Y");
		bb.add("Z");
	}

	public void setSlidString(List<String> index) {
		bb.clear();
		bb.add("常用");
		bb.add("热门");
		for (String newStr : index) {
			bb.add(newStr.toUpperCase());
		}
	}

	public enum Mode {
		Normal, Hot
	}

	public void setTextView(TextView mTextDialog) {
		this.mTextDialog = mTextDialog;
	}

	private float textSize = 12f;

	public SideBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		textSize = context.getResources().getDimension(R.dimen.slide_bar_text_size);
	}

	public SideBar(Context context, AttributeSet attrs) {
		super(context, attrs);
		textSize = context.getResources().getDimension(R.dimen.slide_bar_text_size);
	}

	public SideBar(Context context) {
		super(context);
		textSize = context.getResources().getDimension(R.dimen.slide_bar_text_size);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		int height = getHeight();
		int width = getWidth();
		int singleHeight = height / bb.size();

		for (int i = 0; i < bb.size(); i++) {
			paint.setColor(Color.GRAY);
			paint.setTypeface(Typeface.DEFAULT);
			paint.setAntiAlias(true);
			paint.setTextSize(textSize);
			if (i == choose) {
				paint.setColor(Color.parseColor("#ffffff"));
				paint.setFakeBoldText(true);
			}

			float xPos = width / 2 - paint.measureText(bb.get(i)) / 2;
			float yPos = singleHeight * i + singleHeight;
			canvas.drawText(bb.get(i), xPos, yPos, paint);
			paint.reset();
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		final int action = event.getAction();
		final float y = event.getY();
		final int oldChoose = choose;
		final OnTouchingLetterChangedListener listener = onTouchingLetterChangedListener;
		final int c = (int) (y / getHeight() * bb.size());
		switch (action) {
			case MotionEvent.ACTION_UP:
				setBackgroundDrawable(new ColorDrawable(0x00000000));
				choose = -1;
				invalidate();
				if (mTextDialog != null) {
					mTextDialog.setVisibility(View.INVISIBLE);
				}
				break;

			default:
				setBackgroundResource(R.drawable.bg_sidebar);
				if (oldChoose != c) {
					if (c >= 0 && c < bb.size()) {
						if (listener != null) {
							listener.onTouchingLetterChanged(bb.get(c));
						}
						if (mTextDialog != null) {
							mTextDialog.setText(bb.get(c));
							mTextDialog.setVisibility(View.VISIBLE);
						}

						choose = c;
						invalidate();
					}
				}

				break;
		}
		return true;
	}

	/**
	 * 
	 * @param onTouchingLetterChangedListener
	 */
	public void setOnTouchingLetterChangedListener(OnTouchingLetterChangedListener onTouchingLetterChangedListener) {
		this.onTouchingLetterChangedListener = onTouchingLetterChangedListener;
	}

	public interface OnTouchingLetterChangedListener {
		public void onTouchingLetterChanged(String s);
	}

}
