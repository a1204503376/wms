import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/asn/page',
        method: 'post',
        params: page,
        data: params
    })
}

export const remove = (removeObj) => {
    return request({
        url: '/api/wms/asn/remove',
        method: 'post',
        data: removeObj
    })
}

export const exportData = (data) => {
    return request({
        url: '/api/wms/asn/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const add = (data) => {
    return request({
        url: '/api/wms/asn/add',
        method: 'post',
        data: data,
    })
}

export const detailByEdit = (asdBillIdObj) => {
    return request({
        url: '/api/wms/asn/detailByEdit',
        method: 'post',
        data: asdBillIdObj
    })
}

export const edit = (data) => {
    return request({
        url: '/api/wms/asn/edit',
        method: 'post',
        data: data,
    })
}

export const getAsnHeader = (asnBillId) => {
    return request({
        url: '/api/wms/asn/detail_header',
        method: 'post',
        data: {
            asnBillId: asnBillId
        },
    })
}

export const getAsnDetail = (page, asnBillId) => {
    return request({
        url: '/api/wms/asn/detail_detail',
        method: 'post',
        params: page,
        data: {
            asnBillId: asnBillId
        },
    })
}

export const getReceive = (page, asnBillId) => {
    return request({
        url: '/api/wms/asn/detail_receive',
        method: 'post',
        params: page,
        data: {
            asnBillId: asnBillId
        },
    })
}

export const getAsnLog = (page, asnBillId) => {
    return request({
        url: '/api/wms/asn/detail_log',
        method: 'post',
        params: page,
        data: {
            asnBillId: asnBillId
        },
    })
}
