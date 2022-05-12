
import request from '@/router/axios'

export const getOwnerSelectResponseTop10List = () => {
    return request({
        url: '/api/wms/basedata/owner/ownerSelect',
        method: 'get'
    })
}
