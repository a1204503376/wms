
import request from '@/router/axios'

export const getBillTypeSelectResponseList = (data) => {
    return request({
        url: '/api/wms/basedata/billtype/select',
        method: 'post',
        data: data
    })
}
