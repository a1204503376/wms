import request from '@/router/axios'

export const getPage = (page, data) => {
    return request({
        url: '/api/wms/outstock/soBill/page',
        method: 'post',
        params: page,
        data: data
    })
}

export const add = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/add',
        method: 'post',
        data: data
    })
}

export const remove = (soBillIdList) => {
    return request({
        url: '/api/wms/outstock/soBill/remove',
        method: 'post',
        data: {
            idList: soBillIdList
        }
    })
}

export const detailByEdit = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detailByEdit',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const edit = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/edit',
        method: 'post',
        data: data
    })
}

export const getHeaderForDetail = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_header',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const getDetailForDetail = (page, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_detail',
        method: 'post',
        params: page,
        data: {
            soBillId: soBillId
        }
    })
}

export const getLogSoPickForDetail = (page, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_logSoPick',
        method: 'post',
        params: page,
        data: {
            soBillId: soBillId
        }
    })
}

export const getSoLogForDetail = (page, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_log',
        method: 'post',
        params: page,
        data: {
            soBillId: soBillId
        }
    })
}

export const exportData = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const closeSoBill = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/close',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}
export const getSoHeaderByPickPc = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/getSoHeaderByPickPc',
        method: 'post',
        data: data
    })
}

export const getSoBillDataByDistribution = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/getSoBillDataByDistribution',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}


export const getSoPickPlanData = (soBillId, soDetailId) => {
    return request({
        url: '/api/wms/outstock/soBill/getSoPickPlan',
        method: 'post',
        data: {
            soBillId: soBillId,
            soDetailId: soDetailId
        }
    })
}

export const getSoDetailAndStock = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/getSoDetailAndStock',
        method: 'post',
        data: data
    })
}

export const getLineNoAndSkuSelectList = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/getLineNoAndSkuSelectList',
        method: 'get',
        params: {
            soBillId
        }
    })
}

export const automaticAssign = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/automaticAssign',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const cancelAll = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/cancelAll',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const issued = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/issued',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const getStockByDistributeAdjust = (skuId, skuLot1, skuLot2, skuLot4, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/getStockByDistributeAdjust',
        method: 'post',
        data: {
            skuId: skuId,
            skuLot1: skuLot1,
            skuLot2: skuLot2,
            skuLot4: skuLot4,
            soBillId: soBillId
        }
    })
}


export const getStockAgvAndPick = (skuId, skuLot1, skuLot4, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/getStockAgvAndPick',
        method: 'post',
        data: {
            skuId: skuId,
            skuLot1: skuLot1,
            skuLot4: skuLot4,
            soBillId: soBillId
        }
    })
}

export const getSerialSelectResponseList = (stockId) => {
    return request({
        url: '/api/wms/outstock/soBill/getSerialSelectResponseList',
        method: 'get',
        params: {
            stockId
        }
    })
}

export const pickByPc = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/pickByPc',
        method: 'post',
        data: data
    })
}

export const saveAssign = (soBillId, soDetailId, soPickPlanList, stockIdAndSoPickPlanQtyList) => {
    let soBillDistributedRequest = {
        soBillId: soBillId,
        soDetailId: soDetailId,
        oldSoPickPlanList: soPickPlanList,
        stockIdAndSoPickPlanQtyList: stockIdAndSoPickPlanQtyList
    }
    return request({
        url: '/api/wms/outstock/soBill/saveAssign',
        method: 'post',
        data: soBillDistributedRequest
    })
}

// 根据箱码查看库存
export const getAssignDistributeStock = (boxCode, whId, locCode) => {
    return request({
        url: '/api/wms/outstock/soBill/showDistributeAdjustStock',
        method: 'post',
        data: {
            boxCode: boxCode,
            whId: whId,
            locCode: locCode
        }
    })
}




