package com.example.shopping.model;

import org.springframework.stereotype.Component;

@Component
public class FilterInfo {
	public double from;
	public double to;

	public FilterInfo() {
	}

	public FilterInfo(double from, double to) {
		this.from = from;
		this.to = to;
	}

	public double getFrom() {
		return from;
	}

	public void setFrom(double from) {
		this.from = from;
	}

	public double getTo() {
		return to;
	}

	public void setTo(double to) {
		this.to = to;
	}

}
