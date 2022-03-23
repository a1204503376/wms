import request from '@/router/axios';

export const download = (fileName) => {
  return request({
    url: '/api/attachment/download',
    method: 'get',
    params: {
      fileName: fileName
    },
    responseType: 'blob'
  })
}
