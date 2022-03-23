package org.nodes.wms.core.instock.asn.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.nodes.wms.core.instock.asn.entity.AsnHeader;
import org.springblade.core.tool.utils.DateUtil;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

/**
 * author: pengwei
 * date: 2021/7/9 15:42
 * description: AsnHeaderQueryDTO
 */
@Data
public class AsnHeaderQueryDTO extends AsnHeader {

	private String asnBillNo_like;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate postTime_ge;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateUtil.PATTERN_DATE)
	@DateTimeFormat(pattern = DateUtil.PATTERN_DATE)
	private LocalDate postTime_le;
}
