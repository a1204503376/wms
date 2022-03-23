<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            @on-load="onLoad"
            @selection-change="selectionChange"
            :before-open="beforeOpen"
        >

        </nodes-crud>
    </basic-container>
</template>

<script>
import {getDetail, getPage} from "@/api/wms/outstock/wellen";

export default {
    name: "wellenManagement",
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
                        label: '波次编码',
                        prop: 'wellenNo',
                        width: 140,
                        view: true,
                        search: true,
                        sortable: true,
                    },
                    {
                        label: '波次状态',
                        prop: 'wellenState',
                        width: 170,
                        search: true,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" +
                            this.$dict.wellenState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
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
                        label: '多单合并',
                        prop: 'billMultiple',
                        width: 120,
                        type: 'select',
                        dicData:[
                            {
                                label:'是',
                                value:'Y'
                            },
                            {
                                label:'否',
                                value:'N'
                            }
                        ]
                    },
                    {
                        label: '创建时间',
                        prop: 'createTime',
                        width: 120,
                        sortable: true,
                    },
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
        getDetail(done) {
            getDetail(this.form.wellenId)
                .then((res) => {
                    this.form = res.data.data;
                })
                .catch(() => {
                })
                .finally(() => {
                    done();
                });
        },
        beforeOpen(done, type) {
            console.log(type);
            if (["view"].includes(type)) {
                    this.getDetail(done, type);
                console.log(this.getDetail(done, type));
            }
        },
    }
}
</script>

<style scoped>
</style>
