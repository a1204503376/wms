
import request from '@/router/axios'

export const getBillTypeSelectResponseList = (data) => {
    return request({
        url: '/api/wms/basedata/billtype/getBillTypeSelectResponseList',
        method: 'post',
        data: data
    })
}
