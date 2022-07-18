import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/outstock/soBill/pageNotSoPick',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportExcel = (params) => {
    return request({
        url: '/api/wms/outstock/soBill/exportNotSoPick',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}
