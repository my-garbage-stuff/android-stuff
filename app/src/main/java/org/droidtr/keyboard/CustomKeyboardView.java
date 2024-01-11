package org.droidtr.keyboard;

import android.content.*;
import android.os.*;
import android.view.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.graphics.*;
import android.inputmethodservice.*;
import android.graphics.drawable.*;
import android.media.*;
import java.util.*;

public class CustomKeyboardView extends RelativeLayout
{
	Context ctx=null;
	private LinearLayout.LayoutParams butparam =null;
	private LinearLayout[] main =null;
	private key[][][] b = null;
	private LinearLayout[][] ll=null;
	private int i=0;
	private int[] rmax=new int[50];
	private int mmax=0;
	private int getActiveKeyboard=0;
	public Drawable bg=null;
	public int defaultButtonColor=Color.BLACK;
	public int defaultButtonRadius=18;
	public boolean randombg=false;
	Handler h = new Handler();
	private int status=0;
	CustomKeyboardView(Context c){
		super(c);
		ctx=c;
		butparam=new LinearLayout.LayoutParams(-1,-1);
		butparam.weight=1.0f;
		main=new LinearLayout[50];
		createKeyboard(1);
		bg=gd(Color.parseColor("#000000"));
		defaultButtonColor=Color.parseColor("#ffffff");
		this.setBackgroundColor(Color.parseColor("#000000"));
		ll = new LinearLayout[50][400];
		b = new key[50][50][400];
	}
	public void addrow(String[][] rows,int index){
		for(int i=0;i<rows.length;i++){
			addrow(rows[i],index);
		}
	}
	public void addrow(String[][][] rows){
		for(int j=0;j<rows.length;j++){
			for(int i=0;i<rows.length;i++){
				addrow(rows[i],j+mmax);
			}
		}
	}
	public int getActiveKeyboard(){
		return getActiveKeyboard;
	}
	public void createKeyboard(int loop){
		for(i=0;i<loop;i++){
			main[mmax] = new LinearLayout(ctx);
			main[mmax].setLayoutParams(new LayoutParams(-1,-1));
			main[mmax].setOrientation(LinearLayout.VERTICAL);
			main[mmax].setVisibility(View.INVISIBLE);
			this.addView(main[mmax]);
			rmax[mmax]=0;
			mmax=mmax+1;
		}
	}
	public void setHeight(int index,int height){
		main[index].setLayoutParams(new LayoutParams(-1,height));
	}
	public void setHeight(int height){
		for(i=0;i<mmax;i++){
			main[i].setLayoutParams(new LayoutParams(-1,height));
		}
	}
	public void createKeyboard(){

		createKeyboard(1);
	}
	public int getKeyboardHeight(int index)
	{
		return main[index].getLayoutParams().height;
	}
	public void addrow(final String[] keys,int index){
		ll[index][rmax[index]] = new LinearLayout(ctx);
		for(i=0;i<keys.length;i++){
			b[index][rmax[index]][i] = new key(ctx);
			b[index][rmax[index]][i].setGravity(Gravity.CENTER);
			b[index][rmax[index]][i].setText(keys[i]);
			b[index][rmax[index]][i].setLayoutParams(butparam);
			b[index][rmax[index]][i].setOnTouchListener(ocl);
			ll[index][rmax[index]].addView(b[index][rmax[index]][i]);
		}
		ll[index][rmax[index]].setLayoutParams(butparam);
		main[index].addView(ll[index][rmax[index]]);
		rmax[index]=rmax[index]+1;
	}
	public key getButton(int keyboard,int row,int index){
		if(b[index]!= null){
			return b[keyboard][row][index];
		}else{
			return new key(ctx);
		}
	}
	public void interract(View p1){
		System.gc();
		if(p1.getTag()!=null){
			getCurrentInputConnection().sendKeyEvent(setctrlalt(Integer.parseInt(p1.getTag().toString())));
		}else{
			getCurrentInputConnection().commitText(((key)p1).getText().toString(),1);
		}
	}

	public LinearLayout getRow(int keyboard,int index){
		if(ll[index]!= null){
			return ll[keyboard][index];
		}else{
			return new LinearLayout(ctx);
		}
	}
	public LinearLayout getKeyboard(int index){
		if(main[index]!= null){
			return main[index];
		}else{
			return new LinearLayout(ctx);
		}
	}
	public void setActiveKeyboard(int index){
		getActiveKeyboard=index;
		if(main[index].getVisibility()==View.INVISIBLE){
			for(i=0;i<mmax;i++){
				main[i].setVisibility(View.INVISIBLE);
			}
			main[index].setVisibility(View.VISIBLE);
		}
	}
	public KeyEvent setctrlalt(int keycode){
		return setctrlalt(keycode,false,false,false);
	}
	public KeyEvent setctrlalt(int keycode,boolean ctrl,boolean alt,boolean caps){
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			if (!caps) {
				if (ctrl) {
					if (alt) {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_CTRL_ON | KeyEvent.META_ALT_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					} else {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_CTRL_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					}
				} else {
					if (alt) {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_ALT_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					} else {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, 0, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					}
				}
			} else {
				if (ctrl) {
					if (alt) {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_CTRL_ON | KeyEvent.META_ALT_ON | KeyEvent.META_SHIFT_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					} else {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_CTRL_ON | KeyEvent.META_SHIFT_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					}
				} else {
					if (alt) {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_ALT_ON | KeyEvent.META_SHIFT_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					} else {
						return new KeyEvent(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), KeyEvent.ACTION_DOWN, keycode, 0, KeyEvent.META_SHIFT_ON, 0, 0, KeyEvent.FLAG_SOFT_KEYBOARD | KeyEvent.FLAG_KEEP_TOUCH_MODE, InputDevice.SOURCE_KEYBOARD);
					}
				}
			}
		}
		return null;
	} 
	public OnTouchListener ocl = new OnTouchListener(){
		@Override
		public boolean onTouch(final View p1, final MotionEvent p2)
		{
			if(p2.getAction() == MotionEvent.ACTION_DOWN){
				interract(p1);
				if(status!=2 && getActiveKeyboard==1){
					setActiveKeyboard(0);
				}
			}
			return true;
		}
	};	
	OnTouchListener setActiveKeyboardEvent(int index){
		return setActiveKeyboardEvent(index,false);
	}
	OnTouchListener setActiveKeyboardEvent(final int index,final boolean isShift){
		return new OnTouchListener(){

			@Override
			public boolean onTouch(final View p1, final MotionEvent p2)
			{

				if(p2.getAction() == MotionEvent.ACTION_DOWN){
					if(isShift){
						if(status !=1){
							setActiveKeyboard(index);
						}
						if(status==0){
							status=1;
						}else if(status==1){
							status=2;
						}else{
							status=0;
						}
					}else{
						setActiveKeyboard(index);
					}
				}
				return true;
			}
		};
	}
	//this is default key class
	class key extends RelativeLayout {
		public TextView tv=null;
		public ImageView icon=null;
		public key(Context c){
			super(c);
			tv =new TextView(c);
			icon =new ImageView(c);
			if(randombg){
				Random r=new Random();
				bg=gd(Color.rgb(r.nextInt(128),r.nextInt(128),r.nextInt(128)));
			}
			setBackgroundDrawable(bg.getConstantState().newDrawable());
			tv.setTextColor(defaultButtonColor);
			addView(tv);
			addView(icon);
		}
		public void setText(String text){
			tv.setText(text);
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
				icon.setBackground(null);
			}
		}
		public void setIcon(Drawable drawable){
			tv.setText("");
			icon.setBackgroundDrawable(drawable);
		}
		public void setIcon(int resource){
			tv.setText("");
			icon.setBackgroundResource(resource);
		}
		public CharSequence getText(){
			return tv.getText();
		}

		@Override
		public void setBackgroundDrawable(Drawable background)
		{
			super.setBackgroundDrawable(background.getConstantState().newDrawable());
		}
	}
	public Drawable gd(int[] col){
		GradientDrawable gd = new GradientDrawable();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			gd.setColors(col);
		}
		gd.setCornerRadius(defaultButtonRadius);
		gd.setStroke(10,Color.TRANSPARENT);
		return gd.getConstantState().newDrawable();
	}
	Drawable gd(int col){
		int[] cols={col,Color.argb(Color.alpha(col),8*Color.red(col)/9,8*Color.green(col)/9,8*Color.blue(col)/9),col};
		return gd(cols);
	}
	public InputConnection getCurrentInputConnection(){
		return ((InputMethodService) getContext()).getCurrentInputConnection();
	}
	public void setRepeat(int keyboard,int row,int index){
		getButton(keyboard,row,index).setOnTouchListener(new OnTouchListener(){
				@Override
				public boolean onTouch(final View p1, final MotionEvent p2)
				{
					if(p2.getAction() == MotionEvent.ACTION_DOWN){
						i=0;
						interract(p1);
						h.postDelayed(new Runnable(){

								@Override
								public void run()
								{

									if(p2.getAction() != MotionEvent.ACTION_UP){
										interract(p1);
										if(i<80){
											i=i+3;
										}
										h.postDelayed(this,120-i);
									}
								}
							},120);
					}
					return true;
				}
			});
	}

}

