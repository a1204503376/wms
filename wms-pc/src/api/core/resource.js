import request from '@/router/axios'

export const getFile = (params) => {
    return request({
        url: '/api/blade-system/resource/get-file',
        method: 'get',
        params: {
            ...params,
        }
    })
}
