package org.nodes.wms.dao.basics.sku.dto.output;



import java.io.Serializable;

/**
 * 规格型号下拉框返回对象
 **/
public class PdaSkuSelectResponse implements Serializable {

	private static final long serialVersionUID = -6365939402967791634L;
	/**
	 * 物品规格型号
	 */
	private String skuSpec;
	private Long id=0L;

	public String getLabel() {
		return skuSpec;
	}

	public Long getId() {
		return id;
	}

	public void setSkuSpec(String skuSpec) {
		this.skuSpec = skuSpec;
	}
}
