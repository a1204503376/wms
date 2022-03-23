package org.nodes.wms.core.count.wrapper;


import org.nodes.wms.core.count.enums.CountTypeEnum;
import org.nodes.wms.core.count.service.ICountTypeQuery;
import org.nodes.wms.core.count.service.impl.ABCCountTypeQuery;
import org.nodes.wms.core.count.service.impl.ChangeCountTypeQuery;
import org.nodes.wms.core.count.service.impl.PointToCountTypeQuery;
import org.nodes.wms.core.count.vo.CountHeaderVO;

import java.util.HashMap;
import java.util.Map;

public class CountTypeQueryFactory {

    private static Map<Integer, ICountTypeQuery> countTypeQueryMap;
    private CountTypeQueryFactory(){}
    static {
        countTypeQueryMap = new HashMap<>();
        countTypeQueryMap.put(CountTypeEnum.ABC.getIndex(),new ABCCountTypeQuery());
        countTypeQueryMap.put(CountTypeEnum.CHANGE.getIndex(),new ChangeCountTypeQuery());
		countTypeQueryMap.put(CountTypeEnum.POINT_TO.getIndex(),new PointToCountTypeQuery());
    }

    public static ICountTypeQuery getCountTypeQuery(Integer searchType, CountHeaderVO countHeader){
		ICountTypeQuery countTypeQuery = countTypeQueryMap.get(searchType);
		countTypeQuery.setParam(countHeader);
		return countTypeQuery;
    }
}
