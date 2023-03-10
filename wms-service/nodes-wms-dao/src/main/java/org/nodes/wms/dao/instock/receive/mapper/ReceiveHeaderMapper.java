package org.nodes.wms.dao.instock.receive.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import org.nodes.wms.dao.instock.receive.dto.input.NotReceiveDetailPageQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePageQuery;
import org.nodes.wms.dao.instock.receive.dto.input.ReceivePdaQuery;
import org.nodes.wms.dao.instock.receive.dto.output.*;
import org.nodes.wms.dao.instock.receive.entities.ReceiveHeader;

import java.util.List;

/**
 * 收货管理 Mapper 接口
 */
public interface ReceiveHeaderMapper extends BaseMapper<ReceiveHeader> {
	IPage<ReceiveHeaderResponse> getPage(IPage<ReceiveHeaderResponse> page, @Param("query") ReceivePageQuery receivePageQuery);

	DetailReceiveHeaderResponse selectHeaderById(@Param("id") Long receiveId);

	List<ReceiveHeaderResponse> getReceiveHeaderResponseByQuery(@Param("query") ReceivePageQuery receivePageQuery);

	Page<ReceiveHeaderPdaResponse> getReceiveList(@Param("query") ReceivePdaQuery receivePdaQuery, IPage<ReceiveHeader> page);

	/**
	 * 未收货明细分页查询
	 *
	 * @param page                       分页参数
	 * @param notReceiveDetailPageQuery  未收货明细查询条件
	 * @param detailStatusList              接收状态
	 * @return 未收货明细数据
	 */
	IPage<NotReceiveDetailResponse> pageNotReceiveDetail(
		IPage<?> page,
		@Param("param") NotReceiveDetailPageQuery notReceiveDetailPageQuery,
		@Param("detailStatusList") List<Integer> detailStatusList);

	/**
	 * 根据条件获取未收货明细数据
	 *
	 * @param notReceiveDetailPageQuery    查询条件请求对象
	 * @param detailStatusList              接收状态
	 * @return 未收货明细数据
	 */
	List<NotReceiveDetailExcelResponse> listNotReceiveDetailByQuery(
		@Param("param") NotReceiveDetailPageQuery notReceiveDetailPageQuery, @Param("detailStatusList") List<Integer> detailStatusList);

	/**
	 * pc收货获取头表返回对象
	 *
	 * @param receiveId 收货单id
	 */
	ReceiveByPcResponse selectReceiveByPcResponse(Long receiveId);

	/**
	 * 根据ASN单id分页查询收货单头表信息
	 *
	 * @param asnBillId: ASN单id
	 * @param page: 分页参数
	 * @return Page<ReceiveHeaderResponse> 收货单头表分页信息
	 */
    Page<ReceiveHeaderResponse> selectReceiveHeaderForDetailByAsnBillId(IPage<?> page, @Param("asnBillId") Long asnBillId);
}
