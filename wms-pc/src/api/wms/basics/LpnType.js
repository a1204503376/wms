import request from '@/router/axios';

export const getLpnTypePage = (Query, query) => {
    return request({
        url: '/api/wms/lpnType/page',
        method: 'post',
        params: query,
        data: Query

    })
}

export const newLpnType = (Request) => {
    return request({
        url: '/api/wms/lpnType/newLpnType',
        method: 'post',
        params: {
            Request
        }
    })
}

export const deleteLpnType = (Request) => {
    return request({
        url: '/api/wms/lpnType/delete',
        method: 'post',
        data: {
            list:Request
        }
    })
}


export const excelLpnType = (params) => {
    return request({
        url: '/api/wms/lpnType/excel',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}
