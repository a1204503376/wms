import request from "@/router/axios";

export const page = (page, params) => {
    return request({
        url: '/api/wms/updateVer/page',
        method: 'post',
        params:page,
        data: params
    })
}

export const exportFile = (params) => {
    return request({
        url: '/api/wms/updateVer/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

export const detailByEdit = (suvId) => {
    return request({
        url: '/api/wms/updateVer/detailByEdit',
        method: 'get',
        params: {
            suvId: suvId
        }
    })
}

export const edit = (data) => {
    return request({
        url: '/api/wms/updateVer/edit',
        method: 'post',
        data: data,
    })
}
