import request from '@/router/axios'

export const getList = (params) => {
    return request({
        url: '/api/wms/basedata/sku/list',
        method: 'get',
        params:{
            ...params
        }
    })
}

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/basedata/sku/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const addsku = (row) => {
    return request({
        url: '/api/wms/basedata/sku/submit',
        method: 'post',
        data: row
    })
}
//删除
export const remove = (ids) => {
    return request({
        url: '/api/wms/basedata/sku/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const getDetail = (id) => {
    return request({
        url: '/api/wms/basedata/sku/detail',
        method: 'get',
        params: {
            skuId: id
        }
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/basedata/sku/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}

export const canEdit = (id) => {
    return request({
        url: '/api/wms/basedata/sku/edit-valid',
        method: 'get',
        params: {
            skuId: id
        }
    })
}
