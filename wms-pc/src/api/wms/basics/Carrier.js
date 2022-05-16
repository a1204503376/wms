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

export const excelCarrier = (params) => {
    return request({
        url: '/api/wms/carriers/excel',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

export const updateCarrier = (updateStatusRequest) => {
    return request({
        url: '/api/wms/carriers/updateStatusById',
        method: 'post',
        data: updateStatusRequest
    })
}

export const addCarrier = (data) => {
    return request({
        url: '/api/wms/carriers/newCarrier',
        method: 'post',
        data: data,
    })
}

class CarrierService {
    async getStateList(url,data) {
        const response = await request({
            url: url,
            method: 'post',
            data:data
        })
        return response.data.data;
    }

    async getDropDown(data) {
        return await this.getStateList('/api/wms/carriers/getDropDown',data);
    }


}
export const carrierService =new CarrierService();
