import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/customer/page',
        method: 'post',
        data: page,params

    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/customer/delete',
        method: 'post',
        data: ids,
        params: {

        }
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/customer/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

