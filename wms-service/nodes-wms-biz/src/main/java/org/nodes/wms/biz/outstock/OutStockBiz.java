package org.nodes.wms.biz.outstock;

import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;

public interface OutStockBiz {

    /**
     * PC按件拣货
     * 
     * @param request
     */
    void pcPickByPcs(PickByPcRequest request);
}
