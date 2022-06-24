package org.nodes.wms.biz.basics.warehouse.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.vo.DeptVO;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.dao.basics.location.LocationDao;
import org.nodes.wms.dao.basics.location.constant.LocationConstant;
import org.nodes.wms.dao.basics.location.entities.Location;
import org.nodes.wms.dao.basics.location.enums.LocTypeEnum;
import org.nodes.wms.dao.basics.warehouse.SysAuthDao;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.SysAuth;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.nodes.wms.dao.basics.zone.ZoneDao;
import org.nodes.wms.dao.basics.zone.entities.Zone;
import org.nodes.wms.dao.basics.zone.enums.ZoneTypeEnum;
import org.springblade.core.log.exception.ServiceException;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.AesUtil;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库房管理 业务类
 */
@Service
@RequiredArgsConstructor
public class WarehouseBizImpl implements WarehouseBiz {
	private final WarehouseDao warehouseDao;

	private final ZoneDao zoneDao;

	private final LocationDao locationDao;

	private final SysAuthDao sysAuthDao;

	private final IDeptService deptService;

	private final String AES_KEY = "mKzlYJ9tOXJn8uG16wTHXbQuD7i0lBF0";
	/**
	 * 全局获取DeptVO
	 */
	List<DeptVO> deptVOList = new ArrayList<>();

	@Override
	public List<WarehouseResponse> getWarehouseSelectResponseList() {
		return warehouseDao.getWarehouseSelectResponseList();
	}

	@Override
	public Warehouse findById(Long warehouseId) {

		return warehouseDao.findById(warehouseId);
	}

	@Override
	public Warehouse findByCode(String whCode) {
		return warehouseDao.findByCode(whCode);
	}

	@Override
	public List<Warehouse> getWarehouseByUserId(BladeUser user) {
		//清空全局的deptVOList
		deptVOList = new ArrayList<>();
		//获取全部的机构
		List<Dept> deptList = deptService.list();
		//根据当前用户查询出当前用户所在的机构
		Dept byId = deptService.getById(Long.parseLong(user.getDeptId()));
		//复制当前用户的机构到DeptVO对象
		DeptVO deptVO = BeanUtil.copy(byId, DeptVO.class);
		//将当前用户的机构vo对象添加到deptVOList
		deptVOList.add(deptVO);
		//将全部的机构的实体类集合都转换成DeptVO
		List<DeptVO> copy = BeanUtil.copy(deptList, DeptVO.class);
		//递归获取跟当前用户有关系的机构
		makeTree(copy, Long.parseLong(user.getDeptId()));
		//将跟当前用户有关系的机构去重并组成Long类型的集合
		List<Long> longList = deptVOList.stream().map(DeptVO::getId).distinct().collect(Collectors.toList());
		//获取跟当前用户有关系的库房
		List<Warehouse> warehouseList = warehouseDao.getListByDeptId(longList);
		return warehouseList;
	}

	//递归
	private List<DeptVO> makeTree(List<DeptVO> departmentList, Long pId) {
		//子类
		List<DeptVO> children = departmentList.stream().filter(x -> x.getParentId().longValue() == pId.longValue()).collect(Collectors.toList());
		//后辈中的非子类
		List<DeptVO> successor = departmentList.stream().filter(x -> x.getParentId().longValue() != pId.longValue()).collect(Collectors.toList());
		children.forEach(x ->
			{
				deptVOList.add(x);
				makeTree(successor, x.getId()).forEach(
					y -> {
						x.getChildren().add(y);
						deptVOList.add(y);
					}
				);
			}
		);
		return children;
	}

	@Override
	public void afterNewWarehouse(Warehouse warehouse) {
		Zone zoneParam = new Zone();
		zoneParam.setWhId(warehouse.getWhId());
		zoneParam.setZoneCode(warehouse.getWhCode() + "-" + ZoneTypeEnum.VIRTUAL);
		zoneParam.setZoneName(ZoneTypeEnum.VIRTUAL.getName());
		zoneParam.setZoneType(ZoneTypeEnum.VIRTUAL.getCode());
		zoneParam.setCreateDept(warehouse.getDeptId());
		Zone zone = zoneDao.saveOrUpdateZone(zoneParam);

		Arrays.stream(LocationConstant.getLocTypes()).forEach(item -> {
			Location locationParam = new Location();
			locationParam.setWhId(warehouse.getWhId());
			locationParam.setZoneId(zone.getZoneId());
			locationParam.setLocType(LocTypeEnum.Virtual.getCode());
			locationParam.setLocCode(warehouse.getWhCode() + "-" + item);
			locationParam.setCreateDept(warehouse.getDeptId());
			locationDao.saveOrUpdateLocation(locationParam);
		});
	}

	@Override
	public String authorized(String authorizeString) {
		// 加密
		String encryptValue = AesUtil.encryptToHex(authorizeString, AES_KEY);

		SysAuth sysAuth = new SysAuth();
		sysAuth.setContent(encryptValue);
		sysAuthDao.insertOne(sysAuth);
		return encryptValue;
	}

	@Override
	public void valiAuthorization() {
		// 解密
		String decryptValue = AesUtil.decryptFormHexToString(sysAuthDao.getOne().getContent(), AES_KEY);
		JSONObject jsonObject = JSON.parseObject(decryptValue);
		// 库房授权个数
		int authorizations = jsonObject.getInteger("authorizations");
		// 库房总数
		int count = warehouseDao.countWarehouse();
		if (count >= authorizations) {
			throw new ServiceException("新增失败，库房数量已达到库房授权数量");
		}
	}
}
