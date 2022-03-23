import request from '@/router/axios'

export const getList = (row) => {
    return request({
        url: '/api/wms/basedata/skuOutstock/list',
        method: 'get',
        params: row
    })
}
