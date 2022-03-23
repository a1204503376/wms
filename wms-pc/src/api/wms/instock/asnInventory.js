import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/instock/asn-inventory/page',
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
    url: '/api/wms/instock/asn-inventory/detail',
    method: 'get',
    params: {
      id
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/AsnInventory/asninventory/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const submit = (row) => {
  return request({
    url: '/api/AsnInventory/asninventory/submit',
    method: 'post',
    data: row
  })
}

