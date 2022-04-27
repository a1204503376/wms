import request from '@/router/axios'

export const page = (current, size, params) => {
    return request({
        url: '/api/wms/suppliers/page',
        method: 'post',
        data: params,
        params: {
            current: current,
            size: size
        }

    })
}

