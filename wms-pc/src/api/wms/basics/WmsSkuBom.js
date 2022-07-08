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

export const save = (params) => {
    return request({
        url: '/api/wms/WmsSkuBom/save',
        method: 'post',
        data: params
    })
}

export const deleteWmsSkuBom = (params) => {
    return request({
        url: '/api/wms/WmsSkuBom/delete',
        method: 'post',
        data: {ids: params}
    })
}

export const selectSkuBomById = (params) => {
    return request({
        url: '/api/wms/WmsSkuBom/selectSkuBomById',
        method: 'post',
        data: params
    })
}

export const importData = (data) => {
    return request({
        url: '/api/wms/WmsSkuBom/import-data',
        method: 'post',
        data: data
    })
}

