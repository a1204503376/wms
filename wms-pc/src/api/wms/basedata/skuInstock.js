import request from '@/router/axios'

export const getList = (row) => {
    return request({
        url: '/api/wms/basedata/skuInstock/list',
        method: 'get',
        params: row
    })
}
