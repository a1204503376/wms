import request from '@/router/axios'
import {blob} from "pikaz-excel-js/lib/PikazJsExcel.common";

export const getPage = (page, data) => {
    return request({
        url: '/api/wms/outstock/soBill/page',
        method: 'post',
        params: page,
        data: data
    })
}

export const add = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/add',
        method: 'post',
        data: data
    })
}

export const remove = (soBillIdList) => {
    return request({
        url: '/api/wms/outstock/soBill/remove',
        method: 'post',
        data: {
            idList: soBillIdList
        }
    })
}

export const detailByEdit = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detailByEdit',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const edit = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/edit',
        method: 'post',
        data: data
    })
}

export const getHeaderForDetail = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_header',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const getDetailForDetail = (page, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_detail',
        method: 'post',
        params: page,
        data: {
            soBillId: soBillId
        }
    })
}

export const getLogSoPickForDetail = (page, soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/detail_logSoPick',
        method: 'post',
        params: page,
        data: {
            soBillId: soBillId
        }
    })
}

export const exportData = (data) => {
    return request({
        url: '/api/wms/outstock/soBill/export',
        method: 'post',
        data: data,
        responseType: 'blob'
    })
}

export const closeSoBill = (soBillId) => {
    return request({
        url: '/api/wms/outstock/soBill/close',
        method: 'post',
        data: {
            soBillId: soBillId
        }
    })
}

export const getSoBillDataByReceiveIds = (ids) => {
    return request({
        url: '/api/wms/outstock/soBill/findLogSoPickByReceiveIds',
        method: 'post',
        data: {
            idList: ids
        }
    })
}

