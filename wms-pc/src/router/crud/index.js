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
        },{
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
        children: [{
            path: 'add/:id',
            name: '新增ASN单',
            props: true,
            component: () => import('@/views/wms/instock/asnHeader/asnAdd')
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
    }

]
