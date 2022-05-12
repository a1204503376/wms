import request from '@/router/axios'

export const getLocationSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/warehouse/location/getLocationSelectResponseTop10List',
        method: 'post',
        data: data
    })
}


