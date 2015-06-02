package com.example.mappp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.OverlayItem;

public class Pinpoint extends ItemizedOverlay<OverlayItem>{

	private ArrayList<OverlayItem> markr = new  ArrayList<OverlayItem>();
	private Context c;
	
	public Pinpoint(Drawable arg0) {
		super(boundCenter(arg0));
		// TODO Auto-generated constructor stub
	}
	public Pinpoint(Drawable m, Context cont) {
		this(m);
		c=cont;
	}

	@Override
	protected OverlayItem createItem(int arg0) {
		// TODO Auto-generated method stub
		return markr.get(arg0);
	}

	@Override
	public int size() {
		// TODO Auto-generated method stub
		return markr.size();
	}

	
	public void insertmark(OverlayItem ovitem) {
		// TODO Auto-generated method stub
		markr.add(ovitem);
		this.populate();
		
	}
}
