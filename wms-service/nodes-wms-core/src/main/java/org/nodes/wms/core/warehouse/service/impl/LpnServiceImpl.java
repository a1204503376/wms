
package org.nodes.wms.core.warehouse.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.nodes.core.base.cache.DictCache;
import org.nodes.core.base.entity.Dict;
import org.nodes.core.constant.DictCodeConstant;
import org.nodes.core.tool.cache.SerialNoCache;
import org.nodes.core.tool.entity.DataVerify;
import org.nodes.core.tool.utils.ValidationUtil;
import org.nodes.wms.core.warehouse.cache.LpnCache;
import org.nodes.wms.core.warehouse.dto.LpnDTO;
import org.nodes.wms.core.warehouse.entity.Lpn;
import org.nodes.wms.core.warehouse.entity.LpnState;
import org.nodes.wms.core.warehouse.excel.LpnExcel;
import org.nodes.wms.core.warehouse.mapper.LpnMapper;
import org.nodes.wms.core.warehouse.service.ILpnService;
import org.nodes.wms.core.warehouse.service.ILpnStateService;
import org.nodes.wms.core.warehouse.vo.LpnVO;
import org.nodes.wms.core.warehouse.wrapper.LpnWrapper;
import org.springblade.core.excel.util.ExcelUtil;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.mp.base.BaseServiceImpl;
import org.springblade.core.mp.support.Condition;
import org.springblade.core.tool.utils.*;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 容器服务实现类
 *
 * @author liangmei
 * @since 2019-12-10
 */
@Service
@Primary
@Transactional(propagation = Propagation.NESTED, isolation = Isolation.DEFAULT, rollbackFor = Exception.class)
public class LpnServiceImpl<M extends LpnMapper, T extends Lpn>
	extends BaseServiceImpl<LpnMapper, Lpn>
	implements ILpnService {


	@Override
	public boolean saveOrUpdate(Lpn lpn) {
		if (Func.isEmpty(lpn.getLpnId())) {
			Lpn one = this.getOne(Condition.getQueryWrapper(new Lpn()).lambda().eq(Lpn::getLpnCode, lpn.getLpnCode()));
			if (Func.isNotEmpty(one)) {
				throw new ServiceException("容器编码已存在！");
			}
			this.save(lpn);
		} else {
			this.updateById(lpn);
		}
		return true;
	}

	@Override
	public List<LpnVO> selectList(Lpn lpn) {
		return LpnWrapper.build().listVO(super.list(this.getSelectQueryWrapper(lpn)));
	}

	@Override
	public IPage<LpnVO> selectPage(IPage<Lpn> page, Lpn lpn) {
		IPage<Lpn> pages = super.page(page, this.getSelectQueryWrapper(lpn));
		return LpnWrapper.build().pageVO(pages);
	}

	@Override
	public void exportExcel(HashMap<String, Object> params, HttpServletResponse response) {
		//查询库房信息
		List<LpnVO> lpnList = LpnWrapper.build().listVO(this.list(Condition.getQueryWrapper(params, Lpn.class)));

		List<LpnExcel> lpnExportList = new ArrayList<>();
		for (Lpn lpn : lpnList) {
			// 计算最大循环次数，最少循环一次
			Integer maxLength = 1;
			for (int i = 0; i < maxLength; i++) {
				LpnExcel lpnExcel = BeanUtil.copy(lpn, LpnExcel.class);
				//企业类型
				if (Func.isNotEmpty(lpn.getLpnType())) {
					Dict dict = DictCache.list(DictCodeConstant.ENTERPRISE_TYPE).stream().filter(u -> {
						return Func.equals(u.getDictKey(), lpn.getLpnType());
					}).findFirst().orElse(null);

					String lpnType = Func.isEmpty(dict) ? StringPool.EMPTY : dict.getDictValue();
					lpnExcel.setLpnType(Func.toInt(lpnType));
				}
				lpnExportList.add(lpnExcel);
			}
		}
		ExcelUtil.export(response, "库房", "库房数据表", lpnExportList, LpnExcel.class);
	}

	@Override
	public List<DataVerify> validExcel(List<LpnExcel> dataList) {
		List<DataVerify> dataVerifyList = new ArrayList<>();

		Map<String, LpnDTO> cache = new HashMap<>();
		int index = 1;
		for (LpnExcel lpnExcel : dataList) {
			DataVerify dataVerify = new DataVerify();
			dataVerify.setIndex(index);
			// 封装成DTO
			LpnDTO lpnDTO = LpnWrapper.build().entityDTO(lpnExcel);

			// 开始效验数据
			ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(lpnDTO);

			// 容器编码唯一性验证
			//Lpn findLpn = LpnCache.getByCode(lpnDTO.getLpnCode());
			Lpn findLpn = super.list(Condition.getQueryWrapper(new Lpn())
				.lambda()
				.eq(Lpn::getLpnCode, lpnDTO.getLpnCode())
			).stream().findFirst().orElse(null);

			if (Func.isNotEmpty(findLpn)) {
				validResult.addError("lpnCode", "容器编码[" + lpnDTO.getLpnCode() + "]已存在");
			}

			if (validResult.hasErrors()) {
				dataVerify.setMessage(StringUtil.join(validResult.getAllErrors()));
			} else {
				cache.put(lpnDTO.getLpnCode(), lpnDTO);
				dataVerify.setCacheKey(lpnDTO.getLpnCode());
			}
			dataVerifyList.add(dataVerify);
			index++;
		}
		if (Func.isNotEmpty(cache)) {
			// 存储数据到redis缓存中
			for (String code : cache.keySet()) {
				LpnCache.put(code, cache.get(code));
			}
		}

		return dataVerifyList;
	}

	@Override
	public boolean importData(List<DataVerify> dataVerifyList) {
		if (Func.isEmpty(dataVerifyList)) {
			throw new ServiceException("没有数据可以保存！");
		}
		for (DataVerify dataVerify : dataVerifyList) {
			if (ObjectUtil.isEmpty(dataVerify)) {
				continue;
			}
			LpnDTO lpnDTO = LpnCache.get(dataVerify.getCacheKey());
			if (ObjectUtil.isEmpty(lpnDTO)) {
				continue;
			}
			if (this.saveOrUpdate(lpnDTO)) {
				LpnCache.remove(lpnDTO.getLpnCode());
				//LpnCache.saveOrUpdate(lpnDTO);
			}
		}
		return true;
	}

	/**
	 * 获取模糊查询构造器
	 *
	 * @param lpn 查询条件
	 * @return 查询构造器
	 */
	private QueryWrapper<Lpn> getSelectQueryWrapper(Lpn lpn) {
		QueryWrapper<Lpn> queryWrapper = new QueryWrapper<>();
		if (Func.isNotEmpty(lpn.getLpnCode())) {
			queryWrapper.lambda().like(Lpn::getLpnCode, lpn.getLpnCode());
		}
		if (Func.isNotEmpty(lpn.getLpnName())) {
			queryWrapper.lambda().like(Lpn::getLpnName, lpn.getLpnName());
		}
		if (Func.isNotEmpty(lpn.getLpnType())) {
			queryWrapper.lambda().eq(Lpn::getLpnType, lpn.getLpnType());
		}
		return queryWrapper;
	}


	/**
	 * 创建lpnCode
	 *
	 * @return lpnCode
	 */
	public String create() {
		long count = 1;
		Lpn lpn = new Lpn();
		LpnState lpnState = new LpnState();
		ILpnStateService lpnStateService = SpringUtil.getBean(ILpnStateService.class);
		while (count != 0L) {
			String lpnCode = SerialNoCache.getLpnCode();
			count = super.list(Condition.getQueryWrapper(new Lpn())
				.lambda()
				.eq(Lpn::getLpnCode, lpnCode)).stream().count();
			if (count == 0L) {
				lpn.setLpnCode(lpnCode);
				lpn.setLpnType(2);
				super.save(lpn);
				lpnState.setLpnCode(lpnCode);
				lpnState.setLpnState(0);
				lpnState.setParentId(0L);
				lpnStateService.save(lpnState);
			}
		}
		return lpn.getLpnCode();
	}
}
