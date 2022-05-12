import request from '@/router/axios'

export const getZoneSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/warehouse/zone/select',
        method: 'post',
        data: data
    })
}


