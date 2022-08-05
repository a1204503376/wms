import request from "@/router/axios";

export const page = (page, params) => {
    return request({
        url: '/api/wms/stock/pageUnReturned',
        method: 'post',
        params: page,
        data: params
    })
}

export const exportData = (data) => {
    return request({
        url: '/api/wms/stock/exportUnReturned',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

