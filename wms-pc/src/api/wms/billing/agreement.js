import request from '@/router/axios';

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/billing/agreement/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

export const getDetail = (id) => {
    return request({
        url: '/api/wms/billing/agreement/detail',
        method: 'get',
        params: {
            id
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/billing/agreement/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const submit = (row) => {
    return request({
        url: '/api/wms/billing/agreement/submit',
        method: 'post',
        data: row
    })
}

export const getPageByOwnerBill = (current, size, params) => {
    return request({
        url: '/api/wms/billing/agreement/owner/bill/page',
        method: 'get',
        params: {
            ...params,
            current,
            size,
        }
    })
}

