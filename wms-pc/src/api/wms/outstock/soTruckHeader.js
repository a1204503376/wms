import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/outstock/soTruckHeader/page',
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
        url: '/api/wms/outstock/soTruckHeader/detail',
        method: 'get',
        params: {
            truckId: id
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/outstock/soTruckHeader/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const confirmSo = (truckId) => {
    return request({
        url: '/api/wms/outstock/soTruckHeader/SaveconfirmSo',
        method: 'post',
        params: {
            truckId,
        }
    })
}
export const add = (row) => {
    return request({
        url: '/api/wms/outstock/soTruckHeader/submit',
        method: 'post',
        data: row
    })
}

export const update = (row) => {
    return request({
        url: '/api/wms/outstock/soTruckHeader/submit',
        method: 'post',
        data: row
    })
}

export const soTruckDetail = (id) => {
    return request({
        url: '/api/wms/outstock/soTruckDetail/list',
        method: 'get',
        params: {
            truckId: id
        }
    })
}

export const detailRemove = (ids) => {
    return request({
        url: '/api/wms/outstock/soTruckDetail/remove',
        method: 'post',
        params: {
            ids,
        }
    })
}
