package com.jayway.android.robotium.solo;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import android.app.Activity;
import android.app.Dialog;
import android.app.Instrumentation;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

/**
 * This class contains view methods. Examples are getViews(),
 * getCurrentTextViews(), getCurrentImageViews().
 * 
 * @author Renas Reda, renas.reda@jayway.com
 * 
 */

class ViewFetcher {

	private final ArrayList<View> viewList = new ArrayList<View>();
	private final Instrumentation inst;

    /**
     * Constructs this object.
     *
     * @param soloActivity the {@link Activity} instance.
     * @param dialogUtils the {@link Dialog} instance.
     * @param inst the {@link Instrumentation} instance.
     */
	
    public ViewFetcher(Instrumentation inst) {
        this.inst = inst;
    }

	
	/**
	 * Returns the absolute top view in an activity.
	 *
	 * @param view the view whose top parent is requested
	 * @return the top parent view
	 *
	 */
	
	public View getTopParent(View view) {
		if (view.getParent() != null
				&& !view.getParent().getClass().getName().equals(
						"android.view.ViewRoot")) {
			return getTopParent((View) view.getParent());
		} else {
			return view;
		}
	}
	
	/**
	 * Returns the list item parent. It is used by clickInList().
	 * 
	 * @param view the view who's parent is requested
	 * @return the parent of the view
	 */
	
	public View getListItemParent(View view)
	{
		if (view.getParent() != null
				&& !(view.getParent() instanceof android.widget.ListView)) {
			return getListItemParent((View) view.getParent());
		} else {
			return view;
		}
		
	}
	

	/**
	 * This method returns an ArrayList of all the views located in the current activity or dialog.
	 *
	 * @return ArrayList with the views found in the current activity or dialog
	 *
	 */
	
	public ArrayList<View> getViews() {
		inst.waitForIdleSync();
		View decorView;
		viewList.clear();
		View [] views = getWindowDecorViews();
		try {
			decorView = views[views.length-1];
			getViews(decorView);
			return viewList;
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}

 

	/**
	 * Private method which adds all the views located in the currently active
	 * activity to an ArrayList viewList.
	 *
	 * @param view the view who's children should be added to viewList 
	 *
	 */
	
	private void getViews(View view) {
		viewList.add(view);
		if (view instanceof ViewGroup) {
			ViewGroup vg = (ViewGroup) view;
			for (int i = 0; i < vg.getChildCount(); i++) {
				getViews(vg.getChildAt(i));
			}
		}
	}

	/**
	 * This method returns an ArrayList of the images contained in the current
	 * activity.
	 *
	 * @return ArrayList of the images contained in the current activity
	 *
	 */
	
	public ArrayList<ImageView> getCurrentImageViews() {
		ArrayList<View> viewList = getViews();
		ArrayList<ImageView> imageViewList = new ArrayList<ImageView>();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.ImageView) {
				imageViewList.add((ImageView) view);
			}	
		}
		return imageViewList;
	}
	
	
	/**
	 * This method returns an EditText with a certain index.
	 *
	 * @return the EditText with a specified index
	 *
	 */
	
	public EditText getEditText(int index) {
		ArrayList<EditText> editTextList = getCurrentEditTexts();
		return editTextList.get(index);
	}
	
	/**
	 * This method returns a button with a certain index.
	 *
	 * @param index the index of the button
	 * @return the button with the specific index
	 *
	 */
	
	public Button getButton(int index) {
		ArrayList<Button> buttonList = getCurrentButtons();
		return buttonList.get(index);
	}
	
	/**
	 * This method returns the number of buttons located in the current
	 * activity.
	 *
	 * @return the number of buttons in the current activity
	 *
	 */
	
	public int getCurrenButtonsCount() {
		return getCurrentButtons().size();
	}
	
	
	/**
	 * This method returns an ArrayList of all the edit texts located in the
	 * current activity.
	 *
	 * @return an ArrayList of the edit texts located in the current activity
	 *
	 */
	
	public ArrayList<EditText> getCurrentEditTexts() {
		ArrayList<EditText>editTextList = new ArrayList<EditText>();
		ArrayList<View> viewList = getViews();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.EditText)
				editTextList.add((EditText) view);
		}
		return editTextList;
		
	}
	
	/**
	 * This method returns an ArrayList of all the list views located in the current activity.
	 * 
	 * 
	 * @return an ArrayList of the list views located in the current activity
	 * 
	 */

	public ArrayList<ListView> getCurrentListViews() {
		ArrayList<ListView> listViews = new ArrayList<ListView>();
		ArrayList<View> viewList = getViews();
		try{
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.ListView)
				listViews.add((ListView) view);
			}
		}catch(NullPointerException e){}
		return listViews;
	}

	/**
	 * This method returns an ArrayList of all the scroll views located in the current activity.
	 * 
	 * @return an ArrayList of the scroll views located in the current activity
	 * 
	 */

	public ArrayList<ScrollView> getCurrentScrollViews() {
		ArrayList<ScrollView> scrollViews = new ArrayList<ScrollView>();
		ArrayList<View> viewList = getViews();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.ScrollView)
				scrollViews.add((ScrollView) view);
		}
		return scrollViews;
	}
	
	/**
	 * This method returns an ArrayList of spinners (drop-down menus) located in the current
	 * activity.
	 *
	 * @return an ArrayList of the spinners located in the current activity or view
	 *
	 */
	
	public ArrayList<Spinner> getCurrentSpinners()
	{
		ArrayList<Spinner>spinnerList = new ArrayList<Spinner>();
		ArrayList<View> viewList = getViews();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.Spinner)
				spinnerList.add((Spinner) view);
		}
		return spinnerList;
	}
	
	/**
	 * This method returns an ArrayList of the text views located in the current
	 * activity.
	 *
	 * @param parent the parent View in which the text views should be returned. Null if
	 * all text views from the current activity should be returned
	 *
	 * @return an ArrayList of the text views located in the current activity or view
	 *
	 */
	
	public ArrayList<TextView> getCurrentTextViews(View parent) {
		viewList.clear();		
		if(parent == null)
			getViews();
		else
			getViews(parent);
		
		ArrayList<TextView> textViewList = new ArrayList<TextView>();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.TextView) {
				textViewList.add((TextView) view);
			}
			
		}
		return textViewList;	
	}
	
	/**
	 * This method returns an ArrayList of the grid views located in the current
	 * activity.
	 *
	 * @return an ArrayList of the grid views located in the current activity
	 *
	 */
	
	public ArrayList<GridView> getCurrentGridViews() {
		ArrayList<View> viewList = getViews();
		ArrayList<GridView> gridViewList = new ArrayList<GridView>();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.GridView)
				gridViewList.add((GridView) view);
		}
		return gridViewList;
	}
	
	
	/**
	 * This method returns an ArrayList with the buttons located in the current
	 * activity.
	 *
	 * @return and ArrayList of the buttons located in the current activity
	 *
	 */
	
	public ArrayList<Button> getCurrentButtons() {
		ArrayList<Button> buttonList = new ArrayList<Button>();
		ArrayList<View> viewList = getViews();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.Button)
				buttonList.add((Button) view);
		}
		return buttonList;
	}

	/**
	 * This method returns an ArrayList with the toggle buttons located in the
	 * current activity.
	 * 
	 * @return and ArrayList of the toggle buttons located in the current activity
	 * 
	 */

	public ArrayList<ToggleButton> getCurrentToggleButtons() {
		ArrayList<ToggleButton> toggleButtonList = new ArrayList<ToggleButton>();
		ArrayList<View> viewList = getViews();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.ToggleButton)
				toggleButtonList.add((ToggleButton) view);
		}
		return toggleButtonList;
	}
	
	/**
	 * This method returns an ArrayList of the radio buttons contained in the current
	 * activity.
	 *
	 * @return ArrayList of the radio buttons contained in the current activity
	 *
	 */
	
	public ArrayList<RadioButton> getCurrentRadioButtons() {
		ArrayList<View> viewList = getViews();
		ArrayList<RadioButton> radioButtonList = new ArrayList<RadioButton>();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.RadioButton) {
				radioButtonList.add((RadioButton) view);
			}	
		}
		return radioButtonList;
	}
	
	/**
	 * This method returns an ArrayList of the check boxes contained in the current
	 * activity.
	 *
	 * @return ArrayList of the check boxes contained in the current activity
	 *
	 */
	
	public ArrayList<CheckBox> getCurrentCheckBoxes()
	{
		ArrayList<View> viewList = getViews();
		ArrayList<CheckBox> checkBoxList = new ArrayList<CheckBox>();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.CheckBox) {
				checkBoxList.add((CheckBox) view);
			}	
		}
		return checkBoxList;
	}
	
	/**
	 * This method returns an ArrayList of the image buttons contained in the current
	 * activity.
	 *
	 * @return ArrayList of the image buttons contained in the current activity
	 *
	 */
	
	public ArrayList<ImageButton> getCurrentImageButtons()
	{
		ArrayList<View> viewList = getViews();
		ArrayList<ImageButton> imageButtonList = new ArrayList<ImageButton>();
		Iterator<View> iterator = viewList.iterator();
		while (iterator.hasNext()) {
			View view = iterator.next();
			if (view instanceof android.widget.ImageButton) {
				imageButtonList.add((ImageButton) view);
			}	
		}
		return imageButtonList;
	}
	
	private static Class<?> windowManager;
	static{
		try {
			windowManager = Class.forName("android.view.WindowManagerImpl");
			
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * Returns the WindorDecorViews shown on the screen
	 * @return the WindorDecorViews shown on the screen
	 * 
	 */
	
	public View[] getWindowDecorViews()
	{

		Field viewsField;
		Field instanceField;
		try {
			viewsField = windowManager.getDeclaredField("mViews");
			instanceField = windowManager.getDeclaredField("mWindowManager");
			viewsField.setAccessible(true);
			instanceField.setAccessible(true);
			Object instance = instanceField.get(null);
			return (View[]) viewsField.get(instance);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} 
		return null;
	}
	
	
}
