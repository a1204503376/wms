import request from '@/router/axios'

export const getZoneSelectResponse = (data) => {
    return request({
        url: '/api/wms/warehouse/zone/select',
        method: 'post',
        data: {
            data
        }
    })
}


