package graph;

import data.DataMutant;

public class GraphEdge {
	private DataMutant start, end;
	private int similarity;
	
	public GraphEdge(DataMutant _start, DataMutant _end, int _similarity)
	{
		this.start = _start;
		this.end = _end;
		this.similarity = _similarity;
	}
	
	public int getSimilarity()
	{
		return this.similarity;
	}
	
	public String toString()
	{
		return this.start.getName() + " to " + this.end.getName();
	}
}
