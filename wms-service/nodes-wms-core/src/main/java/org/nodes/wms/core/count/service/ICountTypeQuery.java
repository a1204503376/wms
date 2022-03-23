package org.nodes.wms.core.count.service;


import org.nodes.wms.core.count.vo.CountHeaderVO;

import java.util.List;

public interface ICountTypeQuery {
	void setParam(CountHeaderVO countHeader);
	List<CountHeaderVO> queryByCountType();
}
