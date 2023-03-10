import Layout from '@/page/index/'

export default [
    {
        path: '/demo',
        component: Layout,
        children: [{
            path: 'edit/:id',
            name: 'demoEdit',
            props: true,
            component: () => import('@/views/demo/edit')
        }]
    },
    {
        path: '/selectDetails',
        component: Layout,
        children: [{
            path: 'selectDetails/:id',
            name: 'DEMO查看详情',
            props: true,
            component: () => import('@/views/demo/selectDetails')
        }]
    },
    {
        path: '/supplier',
        component: Layout,
        children: [{
            path: 'add/:id',
            name: '新增供应商',
            props: true,
            component: () => import('@/views/wms/basics/supplier/supplierAdd')
        }]
    },
    {
        path: '/LpnTypeAddOrEdit',
        component: Layout,
        children: [{
            path: 'add/:id',
            name: '新增容器',
            props: true,
            component: () => import('@/views/wms/basics/lpntype/LpnTypeAddOrEdit')
        }, {
            path: 'edit/:id',
            name: '编辑容器',
            props: true,
            component: () => import('@/views/wms/basics/lpntype/LpnTypeAddOrEdit')
        }]
    },
    {
        path: '/customer',
        component: Layout,
        children: [{
            path: 'edit/:id',
            name: '新增客户',
            props: true,
            component: () => import('@/views/wms/basics/customer/customerNew')
        }]
    },
    {
        path: '/asn',
        component: Layout,
        children: [
            {
                path: 'add/:asnBillId',
                name: '新增ASN单',
                props: true,
                component: () => import('@/views/wms/instock/asnHeader/asnAdd')
            },
            {
                path: 'edit/:asnBillId',
                name: '编辑ASN单',
                props: true,
                component: () => import('@/views/wms/instock/asnHeader/asnEdit')
            },
            {
                path: 'detail/:asnBillId',
                name: 'ASN单详情',
                props: true,
                component: () => import('@/views/wms/instock/asnHeader/asnDetail')
            }]
    },
    {
        path: '/carrier',
        component: Layout,
        children: [{
            path: 'edit/:id',
            name: '新增承运商',
            props: true,
            component: () => import('@/views/wms/basics/carrier/carrierNew')
        }]
    },
    {
        path: '/receive',
        component: Layout,
        children: [
            {
                path: 'add/:id',
                name: '新增收货单',
                props: true,
                component: () => import('@/views/wms/instock/receive/receiveNew')
            },
            {
                path: 'edit/:id',
                name: '编辑收货单',
                props: true,
                component: () => import('@/views/wms/instock/receive/receiveEdit')
            },
            {
                path: 'receiveByPc/:receiveId',
                name: 'PC收货',
                props: true,
                component: () => import('@/views/wms/instock/receive/receiveByPc')
            },
            {
                path: 'detail/:receiveId',
                name: '收货单详情',
                props: true,
                component: () => import('@/views/wms/instock/receive/receiveDetail')
            }
        ]
    },
    {
        path: '/crontabTask',
        component: Layout,
        children: [{
            path: 'add',
            name: '新增定时任务',
            props: true,
            component: () => import('@/views/crontab/task/crontabTaskNew.vue')
        },
            {
                path: 'edit/:id',
                name: '编辑定时任务',
                props: true,
                component: () => import('@/views/crontab/task/crontabTaskEdit.vue')
            },
            {
                path: 'log/:id/:crontabTaskName',
                name: '任务日志',
                props: true,
                component: () => import('@/views/crontab/task/crontabTaskLog.vue')
            },
        ]
    },
    {
        path: '/location',
        component: Layout,
        children: [
            {
                path: 'add/:locId',
                name: '新增库位',
                props: true,
                component: () => import('@/views/wms/warehouse/location/LocationAdd')
            },
            {
                path: 'edit/:locId',
                name: '编辑库位',
                props: true,
                component: () => import('@/views/wms/warehouse/location/LocationEdit')
            },
            {
                path: 'detail/:locId',
                name: '库位详情',
                props: true,
                component: () => import('@/views/wms/warehouse/location/LocationDetail')
            }]
    },
    {
        path: '/WmsSkuBomAddOrEdit',
        component: Layout,
        children: [{
            path: 'add/:id',
            name: '新增物料',
            props: true,
            component: () => import('@/views/wms/basics/WmsSkuBom/WmsSkuBomAddOrEdit')
        }, {
            path: 'edit/:id',
            name: '编辑物料',
            props: true,
            component: () => import('@/views/wms/basics/WmsSkuBom/WmsSkuBomAddOrEdit')
        }]
    },
    {
        path: '/logMessage',
        component: Layout,
        children: [{
            path: 'list',
            name: '消息列表',
            props: true,
            component: () => import('@/views/wms/log/logMessage')
        }]
    },
    {
        path: '/updateVer',
        component: Layout,
        children: [
            {
                path: 'edit/:suvId',
                name: '系统版本-编辑',
                props: true,
                component: () => import('@/views/wms/system/updateVer/updateVerEdit')
            }]
    },
    {
        path: '/soBill',
        component: Layout,
        children: [
            {
                path: 'add/:id',
                name: '新增发货单',
                props: true,
                component: () => import('@/views/wms/outstock/soHeader/soBillAdd')
            },
            {
                path: 'edit/:id',
                name: '编辑发货单',
                props: true,
                component: () => import('@/views/wms/outstock/soHeader/soBillEdit')
            },
            {
                path: 'pick/:soBillId',
                name: 'PC拣货',
                props: true,
                component: () => import('@/views/wms/outstock/soHeader/pickByPc')
            },
            {
                path: 'detail/:soBillId',
                name: '发货单详情',
                props: true,
                component: () => import('@/views/wms/outstock/soHeader/soBillDetail')
            },
            {
                path: 'distribute/:soBillId',
                name: '分配',
                props: true,
                component: () => import('@/views/wms/outstock/soHeader/distribution')
            }]
    },
    {
        path: '/logSoPick',
        component: Layout,
        children: [
            {
                path: 'createReceiveBill/:logSoPicks',
                name: '创建收货单',
                props: true,
                component: () => import('@/views/wms/instock/receive/receiveNewForLogSoPick')
            }
        ]
    },
    {
        path: '/receiveLog',
        component: Layout,
        children: [
            {
                path: 'createSoBill/:receiveLogs',
                name: '创建发货单',
                props: true,
                component: () => import('@/views/wms/outstock/soHeader/soBillAddForReceiveLog')
            }
        ]
    },
    {
        path: '/unReturned',
        component: Layout,
        children: [
            {
                path: 'createSoBill/:unReturnedData',
                name: '创建归还单',
                props: true,
                component: () => import('@/views/wms/instock/receive/receiveNewForUnReturned')
            }
        ]
    },
    {
        path: '/stock',
        component: Layout,
        children: [
            {
                path: 'stockByBox',
                name: '按箱显示',
                props: true,
                component: () => import('@/views/wms/stock/stock/stockByBox')
            },
            {
                path: 'stockByLpn',
                name: '按LPN显示',
                props: true,
                component: () => import('@/views/wms/stock/stock/stockByLpn')
            },
            {
                path: 'stockBySerial',
                name: '按序列号显示',
                props: true,
                component: () => import('@/views/wms/stock/stock/stockBySerial')
            },
            {
                path: 'serial/:stockId',
                name: '序列号',
                props: true,
                component: () => import('@/views/wms/stock/stock/serial')
            }
        ]
    },
]
