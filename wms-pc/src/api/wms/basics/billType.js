
import request from '@/router/axios'

export const getBillTypeSelectResponseTop10List = (data) => {
    return request({
        url: '/api/wms/basedata/billtype/select',
        method: 'post',
        data:data
    })
}
