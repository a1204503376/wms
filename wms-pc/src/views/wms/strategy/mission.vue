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
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
            @search-change="searchChange"
            :menuWidth="option.menuWidth"
            :before-open="beforeOpen"
        >

        </nodes-crud>

    </basic-container>
</template>

<script>
import {getPage, getDetail, submit, remove} from "@/api/wms/strategy/mission";
import {group as group_1} from "./mission/group_1.js";
import {mapGetters} from "vuex";

export default {
    name: "sku",
    components: {
    },
    data() {
        return {
            loading: false,
            data: [],
            selectionList: [], //选中的数据
            form: {},
            fileUpload: {
                visible: false,
            },
            dataVerify: {
                visible: false,
                dataSource: {},
            },
            option: {
                newBtn: true,
                multiDelBtn: true,
                viewBtn: true,
                editBtn: true,
                delBtn: true,
                menu: true,
                custom: false,
                menuItem: [
                ],
                column: [
                    {
                        label: "优先级",
                        prop: "missionLevel",
                        view: true,
                        sortable: true,
                        sortProp: "missionLevel",
                        search: true
                    },
                    {
                        label: "任务类型",
                        prop: "missionType",
                        search: true,
                        type: "select",
                        dicUrl: '/api/blade-system/dict/dictionary?code=task_type',
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        },
                    },

                    {
                        label: "推送方式",
                        prop: "pushWay",
                        search: true,
                        type: "select",
                        dicUrl: '/api/blade-system/dict/dictionary?code=task_push_way',
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        },
                    },

                    {
                        label: "计算方式",
                        prop: "countWay",
                        search: true,
                        type: "select",
                        dicUrl: '/api/blade-system/dict/dictionary?code=task_count_way',
                        props: {
                            label: 'dictValue',
                            value: 'dictKey'
                        },
                    },
                    {
                        label: "协同下限",
                        prop: "missionSynergyFloor",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "独占上限",
                        prop: "missionAloneUpper",
                        sortable: true,
                        search: true
                    },
                    {
                        label: "分割上限",
                        prop: "missionSegmentUpper",
                        sortable: true,
                        search: true
                    },
                    {
                        prop: "isExceedcountSegment",
                        label: "是否分割",
                        type: "select",
                        dicData: [
                            {label: "是", value: 1},
                            {label: "否", value: 0},
                        ],
                    },

                ],
                group: [],
            },
            searchData: {},
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.mission_add, false),
                view: this.vaildData(this.permission.mission_view, false),
                delete: this.vaildData(this.permission.mission_delete, false),
                edit: this.vaildData(this.permission.mission_edit, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.id);
            });
            return ids.join(",");
        },
    },
    mounted() {
        this.option.group.push(group_1);
    },
    methods: {
        //列表
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
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
        //保存
        rowSave(row, loading, done, type) {
            submit(row).then(
                () => {
                    loading();
                    this.$refs.table.onLoad();
                    this.$message.success("操作成功！");
                },
                (error) => {
                    done();
                }
            );
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            })
                .then(() => {
                    remove(row.id).then(() => {
                        this.$refs.table.onLoad();
                        this.$message({
                            type: "success",
                            message: "操作成功!",
                        });
                    });
                })
                .catch(() => {
                });
        },
        onMultiDel() {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一条数据！");
                return;
            }
            this.$confirm("确定将选择数据删除?", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                remove(this.ids).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                    this.$refs.table.toggleSelection();
                });
            });
        },
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.id)
                    .then((res) => {
                        this.form = res.data.data;
                    })
                    .catch(() => {
                    })
                    .finally(() => {
                        done();
                    });
            }
        },
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.outibrary(row, index);
                    break;
                case 2:
                    this.openInstock(row, index);
                    break;
            }
        },
        callbackSkuOutstock(res) {
            this.skuOutstock.visible = res.visible;
        },
        callbackInstock(res) {
            this.instock.visible = res.visible;
        },
        handleCommand(cmd) {
            let tag = parseInt(cmd);

            if (tag != 1 && tag != 30 && tag != 2) {
                if (!this.selectionList || this.selectionList.length == 0) {
                    this.$message.warning("至少选择一个任务才能执行此操作！");
                    return;
                }
            }
            switch (tag) {
                case 20:
                    this.loading = true;
                    exportSkuEnterpriseFile(this.ids)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res, "物品企业关联.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
                case 30:
                    this.fileUpload1.visible = true;
                    break;
                case 1:
                    this.fileUpload.visible = true;
                    break;
                case 2:
                    this.loading = true;
                    exportFile(this.searchData)
                        .then((res) => {
                            this.$message.success("操作成功，正在下载中...");
                            fileDownload(res.data, "物品.xlsx");
                        })
                        .catch(() => {
                            this.$message.error("系统模板目录配置有误或文件不存在");
                        })
                        .finally(() => {
                            this.loading = false;
                        });
                    break;
            }
        },
        callbackFileUpload(res) {
            this.fileUpload.visible = res.visible;
            if (res.result) {
                this.dataVerify.dataSource = res.data;
                this.dataVerify.visible = true;
            }
        },
        callbackDataVerify(res) {
            this.dataVerify.visible = res.visible;
            if (res.result) {
                this.$message.success("导入成功！");
                this.$refs.table.onLoad();
            }
        },
        searchChange(data) {
            this.searchData = data;
        },
    },
};
</script>
<style lang="scss">
</style>
