package com.epam.my;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class GraphPoint {
	//-----------------------------------------------------------------------------fields
	private String name;
	private HashSet<String> neighbourGroup;
	private ArrayList<Integer> lengthGroup;
	private ArrayList<Integer> costGroup;
	private int mark;
	private int fullCost;
	private String prevShortPoint;
	//-----------------------------------------------------------------------------constructors
	public GraphPoint(String name)
	{
		this.name = name;
		this.neighbourGroup = new HashSet<>();
		this.lengthGroup = new ArrayList<>();
		this.costGroup = new ArrayList<>();
		this.mark = -1;
	}
	//-----------------------------------------------------------------------------
	//-----------------------------------------------------------------------------methods
	public void addNeighbour(String neighbourName, int neighbourLength, int neighbourCost)
	{
		this.neighbourGroup.add(neighbourName);
		this.lengthGroup.add(neighbourLength);
		this.costGroup.add(neighbourCost);
	}

	public String getName() {
		return this.name;
	}

	public HashSet<String> getNeighbourGroup()
	{
		return this.neighbourGroup;
	}

	public Integer getLengthGroupElement(int index)
	{
		return this.lengthGroup.get(index);
	}

	public Integer getCostGroupElement(int index)
	{
		return this.costGroup.get(index);
	}

	public int getMark() {
		return this.mark;
	}

	public void setMark(int mark) {
		this.mark = mark;
	}

	public void setPrevShortPoint(String prevShortPoint) {
		this.prevShortPoint = prevShortPoint;
	}

	public String getPrevShortPoint() {
		return this.prevShortPoint;
	}

	public int getFullCost() {
		return this.fullCost;
	}

	public void setFullCost(int fullCost) {
		this.fullCost = fullCost;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		GraphPoint that = (GraphPoint) o;
		return this.name.equals(that.getName());
	}

	@Override
	public int hashCode() {
		return Objects.hash(name);
	}
}
