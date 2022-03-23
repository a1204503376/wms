import request from '@/router/axios';

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/outstock/sales-header/page',
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
        url: '/api/wms/outstock/sales-header/detail',
        method: 'get',
        params: {
            soBillId: id
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/outstock/sales-header/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}

export const submit = (row) => {
    return request({
        url: '/api/wms/outstock/sales-header/submit',
        method: 'post',
        data: row
    })
}

export const createSo = (row) => {
    return request({
        url: '/api/wms/outstock/sales-header/create-so',
        method: 'post',
        data: row
    })
}

export const getSalesBillNo = (row) => {
    return request({
        url: '/api/wms/outstock/sales-header/getSalesBillNo',
        method: 'get',
    })
}

