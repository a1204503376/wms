<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            v-model="form"
            :table-loading="loading"
            :permission="permissionList"
            @on-load="onLoad"
            @on-del="onDel"
            @row-save="rowSave"
            @load-List="loadList"
            :before-open="beforeOpen"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
        >
        </nodes-crud>
    </basic-container>
</template>

<script>
import {
    getList,
    getPage,
    add,
    remove,
    getDetail,
} from "@/api/core/region.js";
import { group as group_1 } from "./region/group_1";
import {mapGetters} from "vuex";

export default {
    name: "region",
    data() {
        return {
            maps: new Map(),
            loading: false,
            form: {},
            data: [],
            selectionList: [],
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                rowKey: "code",
                page: true,
                custom: false,
                lazy: true,
                menuItem: [
                    {
                        label: "添加子级",
                        command: 1,
                        divided: true
                    }
                ],
                column: [
                    {
                        label: "区域编号",
                        prop: "code",
                        search: true,
                        width: 160,
                        view: true,
                        placeholder: "支持模糊查询",
                        sortable: true,
                    },
                    {
                        label: "父区域编号",
                        prop: "parentCode",
                        search: true,
                        width: 160,
                        placeholder: "支持模糊查询",
                        sortable: true,
                    },
                    {
                        label: "祖区域编号",
                        prop: "ancestors",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "区域名称",
                        prop: "name",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "省级区域编号",
                        prop: "provinceCode",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "省级名称",
                        prop: "provinceName",
                        search: true,
                    },
                    {
                        label: "市级区域编号",
                        prop: "cityCode",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "市级名称",
                        prop: "cityName",
                        search: true,
                    },
                    {
                        label: "区级区域编号",
                        prop: "districtCode",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "区级名称",
                        prop: "districtName",
                        search: true,
                    },
                    {
                        label: "镇级区域编号",
                        prop: "townCode",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "镇级名称",
                        prop: "townName",
                        search: true,
                    },
                    {
                        label: "村级区域编号",
                        prop: "villageCode",
                        search: true,
                        sortable: true,
                    },
                    {
                        label: "村级名称",
                        prop: "villageName",
                        search: true,
                    },
                    {
                        label: "备注",
                        prop: "remark",
                        search: true,
                    },
                ],
            },
            searchData: {},
        };
    },
    mounted() {
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.region_add, false),
                view: this.vaildData(this.permission.region_view, false),
                edit: this.vaildData(this.permission.region_edit, false),
                delete: this.vaildData(this.permission.region_delete, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.code);
            });
            return ids.join(",");
        },
    },
    methods: {
        loadList(tree, treeNode, resolve) {
            const pcode = tree.code;
            this.maps.set(pcode, {tree, treeNode, resolve})
            getList({parentCode_equal: pcode}).then(res => {
                resolve(res.data.data);
                this.$refs.table.doLayout();
            });
        },
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, {parentCode_equal:0})
            ).then(res => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        rowSave(row, loading, done, type) {
            add(row).then(
                () => {
                    const {parentCode} = row;
                    if (this.maps.has(parentCode)) {
                        const {tree, treeNode, resolve} = this.maps.get(parentCode);
                        this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, parentCode, []);
                        this.loadList(tree, treeNode, resolve)
                    }


                    loading();
                    this.$refs.table.onLoad();
                    this.$message.success("操作成功！");
                },
                error => {
                    done();
                }
            );
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.code)
                    .then(res => {
                        this.form = res.data.data;
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(row.code).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                    const {parentCode} = row
                    if (parentCode != '0' && this.maps.has(parentCode)) {
                        const {tree, treeNode, resolve} = this.maps.get(parentCode);
                        this.$set(this.$refs.table.$refs.table.store.states.lazyTreeNodeMap, parentCode, []);
                        this.loadList(tree, treeNode, resolve)
                    }
                });
            });
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.addChild(row);
                    break;
            }
        },
        onMultiDel() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning"
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!"
                    });
                    this.$refs.table.toggleSelection();

                });
            });
        },
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        //成功回调
        childSuccess() {
            this.$refs.table.onLoad();
            this.dialogVisible = false;
        },
        //取消回调
        childCancel() {
            this.dialogVisible = false;
        },
        changeIsShowDialog(val) {
            this.dialogVisible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        addChild(row) {
            this.$refs.table.rowAdd({
                parentCode: row.code,
                hasChildren: row.hasChildren
            });
        },
    }
};
</script>

<style scoped>
</style>
