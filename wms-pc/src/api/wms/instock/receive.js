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
        url: '/api/wms/receive/newReceive',
        method: 'post',
        data: data,
    })
}

export const editReceive = (data) => {
    return request({
        url: '/api/wms/receive/editReceive',
        method: 'post',
        data: data,
    })
}

export const getEditReceiveById = (data) => {
    return request({
        url: '/api/wms/receive/getEditReceiveById',
        method: 'post',
        data: data,
    })
}
export const getReceiveDetailById = (data) => {
    return request({
        url: '/api/wms/receive/getReceiveDetailById',
        method: 'post',
        data: data,
    })
}
export const getReceiveLogList = (receiveId) => {
    return request({
        url: '/api/wms/receive/getReceiveLogList',
        method: 'get',
        params:{
            receiveId,
        }
    })
}

export const getLogList = (receiveId) => {
    return request({
        url: '/api/wms/receive/getLogList',
        method: 'get',
        params:{
            receiveId,
        }
    })
}

export const getReceiveByPc = (data) => {
    return request({
        url: '/api/wms/receive/getReceiveByPc',
        method: 'post',
        data: data,
    })
}
export const getReceiveDetailByPc = (data) => {
    return request({
        url: '/api/wms/receive/getReceiveDetailByPc',
        method: 'post',
        data: data,
    })
}

export const getReceiveBillDataBylsopIds = (lsopIds) => {
    return request({
        url: '/api/wms/receive/findReceiveLogBylsopIds',
        method: 'post',
        data: {
            lsopIdList: lsopIds
        }
    })
}

export const ReceiveByPc = (data) => {
    return request({
        url: '/api/wms/receive/ReceiveByPc',
        method: 'post',
        data: data,
    })
}
