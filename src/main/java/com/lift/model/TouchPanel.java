package com.lift.model;

import java.util.List;

public class TouchPanel {
	
	Lift lift;
	List<Button> buttons;
	
	public void buttonPressed(Button button) {
		TravelRequest request = new TravelRequest();
		request.setDirection(Direction.NONE);
		request.setFloor(button.floor);
		
		this.lift.recieveRequest(request);
	}
}
