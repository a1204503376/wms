
import request from '@/router/axios'

export const getOwnerSelectResponseList = () => {
    return request({
        url: '/api/wms/basedata/owner/ownerSelect',
        method: 'get'
    })
}
