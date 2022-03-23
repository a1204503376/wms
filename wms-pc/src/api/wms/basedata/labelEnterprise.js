import request from '@/router/axios';

export const getList = (peId) => {
    return request({
        url: '/api/wms/basedata/labelenterprise/list',
        method: 'get',
        params: {
            peId
        }
    })
}

export const remove = (ids) => {
    return request({
        url: '/api/wms/basedata/labelenterprise/remove',
        method: 'post',
        params: {
            ids
        }
    })
}
