<template>
    <basic-container>
        <nodes-crud
            ref="table"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :permission="permissionList"
            @on-load="onLoad"
            @selection-change="selectionChange"
        >
            <template slot="menuLeft">

            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {
    getPageByOwnerBill,
} from "@/api/wms/billing/agreement.js";
import {mapGetters} from "vuex";

export default {
    name: "ownerBill",
    components: {},
    data() {
        return {
            loading: false,
            form: {},
            data: [],
            selectionList: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: false,
                custom: false,
                column: [
                    {
                        label: "货主",
                        prop: "woId",
                        search: true,
                        width: 160,
                        sortable: true,
                        type: "select",
                        dicUrl: "/api/wms/basedata/owner/list",
                        props: {
                            label: "ownerName",
                            value: "woId"
                        }
                    },
                    {
                        label: '生效日期',
                        prop: 'effectiveDate',
                        type: 'date-picker'
                    },
                    {
                        label: '解约日期',
                        prop: 'terminationDate',
                        type: 'date-picker'
                    },
                    {
                        label: '结算日期',
                        prop: 'closingDate',
                        type: 'date-picker'
                    },
                    {
                        label: '合计(万元)',
                        prop: 'totalAmount'
                    }
                ],
                group: [],
            },
            searchData: {},
        };
    },
    mounted() {
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {}
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.woId);
            });
            return ids.join(",");
        },
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getPageByOwnerBill(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
                const data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        selectionChange(val) {
            this.selectionList = val;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
    },
};
</script>

<style scoped>
</style>
