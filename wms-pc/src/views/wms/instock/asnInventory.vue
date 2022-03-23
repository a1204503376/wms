<template>
    <basic-container>
        <nodes-crud ref="curd"
                    v-model="form"
                    :option="option"
                    :data="data"
                    :table-loading="loading"
                    :before-open="beforeOpen"
                    @on-load="onLoad"
                    @selection-change="selectionChange">
            <template slot="menuLeft">

            </template>
            <template slot="menuRight">

            </template>
            <template slot="menu">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getPage, getDetail} from "@/api/wms/instock/asnInventory";
import {group as group_1} from "./asnInventory/group_1";
import {group as group_2} from "./asnInventory/group_2";
import {getParamValue} from "@/util/param";

export default {
    data() {
        return {
            form: {},
            query: {},
            loading: true,
            data: [],
            selectionList: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                editBtn: false,
                delBtn: false,
                menu: false,
                custom: false,
                column: [
                    {
                        label: "单据编码",
                        prop: "asnBillNo",
                        search: true,
                        width: 200,
                        sortable: true,
                        view: true
                    },
                    {
                        label: "单据类型",
                        prop: "billTypeCd",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/billtype/list?ioType=I",
                        props: {
                            label: "billTypeName",
                            value: "billTypeCd"
                        }
                    },

                    {
                        label: "单据状态",
                        prop: "asnBillState",
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.asnBillState,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "同步状态",
                        prop: "syncState",
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.syncState,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: '上位系统单号',
                        prop: 'orderNo',
                        search: true,
                        width: 200
                    },
                    {
                        label: "入库方式",
                        prop: "instoreType",
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.instoreType,
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        }
                    },
                    {
                        label: "所属货主",
                        prop: "woId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId"
                        }
                    },
                    {
                        label: "所属库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        }
                    },
                    {
                        label: "所属机构",
                        prop: "deptId",
                        search: true,
                        type: "select-tree",
                        dicUrl: "/api/blade-system/dept/tree",
                        props: {
                            label: "title",
                            value: "id",
                            children: "children"
                        },
                        clearable: true
                    },
                    {
                        label: "供应商",
                        prop: "sName",
                        search: true,
                        width: 150
                    },
                    {
                        label: "供应商地址",
                        prop: "sAddress",
                        width: 160
                    },
                    {
                        label: "供应商电话",
                        prop: "phone",
                        width: 120
                    },
                    {
                        label: "联系人",
                        prop: "contact",
                        width: 110
                    },
                    {
                        label: "预计到货时间",
                        prop: "scheduledArrivalDate",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "实际到货时间",
                        prop: "actualArrivalDate",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "单据完成时间",
                        prop: "finishDate",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "单据创建时间",
                        prop: "preCreateDate",
                        width: 160,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {
                        label: "创建类型",
                        prop: "createType",
                        width: 120,
                        sortable: true,
                        type: 'select',
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.createType,
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        }
                    },
                    {
                        label: "单据创建人",
                        prop: "createUser",
                        width: 120,
                        sortable: true,
                        type: 'select-table-user',
                        dicUrl: '/api/blade-user/list',
                        props: {
                            value: 'id',
                            label: 'realName'
                        }
                    },
                    {
                        label: "备注",
                        prop: "asnBillRemark"
                    }
                ],
                group: []
            },
        };
    },
    computed: {},
    mounted() {
        const lotCount = getParamValue(this.$param.system.lotCount);
        for (let i = 1; i <= lotCount; i++) {
            group_2.column[0].children.push({
                prop: 'skuLot' + i,
                label: '批属性' + i,
                width: 180
            })
        }
        this.option.group.push(group_1);
        this.option.group.push(group_2);
    },
    methods: {
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.id).then(res => {
                    this.form = res.data.data;
                }).finally(() => {
                    done();
                });
            }
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.curd.toggleSelection();
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(page.currentPage, page.pageSize, params).then(res => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        }
    }
};
</script>

<style>
</style>
