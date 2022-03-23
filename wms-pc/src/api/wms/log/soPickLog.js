import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/log/soPick/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getList = (id) => {
    return request({
        url: '/api/wms/log/soPick/list',
        method: 'get',
        params: {
            soBillId:id
        }
    })
}
