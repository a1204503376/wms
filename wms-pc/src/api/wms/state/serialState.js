import request from '@/router/axios'

export const getSerialState = () => {
    return request({
        url: '/api/wms/state/getSerialState',
        method: 'get'
    })
}
