package com.example.piccarto;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "overlays")
public class Overlay {
    //fields
    @PrimaryKey (autoGenerate = true)
    private int overlayID;
    @ColumnInfo(name = "photo_path")//apparently it is unnecessary to annotate each field unless column name is different that variable name
    private String photoPath;
    @ColumnInfo(name = "anchorX")
    private float anchorX;
    @ColumnInfo(name = "anchorY")
    private float anchorY;
    @ColumnInfo(name = "width")
    private float width;
    @ColumnInfo(name = "bearing")
    private float bearing;
    @ColumnInfo(name = "positionO")
    private double position0;
    @ColumnInfo(name = "positionA")
    private double positionA;
    @ColumnInfo(name = "owner")
    private String owner;

    //constructors

/*    public Overlay(String photoPath) {
        this.photoPath = photoPath;
    }

    public Overlay(String photoPath, float anchorX, float anchorY, float width, float bearing, double positionA, double position0, String owner) {
        this.photoPath = photoPath;
        this.anchorX = anchorX;
        this.anchorY = anchorY;
        this.width = width;
        this.bearing = bearing;
        this.positionA = positionA;
        this.position0 = position0;
        this.owner = owner;
    }*/

    public int getOverlayID() {
        return overlayID;
    }

    public void setOverlayID(int overlayID) {
        this.overlayID = overlayID;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
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
