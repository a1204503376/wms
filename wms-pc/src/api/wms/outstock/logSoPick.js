import request from '@/router/axios'

export const getPage = (page,data) => {
    return request({
        url: '/api/wms/outstock/logSoPick/page',
        method: 'post',
        params: page,
        data: data
    })
}

export const exportExcel = (data) => {
    return request({
        url: '/api/wms/outstock/logSoPick/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const cancelOutstock = (ids) => {
    return request({
        url: '/api/wms/outstock/logSoPick/cancelOutstock',
        method: 'post',
        data: {
            idList: ids
        }
    })
}
