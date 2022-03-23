import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/core/lot/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
