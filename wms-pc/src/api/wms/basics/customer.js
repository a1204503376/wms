import request from '@/router/axios'

export const page = (params,page,) => {
    return request({
        url: '/api/wms/customer/page',
        method: 'post',
        data: params,
        params:page,

    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/customer/delete',
        method: 'post',
        data: ids,
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

export const addCustomer = (data) => {
    return request({
        url: '/api/wms/customer/newCustomer',
        method: 'post',
        data: data,
    })
}

export const getCustomerSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/customer/getCustomerSelectResponseTop10List',
        method: 'post',
        data: data
    })
}


