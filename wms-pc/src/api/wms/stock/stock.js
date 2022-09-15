import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/stock/page',
        method: 'post',
        params: page,
        data: params
    })
}
export const getStockStatusList = () => {
    return request({
        url: '/api/wms/state/getStockStatus',
        method: 'get'
    })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/stock/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}
export const exportStockListCount = (data) => {
    return request({
        url: '/api/wms/stock/exportStockListCount',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}


export const importFile = (data) => {
    return request({
        url: '/api/wms/stock/import-data',
        method: 'post',
        data: data
    })
}

export const getStockDataByBoxCode = (data) => {
    return request({
        url: '/api/wms/stock/getStockDataByBoxCode',
        method: 'post',
        data: {
            boxCodeList: data
        }
    })
}

export const getStockDataByStockId = (data) => {
    return request({
        url: '/api/wms/stock/getStockDataByStockId',
        method: 'post',
        data: {
            stockId: data
        }
    })
}

export const move = (data) => {
    return request({
        url: '/api/wms/stock/move',
        method: 'post',
        data: data
    })
}

export const moveByBox = (data) => {
    return request({
        url: '/api/wms/stock/moveByBox',
        method: 'post',
        data: data
    })
}

export const stockFrozen = (data) => {
    return request({
        url: '/api/wms/stock/stockFrozen',
        method: 'post',
        data: data
    })
}

export const stockUnFrozen = (data) => {
    return request({
        url: '/api/wms/stock/stockUnFrozen',
        method: 'post',
        data: data
    })
}

export const pageBySerial = (page, params) => {
    return request({
        url: '/api/wms/stock/pageBySerial',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportBySerial = (data) => {
    return request({
        url: '/api/wms/stock/exportBySerial',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const pageSerial = (data, page) => {
    return request({
        url: '/api/wms/stock/pageSerial',
        method: 'post',
        data: data,
        params: page
    })
}

export const exportSerial = (data) => {
    return request({
        url: '/api/wms/stock/exportSerial',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}


