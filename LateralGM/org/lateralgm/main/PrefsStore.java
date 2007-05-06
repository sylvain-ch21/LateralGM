/*
 * Copyright (C) 2007 Quadduc <quadduc@gmail.com>
 * 
 * This file is part of Lateral GM.
 * Lateral GM is free software and comes with ABSOLUTELY NO WARRANTY.
 * See LICENSE for details.
 */

package org.lateralgm.main;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.prefs.Preferences;

public class PrefsStore
	{
	private static final Preferences prefs = Preferences.userRoot().node("/org/lateralgm");

	public static final ArrayList<String> getRecentFiles()
		{
		String value = prefs.get("FILE_RECENT",null);
		if (value == null) return new ArrayList<String>(0);
		String[] array = value.split(" ");
		ArrayList<String> list = new ArrayList<String>(array.length);
		for (String name : array)
			list.add(Util.urlDecode(name));
		return list;
		}

	public static final void addRecentFile(String name)
		{
		int maxcount = prefs.getInt("FILE_RECENT_COUNT",4);
		ArrayList<String> oldList = getRecentFiles();
		oldList.remove(name);
		String newList;
		newList = Util.urlEncode(name);
		for (int i = 0; i + 1 < maxcount && i < oldList.size(); i++)
			newList += " " + Util.urlEncode(oldList.get(i));
		prefs.put("FILE_RECENT",newList);
		}

	public static final Rectangle getWindowBounds()
		{
		return Util.stringToRectangle(prefs.get("WINDOW_BOUNDS",null),new Rectangle(800,600));
		}

	public static final void setWindowBounds(Rectangle r)
		{
		prefs.put("WINDOW_BOUNDS",Util.rectangleToString(r));
		}

	public static final boolean getWindowMaximized()
		{
		return prefs.getBoolean("WINDOW_MAXIMIZED",true);
		}

	public static final void setWindowMaximized(boolean b)
		{
		prefs.putBoolean("WINDOW_MAXIMIZED",b);
		}
	}