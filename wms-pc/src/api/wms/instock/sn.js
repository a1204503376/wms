import request from '@/router/axios'

export const getList = (asnDetailId) => {
    return request({
        url: '/api/wms/instock/sn/list',
        method: 'get',
        params: {
            asnDetailId: asnDetailId
        }
    })
}
