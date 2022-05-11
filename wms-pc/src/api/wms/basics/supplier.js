import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/suppliers/page',
        method: 'post',
        params:page,
        data: params
    })
}

export const remove = (removeObj) => {
    return request({
        url: '/api/wms/suppliers/remove',
        method: 'post',
        data:removeObj
    })
}

export const exportFile = (params) => {
    return request({
        url: '/api/wms/suppliers/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

export const add = (params) => {
    return request({
        url: '/api/wms/suppliers/newSupplier',
        method: 'post',
        data: params,
    })
}

