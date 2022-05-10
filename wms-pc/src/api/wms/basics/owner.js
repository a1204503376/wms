
import request from '@/router/axios'

export const getOwnerSelectResponseList = () => {
    return request({
        url: '/api/wms/owner/ownerSelect',
        method: 'get'
    })
}
