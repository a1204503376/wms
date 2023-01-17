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
