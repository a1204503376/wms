import request from '@/router/axios'

export const getPage = (page, params) => {
    return request({
        url: '/api/wms/warehouse/location/page',
        method: 'post',
        params: page,
        data: params
    })
}

export const add = (data) => {
    return request({
        url: '/api/wms/warehouse/location/add',
        method: 'post',
        data: data
    })
}

export const edit = (data) => {
    return request({
        url: '/api/wms/warehouse/location/edit',
        method: 'post',
        data: data
    })
}

export const detailByEdit = (locId) => {
    return request({
        url: '/api/wms/warehouse/location/detailByEdit',
        method: 'get',
        params:{
            locId
        }
    })
}

export const freeze = (data) => {
    return request({
        url: '/api/wms/warehouse/location/freeze',
        method: 'post',
        data: {
            locIdList: data
        }
    })
}

export const thaw = (data) => {
    return request({
        url: '/api/wms/warehouse/location/thaw',
        method: 'post',
        data: {
            locIdList: data
        }
    })
}

export const detail = (locId) => {
    return request({
        url: '/api/wms/warehouse/location/detail',
        method: 'get',
        params:{
            locId
        }
    })
}

export const remove = (removeObj) => {
    return request({
        url: '/api/wms/warehouse/location/remove',
        method: 'post',
        data: removeObj
    })
}

export const getLocationSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/warehouse/location/getLocationSelectResponseTop10List',
        method: 'post',
        data: data
    })
}

export const exportFile = (data) => {
    return request({
        url: '/api/wms/warehouse/location/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const importFile = (data) => {
    return request({
        url: '/api/wms/warehouse/location/import-data',
        method: 'post',
        data: data
    })
}

export const getListFor3d = () => {
    return request({
        url: '/api/wms/warehouse/location/get3dLocationData',
        method: 'get'
    })
}


