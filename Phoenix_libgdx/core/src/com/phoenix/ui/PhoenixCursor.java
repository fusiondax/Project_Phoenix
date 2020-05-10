package com.phoenix.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Pixmap;

public enum PhoenixCursor
{
	Arrow("ui_arrow_cursor", 0, 0), 
	Vertical_Resize("ui_ver_resize_cursor", 16, 16), 
	Horizontal_Resize("ui_hor_resize_cursor", 16, 16), 
	Diagonal_Bottom_Right_Resize("ui_diagonal_bot_right_resize_cursor", 16, 16), 
	Diagonal_Bottom_Left_Resize("ui_diagonal_bot_left_resize_cursor", 16, 16),
	Hand("ui_hand_cursor", 16, 16);
	
	private Cursor cursor;
	
	private PhoenixCursor(String name, int hotspotX, int hotspotY)
	{
		String cursorFileName = "graphics/cursors/" + name + ".png";
		cursor = Gdx.graphics.newCursor(new Pixmap(Gdx.files.internal(cursorFileName)), hotspotX, hotspotY);
	}
	
	public Cursor getCursor()
	{
		return cursor;
	}
}
