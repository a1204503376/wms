import request from '@/router/axios'

export const getList = (params) => {
    return request({
        url: '/api/wms/core/userOnline/list',
        method: 'get',
        params: {
            ...params
        }
    })
}
