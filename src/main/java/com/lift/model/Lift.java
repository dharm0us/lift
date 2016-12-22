package com.lift.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import com.lift.model.sensor.MotionSensor;
import com.lift.model.sensor.SmokeSensor;
import com.lift.model.sensor.WeightSensor;
import com.lift.service.EmergencyService;
import com.lift.service.FloorService;
import com.lift.service.WeightService;

public class Lift {

	//Lift parts
	private Door door;
	private TouchPanel panel;
	
	//sensors
	private SmokeSensor smokeSensor; //todo - use
	private WeightSensor weightSensor;
	private MotionSensor motionSensor;
	
	//services
	private WeightService weightService;
	private EmergencyService emergencyService;
	
	//lift state
	private List<Floor> servicableFloors;
	private Floor lastStop;
	private Floor nextStop;
	private PriorityQueue<TravelRequest> requestQueue;//todo - check for thread safety, how will concurrent additions work?
	
	public void setSmokeSensor(SmokeSensor smokeSensor) {
		this.smokeSensor = smokeSensor;
	}

	public void init() {
		//initState
		Floor groundFloor = FloorService.getGroundFloor();
    	this.lastStop = groundFloor;
    	this.nextStop = groundFloor;
    	this.servicableFloors = new ArrayList<Floor>();
    	this.requestQueue = new PriorityQueue<TravelRequest>();
    	
    	//initSensors
    	smokeSensor.setLift(this);
    	
    	//initparts
    	panel.lift = this;
	}
	
	public void triggerEmergency(EmergencyStopReason reason) {
		switch(reason) {
		case SMOKE_DETECTED:
			emergencyService.soundAlarm(reason);
			//notifyService.notifyFireStation();
			//firestation, maint, sprinkler, camera
			makeEmergencyStop();
			break;
		default:
			break;
		}
	}
	
	public void recieveRequest(TravelRequest request) {
		if (servicableFloors.contains(request.getFloor())) {
			requestQueue.add(request);
		}
	}

	public void travelIfNeeded() {
		//authenticate();
		if(!inMotion()) {
			travelToNextFloor();
		}
	}
	
	private boolean inMotion() {
		return (lastStop != nextStop);
	}

	private void travelToNextFloor() {
		Floor nextStop = getNextDestination();
		if(nextStop == null) return;
		gotoFloor(nextStop);
	}
	
	private void gotoFloor(Floor destination) {
		this.nextStop = destination;
		doActualPhysicalTravel(lastStop, destination);
		door.open();
		weightService.waitForPeopleFlowToStop(motionSensor);
		while(!weightService.excessWeight(weightSensor)) {
			weightService.soundExcessWeightAlarm(weightSensor);
			sleep();
		}
		door.close();
	}
	
	private void sleep() {
		// TODO Auto-generated method stub
		
	}

	private void doActualPhysicalTravel(Floor source, Floor destination) {
		if(source.equals(destination)) return;
		//computeDistance
		//fire up pullies, ropes etc and then
		arriveAt(destination);
	}
	
	private void arriveAt(Floor destination) {
		this.lastStop = destination;
	}
	
	private Floor getNextDestination() {
		TravelRequest request = requestQueue.poll();
		if(request != null) return request.getFloor();
		return null;
	}
	

	private void makeEmergencyStop() {
		if(!inMotion()) {
			//do nothing
		} else {
			arriveAt(getNearestServiceableFloor());
		}
	}

	private Floor getNearestServiceableFloor() {
		// TODO Auto-generated method stub
		return null;
	}

	
	
}
