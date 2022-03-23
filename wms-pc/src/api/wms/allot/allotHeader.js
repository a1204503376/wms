import request from '@/router/axios';

export const getPage = (current, size, params) => {
  return request({
    url: '/api/wms/allot/allotheader/page',
    method: 'get',
    params: {
      ...params,
      current,
      size,
    }
  })
}
export const getDetail = (allotBillId) => {
  return request({
    url: '/api/wms/allot/allotheader/detail',
    method: 'get',
    params: {
      allotBillId
    }
  })
}

export const remove = (ids) => {
  return request({
    url: '/api/wms/allot/allotheader/remove',
    method: 'post',
    params: {
      ids,
    }
  })
}

export const add = (row) => {
  return request({
    url: '/api/wms/allot/allotheader/submit',
    method: 'post',
    data: row
  })
}

export const update = (row) => {
  return request({
    url: '/api/wms/allot/allotheader/submit',
    method: 'post',
    data: row
  })
}

export const cancel = (ids) => {
    return request({
        url: '/api/wms/allot/allotheader/cancel',
        method: 'post',
        params: {
            ids,
        }
    })
}
export const canEdit = (id) => {
    return request({
        url: '/api/wms/allot/allotheader/canEdit',
        method: 'get',
        params: {
            allotBillId: id
        }
    })
}
