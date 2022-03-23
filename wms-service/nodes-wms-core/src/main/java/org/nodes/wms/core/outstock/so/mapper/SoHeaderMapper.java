package org.nodes.wms.core.outstock.so.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import org.nodes.wms.core.basedata.entity.Sku;
import org.nodes.wms.core.outstock.so.dto.SoBillCountDTO;
import org.nodes.wms.core.outstock.so.entity.SoHeader;
import org.nodes.wms.core.outstock.so.vo.OutstockSkuRltVO;
import org.springframework.context.annotation.Primary;

import java.time.LocalDateTime;
import java.util.List;

/**
 *  Mapper 接口
 *
 * @author zhonglianshuai
 * @since 2020-02-10
 */
@Primary
public interface SoHeaderMapper extends BaseMapper<SoHeader> {

	/**
	 * 查询已完成订单量
	 *
	 * @param dateTime 指定日期
	 * @return 订单量
	 */
	int selectFinishBillCount(LocalDateTime dateTime);

	/**
	 * 查询已完成订单的物品总数
	 *
	 * @param dateTime 指定日期
	 * @return 物品总数
	 */
	int selectFinishSkuCount(LocalDateTime dateTime);

	/**
	 * 获取当前订单量
	 *
	 * @return 当前订单量
	 */
	List<SoBillCountDTO> selectBillCount();

	/**
	 * 获取指定时间内订单所有物品
	 *
	 * @return
	 */
	List<Sku> selectAllSku(LocalDateTime begin, LocalDateTime end);

	List<OutstockSkuRltVO> selectOutstockSku(LocalDateTime begin, LocalDateTime end);

}
