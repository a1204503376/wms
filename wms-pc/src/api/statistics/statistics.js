import request from '@/router/axios';

export const getSoBillCount = () => {
    return request({
        url: '/api/statistics/soBillCount',
        method: 'get'
    });
}

export const getAsnBillCount = () => {
    return request({
        url: '/api/statistics/asnBillCount',
        method: 'get'
    })
}

export const getStockoutSkuCount = () => {
    return request({
        url: '/api/statistics/stockoutSkuCount',
        method: 'get'
    })
}

export const getIdleSkuInfo = (beginTime, endTime) => {
    return request({
        url: '/api/statistics/idleSkuInfo',
        method: 'get',
        params: {
            ...{
                beginTime: beginTime,
                endTime: endTime
            }
        }
    })
}

export const getBillTrait = () => {
    return request({
        url: '/api/statistics/billTrait',
        method: 'get'
    })
}

export const getSkuStockInfo = () => {
    return request({
        url: '/api/statistics/stockSkuCount',
        method: 'get'
    })
}

export const getLocOccupyInfo = () => {
    return request({
        url: '/api/statistics/locOccupyInfo',
        method: 'get'
    })
}

export const getOutstockRate = () => {
    return request({
        url: '/api/statistics/outstockRate',
        method: 'get'
    })
}

export const getLocRate = () => {
    return request({
        url: '/api/statistics/locRate',
        method: 'get'
    })
}

export const getUnExecTaskInfo = () => {
    return request({
        url: '/api/statistics/unExecTaskInfo',
        method: 'get'
    })
}
export const idleSkuInfo = ()=>{
    return request({
        url :'/api/statistics/idleSkuInfo',
        method:'get'
    })
}

export const getUnsafeStockSkuList = ()=>{
    return request({
        url :'/api/statistics/getUnsafeStockSkuList',
        method:'get'
    })
}

export const getAdventSkuList = ()=>{
    return request({
        url :'/api/statistics/getAdventSkuList',
        method:'get'
    })
}

export const getSkuOutstockSummary = ()=>{
    return request({
        url :'/api/statistics/getSkuOutstockSummary',
        method:'get'
    })
}

export const getAll = () => {
    return request({
        url: '/api/statistics/all',
        method: 'get'
    })
}
