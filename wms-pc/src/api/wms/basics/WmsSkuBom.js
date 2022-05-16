import request from '@/router/axios';

export const getWmsSkuBomPage = (PageQuery, query) => {
    return request({
        url: '/api/wms/WmsSkuBom/findAllWmsSkuBom',
        method: 'post',
        params: query,
        data: PageQuery

    })
}


export const excel = (params) => {
    return request({
        url: '/api/wms/WmsSkuBom/excel',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}
