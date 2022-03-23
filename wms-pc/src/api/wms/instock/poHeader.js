import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/instock/po-header/page',
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
    url: '/api/wms/instock/po-header/detail',
    method: 'get',
    params: {
      poBillId:id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/instock/po-header/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/wms/instock/po-header/submit',
    method: 'post',
    data: row
  })
}

export const createAsn = (row) => {
    return request({
        url: '/api/wms/instock/po-header/create-asn',
        method: 'post',
        data: row
    })
}

export const getPoBillNo = (row) => {
    return request({
        url: '/api/wms/instock/po-header/getPoBillNo',
        method: 'get',
    })
}

