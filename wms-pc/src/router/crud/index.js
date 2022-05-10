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
        path: '/supplier',
        component: Layout,
        children: [{
            path: 'add/:id',
            name: 'supplierEdit',
            props: true,
            component: () => import('@/views/wms/basics/supplier/supplierAdd')
        }]
    },
    {
        path: '/customer',
        component: Layout,
        children: [{
            path: 'edit/:id',
            name: 'customerEdit',
            props: true,
            component: () => import('@/views/wms/basics/customer/customerNew')
        }]
    }
]
