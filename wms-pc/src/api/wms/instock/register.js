import request from '@/router/axios'


export const add = (row) => {
    return request({
        url: '/api/wms/instock/register/save',
        method: 'post',
        data: row
    })
}

