package org.nodes.wms.dao.basics.location.constant;

/**
 * 库位常量类
 **/
public class LocationConstant {

	public static String[] getLocTypes(){
		return new String[]{"STAGE","QC","PICKTO","PACK","UNKNOWN","INTRANSIT"};
	}
}
