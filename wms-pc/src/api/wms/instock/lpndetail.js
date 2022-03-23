import request from '@/router/axios';

export const getList = (current, size, params) => {
  return request({
    url: '/api/wms/instock/asnLpnDetail/list',
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
    url: '/api/wms/instock/asnLpnDetail/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/instock/asnLpnDetail/save',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/instock/asnLpnDetail/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/instock/asnLpnDetail/submit',
    method: 'post',
    data: row
  })
}

export const getListByBillIds = (current, size, billNos) => {
  return request({
    url: '/api/wms/instock/asnLpnDetail/listByBillIds',
    method: 'get',
    params: {
      billIds: billNos + '',
      current,
      size,
    }
  })
}
