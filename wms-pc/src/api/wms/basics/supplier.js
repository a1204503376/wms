import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/supplier/page',
        method: 'post',
        params:page,
        data: params
    })
}

export const remove = (removeObj) => {
    return request({
        url: '/api/wms/supplier/remove',
        method: 'post',
        data: removeObj
    })
}

export const exportFile = (params) => {
    return request({
        url: '/api/wms/supplier/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

export const importFile = (data) => {
    return request({
        url: '/api/wms/supplier/import-data',
        method: 'post',
        data: data
    })
}

export const add = (params) => {
    return request({
        url: '/api/wms/supplier/newSupplier',
        method: 'post',
        data: params,
    })
}

export const getSupplierSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/supplier/select',
        method: 'post',
        data: data
    })
}

