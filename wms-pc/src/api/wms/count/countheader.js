import request from '@/router/axios';

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/count/countheader/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
//获取盘点库位
export const getLocIdApi = (zoneId) => {
    return request({
        url: "/api/wms/warehouse/location/select?zoneId=" + zoneId,
        method: 'get',
    })
}


//盘点单编码
export const getCNO = () => {
    return request({
        url: '/api/wms/count/countheader/getCNo',
        method: 'post',
    })
}

//盘点默认模式
export const getDefaltMode = () => {
    return request({
        url: '/api/wms/count/countheader/getDefaltMode',
        method: 'post',
    })
}
//执行人
export const getUser = () => {
    return request({
        url: '/api/blade-user/list',
        method: 'get',
    })
}
export const getDetail = (countBillId) => {
    return request({
        url: '/api/wms/count/countheader/detail',
        method: 'get',
        params: {
            countBillId,
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/count/countheader/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const abolishRandomCount = (ids) => {
    return request({
        url: '/api/wms/count/countheader/abolishRandomCount',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const add = (row) => {
    return request({
        url: '/api/wms/count/countheader/save',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/count/countheader/submit',
        method: 'post',
        data: row
    })
}
//查询库房
// export const searchSku = (row) => {
//     return request({
//         url: '/api/wms/count/countheader/querylocation',
//         method: 'post',
//         data: row
//     })
// }

//查询库房
export const searchSku = (row) => {
    return request({
        url: '/api/wms/count/countheader/findStockCountDetailBySkuSpec',
        method: 'post',
        data: row
    })
}


export const countTask = (row) => {
    return request({
        url: '/api/wms/count/countheader/countTask',
        method: 'post',
        data: row
    })
}
export const completeTask = (ids) => {
    return request({
        url: '/api/wms/count/countheader/completeTask',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const differenceReport = (ids) => {
    return request({
        url: '/api/wms/count/countheader/differenceReport',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const createDifferenceOrder = (ids) => {
    return request({
        url: '/api/wms/count/countheader/differenceOrder',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const completeDifferenceReport = (ids) => {
    return request({
        url: '/api/wms/count/countheader/completeDifferenceReport',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const randomCountDifferenceReport = (ids) => {
    return request({
        url: '/api/wms/count/countheader/randomCountDifferenceReport',
        method: 'post',
        data: ids

    })
}

export const repeat = (ids) => {
    return request({
        url: '/api/wms/count/countheader/repeat',
        method: 'post',
        params: {
            ids
        }
    })
}

export const abolish = (ids) => {
    return request({
        url: '/api/wms/count/countheader/abolish',
        method: 'post',
        params: {
            ids
        }
    })
}
export const exportFile = (id) => {
    return request({
        url: '/api/wms/count/countreport/export',
        method: 'get',
        params: {
            id
        },
        responseType: 'blob'
    })
}
