<template>
    <basic-container>
        <nodes-crud :option="option"
                    :table-loading="loading"
                    :data="data"
                    ref="crud"
                    v-model="form"
                    :permission="permissionList"
                    @row-del="rowDel"
                    @selection-change="selectionChange"
                    @menu-command="menuCommand"
                    @on-load="onLoad">
            <template slot-scope="row" slot="name">
                <el-tag size="mini" style="cursor:pointer" @click="rowPreview(row.name)">{{ row.name }}</el-tag>
            </template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import {getList, remove} from "@/api/report/report";
import {mapGetters} from "vuex";

export default {
    data() {
        return {
            form: {},
            selectionList: [],
            query: {},
            loading: true,
            page: {
                pageSize: 10,
                currentPage: 1,
                total: 0
            },
            option: {
                height: 'auto',
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menuItem: [
                    {
                        label: '编辑',
                        command: 0
                    },
                    {
                        label: '删除',
                        command: 1
                    }
                ],
                column: [
                    {
                        label: "文件名",
                        prop: "name",
                        slot: true,
                        width: 200
                    },
                    {
                        label: "创建时间",
                        prop: "createTime",
                        type: 'date-picker'
                    },
                    {
                        label: "更新时间",
                        prop: "updateTime",
                        type: 'date-picker'
                    }
                ]
            },
            data: []
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                //     add: this.vaildData(this.permission.reportList_add, false),
                //     view: this.vaildData(this.permission.reportList_view, false),
                //     delete: this.vaildData(this.permission.reportList_delete, false),
            };
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.id);
            });
            return ids.join(",");
        }
    },
    mounted() {
    },
    methods: {
        rowPreview(name) {
            this.$router.push({path: `/myiframe/urlPath?name=${name}&src=${this.website.reportUrl}/preview?_u=nodes-${name}`});
        },
        rowEdit(name) {
            this.$router.push({path: `/myiframe/urlPath?name=${name}&src=${this.website.reportUrl}/designer?_u=nodes-${name}`});
        },
        rowDel(row) {
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    return remove(row.id);
                })
                .then(() => {
                    this.$refs.crud.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                });
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.crud.toggleSelection();
        },
        handleDelete() {
            if (this.selectionList.length === 0) {
                this.$message.warning("请选择至少一条数据");
                return;
            }
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            })
                .then(() => {
                    return remove(this.ids);
                })
                .then(() => {
                    this.onLoad(this.page);
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                    this.$refs.crud.toggleSelection();
                });
        },
        onLoad(page, params = {}) {
            this.loading = true;
            getList(page.currentPage, page.pageSize, Object.assign(params, this.query)).then(res => {
                const data = res.data.data;
                this.page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 0://编辑
                    this.rowEdit(row.name);
                    break;
                case 1://删除
                    this.rowDel(row);
                    break;
            }
        },
    }
};
</script>

<style>
</style>
