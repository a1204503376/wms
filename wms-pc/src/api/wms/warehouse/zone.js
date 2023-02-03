import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/warehouse/zone/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const getZone = () => {
    return request({
        url: '/api/wms/warehouse/warehouse/list',
        method: 'get'
    })
}
export const getList = (params) => {
    return request({
        url: '/api/wms/warehouse/zone/list',
        method: 'get',
        params: {
            ...params
        }
    })
}

export const getDetail = (zoneId) => {
    return request({
        url: '/api/wms/warehouse/zone/detail',
        method: 'get',
        params: {
            zoneId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/warehouse/zone/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const add = (row) => {
    return request({
        url: '/api/wms/warehouse/zone/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/warehouse/zone/submit',
        method: 'post',
        data: row
    })
}
export const getSelectList = (whId) => {
    return request({
        url: '/api/wms/warehouse/zone/list',
        method: 'get',
        params: {
            whId: whId
        }
    })
}
export const exportFile = (data) => {
    return request({
        url: '/api/wms/warehouse/zone/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}

export const getZoneSelectResponse = (data) => {
    return request({
        url: '/api/wms/warehouse/zone/select',
        method: 'post',
        data: {
            data
        }
    })
}

