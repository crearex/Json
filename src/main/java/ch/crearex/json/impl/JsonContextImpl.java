package ch.crearex.json.impl;

import java.util.LinkedList;
import java.util.Stack;

import ch.crearex.json.JsonParser;
import ch.crearex.json.JsonCallback;
import ch.crearex.json.JsonContextBase;
import ch.crearex.json.JsonIllegalSyntaxException;

public class JsonContextImpl extends JsonContextBase {
	
	private int objectLevel = 0;
	private int containmentLevel = 0;
	private final LinkedList<String> propertyNames = new LinkedList<String>();
	
	public JsonContextImpl(JsonCallback callback) {
		super(callback);
	}
	
	@Override
	public void reset() {
		super.reset();
		objectLevel = 0;
		containmentLevel = 0;
		propertyNames.clear();
	}
	
	@Override
	public void notifyBeginObject() {
		objectLevel++;
		containmentLevel++;
		super.notifyBeginObject();	
	}
	
	@Override
	public void notifyEndObject() {
		if(objectLevel > 0) {
			while(propertyNames.size() == objectLevel) {
				propertyNames.removeLast();
			}
			objectLevel--;
		}
		super.notifyEndObject(); 
		containmentLevel--;
	}

	@Override
	public void notifyBeginArray() {
		containmentLevel++;
		super.notifyBeginArray();
	}
	
	@Override
	public void notifyEndArray() {
		super.notifyEndArray();
		containmentLevel--;
	}
	
	public void notifyProperty(String propertyName) {
		if(propertyNames.size() == objectLevel) {
			propertyNames.removeLast();
		}
		propertyNames.add(propertyName);
		super.notifyProperty(propertyName);
	}

	@Override
	public int getLevel() {
		return containmentLevel;
	}
	
	@Override
	public String toString() {
		return getPath().toString();
	}

}
