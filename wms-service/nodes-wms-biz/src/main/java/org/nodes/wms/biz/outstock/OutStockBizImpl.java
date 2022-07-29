package org.nodes.wms.biz.outstock;

import org.nodes.wms.dao.outstock.so.dto.input.PickByPcRequest;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

/**
 * 出库业务实现类
 * 
 * @author nodesc
 */
@Service
@RequiredArgsConstructor
public class OutStockBizImpl implements OutStockBiz {

    @Override
    public void pickByPc(PickByPcRequest request) {
        // TODO Auto-generated method stub
        // 单据状态为终结状态，则不能进行拣货
        // 已经分配过的不能进行PC拣货
        //
    }

}
