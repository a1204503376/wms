import request from '@/router/axios'

export const createPickPlan = (params) => {
    return request({
        url: '/api/wms/outstock/pickplan/create',
        method: 'post',
        data: {
            ...params
        }
    })
}
export const createPickPlanByWellen = (data) => {
    return request({
        url: '/api/wms/outstock/pick/createPickPlanByWellen',
        method: 'post',
        data: data
    })
}
export const getDetailPickPlan = (ids) => {
    return request({
        url: '/api/wms/outstock/pickplan/getDetailPickPlan',
        method: 'post',
        params: {
            ids
        }
    })
}

export const savePickPlan = (data) => {
    return request({
        url: '/api/wms/outstock/pickplan/save',
        method: 'post',
        data: data
    })
}

export const rollback = (ids) => {
    return request({
        url: '/api/wms/outstock/pickplan/rollback',
        method: 'post',
        params: {
            ids
        }
    })
}
export const getList = (current, size, params) => {
    return request({
        url: '/api/wms/outstock/pickplan/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}
