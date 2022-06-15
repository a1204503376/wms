package org.nodes.wms.biz.basics.warehouse.impl;

import lombok.RequiredArgsConstructor;
import org.nodes.core.base.entity.Dept;
import org.nodes.core.base.service.IDeptService;
import org.nodes.core.base.vo.DeptVO;
import org.nodes.wms.biz.basics.warehouse.WarehouseBiz;
import org.nodes.wms.dao.basics.warehouse.WarehouseDao;
import org.nodes.wms.dao.basics.warehouse.dto.output.WarehouseResponse;
import org.nodes.wms.dao.basics.warehouse.entities.Warehouse;
import org.springblade.core.secure.BladeUser;
import org.springblade.core.tool.utils.BeanUtil;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 库房管理 业务类
 */
@Service
@RequiredArgsConstructor
public class WarehouseBizImpl implements WarehouseBiz {
	private  final WarehouseDao warehouseDao;
	private  final IDeptService deptService;
	/**
	 * 全局获取DeptVO
	 */
	List<DeptVO> deptVOList=new ArrayList<>();

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
		deptVOList=new ArrayList<>();
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
		makeTree(copy,Long.parseLong(user.getDeptId()));
		//将跟当前用户有关系的机构去重并组成Long类型的集合
		List<Long> longList = deptVOList.stream().map(DeptVO::getId).distinct().collect(Collectors.toList());
		//获取跟当前用户有关系的库房
		List<Warehouse> warehouseList = warehouseDao.getListByDeptId(longList);
		return warehouseList;
	}

	//递归
	private  List<DeptVO> makeTree(List<DeptVO> departmentList, Long pId) {
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

}
