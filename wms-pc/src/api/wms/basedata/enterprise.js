import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/basedata/enterprise/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
export const getList = (params) => {
    return request({
        url: '/api/wms/basedata/enterprise/list',
        method: 'get',
        params: {
            ...params,
        }
    })
}
export const getDetail = (id) => {
    return request({
        url: '/api/wms/basedata/enterprise/detail',
        method: 'get',
        params: {
            id
        }
    })
}
export const add = (row) => {
    return request({
        url: '/api/wms/basedata/enterprise/submit',
        method: 'post',
        data: row
    })
}
export const remove = (ids) => {
    return request({
        url: '/api/wms/basedata/enterprise/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const update = (row) => {
    return request({
        url: '/api/wms/basedata/enterprise/submit',
        method: 'post',
        data: row
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/basedata/enterprise/export',
        method: 'get',
        params: {
            ...data
        },
        responseType: 'blob'
    })
}
