import request from '@/router/axios'

export const page = (page, params) => {
    return request({
        url: '/api/wms/receive/page',
        method: 'post',
        data:page,params
    })
}


export const remove = (nums) => {
    return request({
        url: '/api/wms/receive/delete',
        method: 'post',
        data: nums,
        params: {

        }
    })
}

export const close = (nums) => {
    return request({
        url: '/api/wms/receive/editBillState',
        method: 'post',
        data: nums,
        params: {

        }
    })
}
