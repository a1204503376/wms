package org.nodes.wms.core.count.wrapper;


import org.nodes.wms.core.count.service.ICountTypeQuery;

public class CountTypeQueryContext {

    private ICountTypeQuery countTypeQuery;

    public void setCountTypeQuery(ICountTypeQuery countTypeQuery) {
        this.countTypeQuery = countTypeQuery;
    }

    public void query(){
        if (countTypeQuery != null) {
			countTypeQuery.queryByCountType();
        }
    }
}
