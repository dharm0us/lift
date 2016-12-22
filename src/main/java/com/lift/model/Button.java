package com.lift.model;

public class Button {
	public Floor floor;
	public TouchPanel panel;
	
	public void press() {
		panel.buttonPressed(this);
	}
}
