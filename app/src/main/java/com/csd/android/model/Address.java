package com.csd.android.model;

import java.io.Serializable;

import com.baidu.mapapi.model.LatLng;

public class Address implements Serializable {
	public String name;
	public String address;
	public double[] location = new double[2];
}
