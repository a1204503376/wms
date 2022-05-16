import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/receive/page',
        method: 'post',
        params: page,
        data: params
    })
}


export const remove = (nums) => {
    return request({
        url: '/api/wms/receive/delete',
        method: 'post',
        data: nums
    })
}

export const close = (nums) => {
    return request({
        url: '/api/wms/receive/editBillState',
        method: 'post',
        data: nums
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/receive/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const getReceiveStateList = () => {
    return request({
        url: '/api/wms/receive/getReceiveStateList',
        method: 'get'
    })
}

export const addReceive = (data) => {
    return request({
        url: '/api/wms/receive/new',
        method: 'post',
        data: data,
    })
}
