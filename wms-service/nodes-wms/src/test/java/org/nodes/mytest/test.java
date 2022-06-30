package org.nodes.mytest;

import java.math.BigDecimal;

public class test {
	public static void main(String[] args) {
		BigDecimal i  = new BigDecimal(0);
		BigDecimal a  = new BigDecimal(58.0000);
		BigDecimal b = new BigDecimal(2.000);
		i = i.add(a);
		i = i.add(b);
		System.out.println(i);
	}
}
