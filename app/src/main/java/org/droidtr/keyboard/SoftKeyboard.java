package org.droidtr.keyboard;

import android.annotation.SuppressLint;
import android.graphics.*;
import android.graphics.drawable.*;
import android.inputmethodservice.*;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.*;
import android.widget.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Example of writing an input method for a soft keyboard.  This code is
 * focused on simplicity over completeness, so it should in no way be considered
 * to be a complete soft keyboard implementation.  Its purpose is to provide
 * a basic example for how you would get started writing an input method, to
 * be fleshed out as appropriate.
 */
public class SoftKeyboard extends InputMethodService {

	CustomKeyboardView main=null;
	util util;
	boolean lock=false,caps=false;
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public View onCreateInputView() {
		main = new CustomKeyboardView(this);
		util = new util(this);
		main.createKeyboard(3);
		main.setHeight((int) (getResources().getDisplayMetrics().heightPixels / 2.5f));

		main.bg = main.gd(Color.parseColor("#000000"));
		main.defaultButtonColor = Color.parseColor("#ffffff");
		main.setBackgroundColor(Color.parseColor("#000000"));

		String[][][] pages;
		String tmp;
		String layout = util.loadAssetTextAsString("trf.json");
		try {
			JSONObject reader = new JSONObject(layout);
			String[] page_names = {"A", "B", "C", "D"};
			pages = new String[page_names.length][][];
			for (int i = 0; i < pages.length; i++) {
				JSONObject a = reader.getJSONObject(page_names[i]);

				String[] row_names = {"num","row0", "row1", "row2", "row3"};
				pages[i] = new String[row_names.length][];
				for(int j=0;j<row_names.length;j++) {
					tmp = a.getString(row_names[j]);
					pages[i][j] = tmp.split(" ");
				}
				pages[i][row_names.length-1][2] = " ";
				main.addrow(pages[i],i);
			}

			Drawable gd = main.gd(Color.parseColor("#5f97f6"));
			Drawable gd2 = main.gd(Color.parseColor("#373c40"));


			//Geçişler
			main.getButton(0,3,0).setOnTouchListener(main.setActiveKeyboardEvent(1,true));
			main.getButton(1,4,0).setOnTouchListener(main.setActiveKeyboardEvent(2));
			main.getButton(1,3,0).setOnTouchListener(main.setActiveKeyboardEvent(0,true));
			main.getButton(2,4,0).setOnTouchListener(main.setActiveKeyboardEvent(0));
			main.getButton(0,4,0).setOnTouchListener(main.setActiveKeyboardEvent(2));
			main.getButton(2,3,0).setOnTouchListener(main.setActiveKeyboardEvent(3));
			main.getButton(3,4,0).setOnTouchListener(main.setActiveKeyboardEvent(1));
			main.getButton(3,3,0).setOnTouchListener(main.setActiveKeyboardEvent(2,true));


			DisplayMetrics displayMetrics = new DisplayMetrics();
			this.getWindow().getWindow().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

			// Boşluk tuşu
			for(int i=0;i<4;i++) {
				main.getButton(i, 4, 2).setLayoutParams(new LinearLayout.LayoutParams(displayMetrics.widthPixels / 2, -1));
				main.getButton(i, 4, 2).setIcon(R.drawable.space);
				main.getButton(i, 4, 2).setTag(KeyEvent.KEYCODE_SPACE);
			}
			// Shift tuşu
			for(int i=0;i<4;i++) {
				main.getButton(i, 3, 0).setIcon(getResources().getDrawable(R.drawable.caps));
			}
			// Enter tuşu
			for(int i=0;i<4;i++) {
				main.getButton(i, 4, 4).setIcon(getResources().getDrawable(R.drawable.enter));
			}

			// delete
			for(int i=0;i<2;i++) {
				main.getButton(i, 3, 10).setIcon(getResources().getDrawable(R.drawable.del));
			}
			for(int i=2;i<4;i++) {
				main.getButton(i, 3, 8).setIcon(getResources().getDrawable(R.drawable.del));
			}

			// Sol ok tuşu
			for(int i=0;i<2;i++) {
				main.getButton(i, 0, 0).setIcon(getResources().getDrawable(R.drawable.sol));
				main.getButton(i, 0, 0).setTag(21);
			}
			// Sol ok tuşu
			for(int i=0;i<2;i++) {
				main.getButton(i, 0, 11).setIcon(getResources().getDrawable(R.drawable.sag));
				main.getButton(i, 0, 11).setTag(22);
			}

			//özel tuşlar
			main.getButton(1,3,10).setTag(KeyEvent.KEYCODE_DEL);
			main.getButton(1,4,4).setTag(KeyEvent.KEYCODE_ENTER);
			main.getButton(2,3,8).setTag(KeyEvent.KEYCODE_DEL);
			main.getButton(2,4,4).setTag(KeyEvent.KEYCODE_ENTER);
			main.getButton(3,3,8).setTag(KeyEvent.KEYCODE_DEL);
			main.getButton(3,4,4).setTag(KeyEvent.KEYCODE_ENTER);
			main.getButton(0,3,10).setTag(KeyEvent.KEYCODE_DEL);
			main.getButton(0,4,4).setTag(KeyEvent.KEYCODE_ENTER);


			//Tekrarlama
			main.setRepeat(0,3,10);
			main.setRepeat(1,3,10);
			main.setRepeat(2,3,8);
			main.setRepeat(3,3,8);
			main.setRepeat(0,4,2);
			main.setRepeat(1,4,2);
			main.setRepeat(2,4,2);
			main.setRepeat(3,4,2);

			main.setActiveKeyboard(0);

			return main;
		} catch (JSONException e) {
			Log.e("Error",e.getMessage());
		}
    return null;
	}
}
