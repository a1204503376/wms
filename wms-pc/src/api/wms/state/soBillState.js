import request from '@/router/axios'

export const getSoBillState = () => {
    return request({
        url: '/api/wms/state/getSoBillState',
        method: 'get'
    })
}
