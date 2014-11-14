package com.mhdlub.sim2048;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class grid_adapter extends BaseAdapter{

	private ArrayList<grid_item> contents;//array of grid_items
	private Context contxt;//the calling context 
	
	public grid_adapter(Context c, ArrayList<grid_item> con)
	{
		this.contxt=c;
		this.contents=con;
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return contents.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override	
	public boolean isEnabled(int position) {
		// TODO Auto-generated method stub
		//disable clicking on gridview items
		return false;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		View grid;
		//bind with the actual gridview
	      LayoutInflater inflater = (LayoutInflater) contxt
	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	      if (arg1 == null) {
	            grid = new View(contxt);//create a new view
	            //bind it with the XML code that represents individual tile
	        grid = inflater.inflate(R.layout.grid_layout, null);
	            
	      }
	      else
	    	  grid = arg1;
	      //bind properties of each tile using the provided contents
	      TextView textView = (TextView) grid.findViewById(R.id.textView1);
	      //set the text
          textView.setText(contents.get(arg0).getText());
          //set the color
          textView.setBackgroundColor(contents.get(arg0).getCol());
          
	      return grid;
	}

}
