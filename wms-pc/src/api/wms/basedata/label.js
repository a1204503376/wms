import request from '@/router/axios';

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/basedata/label/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const submit = (row) => {
    return request({
        url: '/api/wms/basedata/label/submit',
        method: 'post',
        data: row
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/basedata/label/remove',
        method: 'post',
        params: {
            ids
        }
    })
}

export const getDetail = (wlId) => {
    return request({
        url: '/api/wms/basedata/label/detail',
        method: 'get',
        params: {
            wlId
        }
    })
}
