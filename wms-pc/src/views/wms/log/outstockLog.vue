<template>
    <basic-container>
        <nodes-crud
                ref="table"
                :option="option"
                :data="data"
                :table-loading="loading"
                @on-load="onLoad"
                @selection-change="selectionChange"
        >

        </nodes-crud>
    </basic-container>
</template>

<script>
    import {getPage} from "@/api/wms/log/outstockLog";

    export default {
        name: "outstockLog",
        data() {
            return {
                loading: false,
                data: [],
                selectionList: [],
                option: {
                    newBtn: false,
                    multiDelBtn: false,
                    editBtn: false,
                    delBtn: false,
                    menu: false,
                    column: [
                        {
                            label: '波次编号',
                            prop: 'wellenNo',
                            width: 140,
                            search: true,
                            sortable: true,
                        },
                        {
                            label: '单据编号',
                            prop: 'soBillNo',
                            width: 170,
                            search: true,
                            sortable: true,
                        },
                        {
                            label: '库房',
                            prop: 'whId',
                            search: true,
                            type: 'select',
                            dicUrl: '/api/wms/warehouse/warehouse/list',
                            props: {
                                label: 'whName',
                                value: 'whId'
                            }
                        },
                        {
                            label: '策略编码',
                            prop: 'ssoCode',
                            width: 120,
                            sortable: true,
                        },
                        {
                            label: '策略名称',
                            prop: 'ssoName',
                            width: 120,
                            sortable: true,
                        },
                        {
                            label: '处理顺序',
                            prop: 'ssodProcOrder',
                            sortable: true,
                            width: 120,
                        },
                        {
                            label: '分配计算',
                            prop: 'outstockFunction',
                            width: 120,
                            sortable: true,
                        },
                        {
                            label: '是否成功',
                            prop: 'success',
                            sortable: true,
                            width: 120,
                            type:'select',
                            dicData:[
                                {
                                    label:'是',
                                    value:1
                                },
                                {
                                    label:'否',
                                    value:0
                                }
                            ]
                        },
                        {
                            label: '执行内容',
                            prop: 'solProcLog',
                            width: 300,
                        },
                        {
                            label: '执行时间',
                            prop: 'createTime',
                            hide: true,
                            search: true,
                            type: 'date-picker'
                        },
                        {
                            label: '用户名称',
                            prop: 'createUser',
                            width: 100,
                            type: 'select-table-user',
                            dicUrl: '/api/blade-user/list',
                            props: {
                                value: 'id',
                                label: 'realName'
                            }
                        }
                    ]
                }
            }
        },
        methods: {
            selectionChange(list) {
                this.selectionList = list;
            },
            selectionClear() {
                this.selectionList = [];
                this.$refs.table.toggleSelection();
            },
            onLoad(page, params = {}) {
                this.loading = true;
                getPage(
                    page.currentPage,
                    page.pageSize,
                    params
                ).then(res => {
                    const data = res.data.data;
                    page.total = data.total;
                    this.data = data.records;
                    this.loading = false;
                    this.selectionClear();
                });
            },
        }
    }
</script>

<style scoped>
</style>
