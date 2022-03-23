<template>
    <basic-container>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            v-model="form"
            :table-loading="loading"
            @on-load="onLoad"
            @row-save="rowSave"
            @load-List="loadList"
            @selection-change="selectionChange"
            :before-open="beforeOpen"
        >
        </nodes-crud>
    </basic-container>
</template>
<script>
import {Treepaging, getDetail, add} from "@/api/authority/datascope";
import {getPage, getList} from "@/api/core/menu";
import {group as group_1} from "./datascope/group_1.js";

export default {
    name: "datascope",
    data() {
        return {
            maps: new Map(),
            loading: false,
            data: [],
            selectionList: [],
            form: {},
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: true,
                editBtn: true,
                delBtn: false,
                menu: true,
                saveBtn: true,
                rowKey: "id",
                page: false,
                custom: false,
                dialogWidth: '80%',
                lazy: true,
                column: [
                    {prop: "name", label: "菜单名称", search: true, width: 200},
                    {prop: "path", label: "路由地址", width: 200},
                    {prop: "parentName", label: "上级菜单", width: 200},
                ],
                group: [],
            },
        };
    },
    components: {},
    computed: {
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.id);
            });
            return ids.join(",");
        }
    },
    mounted() {
        this.option.group.push(group_1);
    },
    methods: {
        loadList(tree, treeNode, resolve) {
            const pid = tree.id;
            this.maps.set(pid, {tree, treeNode, resolve})
            getList({parentId: pid, category: 1}).then(res => {
                resolve(res.data.data);
                this.$refs.table.doLayout();
            });
        },
        //列表
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        rowSave(row, loading, done, type) {
            add(row).then(res => {
                    loading();
                    this.$refs.table.onLoad();
                    this.$message.success("操作成功！");
                },
                error => {
                    done();
                }
            );
        },
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        beforeOpen(done, type, finish) {
            if (['edit', 'view'].includes(type)) {
                let menuId = this.form.id;
                Treepaging({menuId: menuId}).then(res => {
                    this.option.group[0].column[0].name= this.form.name;
                    this.form = {};
                    this.$set(this.form, 'menuId', menuId);
                    res.data.data.forEach(item => {
                        this.$set(item, 'menuId', menuId);
                    });
                    this.$set(this.form, 'dataScopeList', res.data.data);
                }).catch(() => {
                }).finally(() => {
                    done();
                })
            }
        },
    }
};
</script>
<style lang="scss" scoped>

</style>
