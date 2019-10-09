package com.example.piccarto;

public class Overlay {
    //fields
    private int overlayID;
    private byte[] imageByteArray;
    private float anchorX, anchorY, height, width, bearing;
    private double positionA, position0;
    private String owner;
    //constructors

    public Overlay() {}

    public Overlay(int overlayID, byte[] imageByteArray, float anchorX, float anchorY, float height, float width, float bearing, double positionA, double position0, String owner) {
        this.overlayID = overlayID;
        this.imageByteArray = imageByteArray;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.height = height;
        this.width = width;
        this.bearing = bearing;
        this.positionA = positionA;
        this.position0 = position0;
        this.owner = owner;
    }

    public int getOverlayID() {
        return overlayID;
    }

    public void setOverlayID(int overlayID) {
        this.overlayID = overlayID;
    }

    public byte[] getImageByteArray() {
        return imageByteArray;
    }

    public void setImageByteArray(byte[] imageByteArray) {
        this.imageByteArray = imageByteArray;
    }

    public float getAnchorX() {
        return anchorX;
    }

    public void setAnchorX(float anchorX) {
        this.anchorX = anchorX;
    }

    public float getAnchorY() {
        return anchorY;
    }

    public void setAnchorY(float anchorY) {
        this.anchorY = anchorY;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getBearing() {
        return bearing;
    }

    public void setBearing(float bearing) {
        this.bearing = bearing;
    }

    public double getPositionA() {
        return positionA;
    }

    public void setPositionA(double positionA) {
        this.positionA = positionA;
    }

    public double getPosition0() {
        return position0;
    }

    public void setPosition0(double position0) {
        this.position0 = position0;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
