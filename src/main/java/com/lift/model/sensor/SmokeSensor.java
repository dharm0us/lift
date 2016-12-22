package com.lift.model.sensor;

import com.lift.model.EmergencyStopReason;
import com.lift.model.Lift;

public class SmokeSensor {

	private Lift lift;
	
	public Lift getLift() {
		return lift;
	}

	public void setLift(Lift lift) {
		this.lift = lift;
	}

	private void smokeDetected() {
		lift.triggerEmergency(EmergencyStopReason.SMOKE_DETECTED);
	}
}
