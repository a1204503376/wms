import request from '@/router/axios'

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/log/instockLog/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
