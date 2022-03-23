import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/core/stocksnapshoot/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}

export const getDetail = (id) => {
  return request({
    url: '/api/wms/core/stocksnapshoot/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/core/stocksnapshoot/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/wms/core/stocksnapshoot/submit',
    method: 'post',
    data: row
  })
}

export const initialize = () => {
    return request({
        url: '/api/wms/core/stocksnapshoot/initializeStockSnapshoot',
        method: 'get',
    })
}

export const stockSnapshoot = () => {
    return request({
        url: '/api/wms/core/stocksnapshoot/stockSnapshoot',
        method: 'get',
    })
}
