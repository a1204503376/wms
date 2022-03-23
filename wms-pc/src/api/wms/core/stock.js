import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/core/stock/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

// 库存冻结
export const lockByStock = (ids) => {
    return request({
        url: '/api/wms/core/stock/lock',
        method: 'post',
        params: {
            ids,
        }
    })
}

// 库存解冻
export const unlockByStock = (ids) => {
    return request({
        url: '/api/wms/core/stock/unlock',
        method: 'post',
        params: {
            ids,
        }
    })
}
// 批次冻结/解冻
export const lockByLot = (ids) => {
    return request({
        url: '/api/wms/core/stock/lock/lot',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/core/stock/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}
export const accounts = (ids, startTime, endTime, group, accountsVo,stockIds) => {
    return request({
        url: '/api/wms/core/stock/accounts',
        method: 'post',
        params: {
            ids,
            startTime,
            endTime,
            group,
            ...accountsVo,
            stockIds:stockIds
        }
    })
}
//内部转移
export const internalTransfer = (data) => {
    return request({
        url: '/api/wms/core/stock/internalTransfer',
        method: 'post',
        data: data
    })

}
export const createRelenishOrder = (data) => {
    return request({
        url: '/api/wms/core/relenishment/create',
        method: 'post',
        data: data
    })

}
export const erpCompare = () => {
    return request({
        url: '/api/wms/core/stock/erpCompare',
        method: 'get'
    })
}

export const stockDetails=(current, size, params)=>{
    return request({
        url:'/api/wms/core/stockDetail/stockDetails',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}


// 库存明细冻结
export const stockDetailsLock = (ids) => {
    return request({
        url: '/api/wms/core/stockDetail/lock',
        method: 'post',
        params: {
            ids,
        }
    })
}

// 库存明细解冻
export const stockDetailsunLock = (ids) => {
    return request({
        url: '/api/wms/core/stockDetail/unlock',
        method: 'post',
        params: {
            ids,
        }
    })
}

// 批次冻结
export const lotLock = (lotNumber) => {
    return request({
        url: '/api/wms/core/lot/lock',
        method: 'post',
        params: {
            lotNumber,
        }
    })
}

// 批次解冻
export const lotunLock = (lotNumber) => {
    return request({
        url: '/api/wms/core/lot/unlock',
        method: 'post',
        params: {
            lotNumber,
        }
    })
}
