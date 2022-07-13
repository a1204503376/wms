package org.nodes.wms.dao.basics.skulot.dto.output;

import lombok.Data;
import org.nodes.wms.dao.basics.skulot.entities.SkuLotBaseEntity;

import java.io.Serializable;

/**
 * PDA批属性组件响应对象
 **/
@Data
public class FindAllSkuLotResponse extends SkuLotBaseEntity implements Serializable {
	private static final long serialVersionUID = -2436813039015326126L;
	private Integer numberOfOpen;
}
