import request from '@/router/axios'

export const getPage = (current, size, params) => {
    return request({
        url: '/api/wms/log/systemProc/page',
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
        url: '/api/wms/log/systemProc/detail',
        method: 'get',
        params: {
            systemProcId: id
        }
    })
}
