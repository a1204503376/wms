import request from '@/router/axios'

export const page = (current, size, descs, ascs , params) => {
    return request({
        url: '/api/wms/suppliers/page',
        method: 'post',
        data: params,
        params: {
            current: current,
            size: size,
            descs: descs,
            ascs: ascs
        }

    })
}

export const remove = (removeObj) => {
    return request({
        url: '/api/wms/suppliers/remove',
        method: 'post',
        data:removeObj
    })
}

export const exportFile = (params) => {
    return request({
        url: '/api/wms/suppliers/export',
        method: 'post',
        data: params,
        responseType: 'blob'
    })
}

