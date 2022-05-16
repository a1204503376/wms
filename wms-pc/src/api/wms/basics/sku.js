import request from '@/router/axios';

export const getSkuSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/basedata/sku/getSkuSelectResponseTop10List',
        method: 'post',
        data: data
    })
}