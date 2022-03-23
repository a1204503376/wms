package org.nodes.wms.core.outstock.so.vo;

import lombok.Data;
import org.nodes.wms.core.outstock.so.entity.SoHeader;

@Data
public class WellenSoHeaderVo {

	public Long wellenId;

	public SoHeader soHeader;
}
