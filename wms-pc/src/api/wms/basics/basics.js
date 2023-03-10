import request from '@/router/axios';

export const getCarriersPage = (carrierPageQuery, query) => {
    return request({
        url: '/api/wms/carriers/page',
        method: 'post',
        params: query,
        data: carrierPageQuery

    })
}

export const newCarrier = (newCarrierRequest) => {
    return request({
        url: '/api/wms/carriers/newCarrier',
        method: 'post',
        params: {
            newCarrierRequest
        }
    })
}

export const deleteCarrier = (deleteRequest) => {
    return request({
        url: '/api/wms/carriers/delete',
        method: 'post',
        data: {
            list:deleteRequest
        }
    })
}

