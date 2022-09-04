package org.nodes.wms.dao.basics.location.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.nodes.core.constant.DictKVConstant;
import org.springblade.core.tenant.mp.TenantEntity;
import org.springblade.core.tool.utils.Func;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 库位管理 实体类
 *
 * @author nodesc
 */
@Data
@TableName("wms_location")
public class Location extends TenantEntity {

	private static final long serialVersionUID = -1439112217262101365L;

	/**
	 * 库位ID
	 */
	@TableId(value = "loc_id", type = IdType.ASSIGN_ID)
	private Long locId;

	/**
	 * 库区ID
	 */
	private Long zoneId;

	/**
	 * 库房ID
	 */
	private Long whId;

	/**
	 * 库位编码
	 */
	private String locCode;

	/**
	 * 库位类型
	 */
	private Integer locType;

	/**
	 * 库位种类
	 */
	private Integer locCategory;

	/**
	 * 库位处理
	 */
	private Integer locHandling;

	/**
	 * 分配区
	 */
	private String allocationZone;

	/**
	 * 使用状态
	 */
	private Integer locFlag;

	/**
	 * ABC分类
	 */
	private Integer abc;

	/**
	 * 库存差异是否自动冻结货位
	 */
	private String lockFlag;

	/**
	 * 状态
	 */
	private Integer locStatus;

	/**
	 * 路线顺序
	 */
	private Integer logicAllocation;

	/**
	 * 长
	 */
	private BigDecimal locLength;

	/**
	 * 宽
	 */
	private BigDecimal locWide;

	/**
	 * 高
	 */
	private BigDecimal locHigh;

	/**
	 * 货架层
	 */
	private String locLevel;

	/**
	 * X坐标
	 */
	private Integer xCode;

	/**
	 * Y坐标
	 */
	private Integer yCode;

	/**
	 * Z坐标
	 */
	private Integer zCode;

	/**
	 * 校验位数
	 */
	private String checkDigit;

	/**
	 * 货架列
	 */
	private String locColumn;

	/**
	 * 货架排
	 */
	private String locBank;

	/**
	 * 上架顺序
	 */
	private Integer putOrder;

	/**
	 * 适用的容器类型
	 */
	private Long lpnTypeId;

	/**
	 * 定方位
	 */
	private String orientation;

	/**
	 * 容量
	 */
	private BigDecimal capacity;

	/**
	 * 载重量
	 */
	private BigDecimal loadWeight;

	/**
	 * 最大存放件数
	 */
	private Integer itemNum;

	/**
	 * 最大存放托数
	 */
	private Integer trayNum;

	/**
	 * 盘点单编码
	 */
	private String countBillNo;

	/**
	 * 库位共享宽度
	 */
	private String shareWidth;

	/**
	 * 库位共享重量
	 */
	private String shareWeight;

	/**
	 * 混放货品
	 */
	private String locSkuMix;

	/**
	 * 混放批号
	 */
	private String locLotNoMix;

	/**
	 * 混放批属性1
	 */
	private String locLotMix1;
	/**
	 * 混放批属性2
	 */
	private String locLotMix2;
	/**
	 * 混放批属性3
	 */
	private String locLotMix3;
	/**
	 * 混放批属性4
	 */
	private String locLotMix4;
	/**
	 * 混放批属性5
	 */
	private String locLotMix5;
	/**
	 * 混放批属性6
	 */
	private String locLotMix6;
	/**
	 * 混放批属性7
	 */
	private String locLotMix7;
	/**
	 * 混放批属性8
	 */
	private String locLotMix8;
	/**
	 * 混放批属性9
	 */
	private String locLotMix9;
	/**
	 * 混放批属性10
	 */
	private String locLotMix10;
	/**
	 * 混放批属性11
	 */
	private String locLotMix11;
	/**
	 * 混放批属性12
	 */
	private String locLotMix12;
	/**
	 * 混放批属性13
	 */
	private String locLotMix13;
	/**
	 * 混放批属性14
	 */
	private String locLotMix14;
	/**
	 * 混放批属性15
	 */
	private String locLotMix15;
	/**
	 * 混放批属性16
	 */
	private String locLotMix16;
	/**
	 * 混放批属性17
	 */
	private String locLotMix17;
	/**
	 * 混放批属性18
	 */
	private String locLotMix18;
	/**
	 * 混放批属性19
	 */
	private String locLotMix19;
	/**
	 * 混放批属性20
	 */
	private String locLotMix20;
	/**
	 * 混放批属性21
	 */
	private String locLotMix21;
	/**
	 * 混放批属性22
	 */
	private String locLotMix22;
	/**
	 * 混放批属性23
	 */
	private String locLotMix23;
	/**
	 * 混放批属性24
	 */
	private String locLotMix24;
	/**
	 * 混放批属性25
	 */
	private String locLotMix25;
	/**
	 * 混放批属性26
	 */
	private String locLotMix26;
	/**
	 * 混放批属性27
	 */
	private String locLotMix27;
	/**
	 * 混放批属性28
	 */
	private String locLotMix28;
	/**
	 * 混放批属性29
	 */
	private String locLotMix29;
	/**
	 * 混放批属性30
	 */
	private String locLotMix30;

	/**
	 * 上次库位盘点发布时间
	 */
	private LocalDateTime lastCycleDate;

	/**
	 * 上次盘点货位日期
	 */
	private LocalDateTime lastLocCountDate;

	/**
	 * 系统自动冻结时关联的信息
	 */
	private String locFlagDesc;

	/**
	 * 判断库位是否可以上架库存
	 *
	 * @return true表示可以上架库存
	 */
	public boolean enableStock() {
		return Func.isNull(locFlag)
			|| DictKVConstant.LOC_FLAG_NORMAL.equals(locFlag);
	}

}
