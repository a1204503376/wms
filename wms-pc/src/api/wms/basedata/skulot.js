import request from '@/router/axios';

export const getList = (params) => {
    return request({
        url: '/api/wms/basedata/skulot/list',
        method: 'get',
        params: {
            ...params
        }
    })
}
export const getLableDetail = (wslId) => {
    return request({
        url: '/api/wms/basedata/skulot/detail',
        method: 'get',
        params: {
            wslId,
        }
    })
}
export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/basedata/skulot/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getDetail = (wslId) => {
    return request({
        url: '/api/wms/basedata/skulot/detail',
        method: 'get',
        params: {
            wslId,
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/basedata/skulot/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/basedata/skulot/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/basedata/skulot/submit',
        method: 'post',
        data: row
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/basedata/skulot/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}

export const getConfig = (skuId) => {
    return request({
        url: '/api/wms/basedata/skulot/config',
        method: 'get',
        params: {
            skuId: skuId
        }
    })
}


