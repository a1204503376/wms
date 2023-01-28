import request from '@/router/axios';

export const findAllQueryPlan = (params) => {
    return request({
        url: '/api/wms/queryPlan/findAllNodesQueryPlan',
        method: 'post',
        data: params
    })
}

export const insertQueryPlan = (data) => {
    return request({
        url: '/api/wms/queryPlan/insert',
        method: 'post',
        data: data
    })
}


export const deleteQueryPlan = (data) => {
    return request({
        url: '/api/wms/queryPlan/delete',
        method: 'post',
        data: data
    })
}

export const updateDefaultQueryPlan = (data) => {
    return request({
        url: '/api/wms/queryPlan/update',
        method: 'post',
        data: data
    })
}

export const cancelDefaultQueryPlan = (data) => {
    return request({
        url: '/api/wms/queryPlan/cancel',
        method: 'post',
        data: data
    })
}


