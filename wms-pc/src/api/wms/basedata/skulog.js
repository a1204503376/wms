import request from '@/router/axios'

export const getList = () => {
  return request({
    url: '/api/wms/basedata/skulog/list',
    method: 'get'
  })
}

