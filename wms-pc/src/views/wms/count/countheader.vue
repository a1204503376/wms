<template>
    <basic-container>
        <nodes-crud
            ref="table"
            v-model="form"
            :option="option"
            :data="data"
            :table-loading="loading"
            :before-open="beforeOpen"
            :permission="permissionList"
            @on-load="onLoad"
            @row-save="rowSave"
            @on-del="onDel"
            @on-multi-del="onMultiDel"
            @selection-change="selectionChange"
            @menu-command="menuCommand"
        >
            <template slot="menuLeft">
        <span class="dropdownPanel">
          <el-dropdown trigger="click" @command="handleCommand">
            <el-button type="primary" size="mini">
              <i class="el-icon-edit-outline"></i>
              操作
              <i class="el-icon-arrow-down el-icon--right"></i>
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item v-if="permission.count_header_completeTask" command="1">完成盘点</el-dropdown-item>
              <el-dropdown-item v-if="permission.count_header_repeat" command="2">复盘</el-dropdown-item>
              <el-dropdown-item v-if="permission.count_header_abolish" command="3">盘点作废</el-dropdown-item>
              <el-dropdown-item v-if="permission.count_header_differenceReport" command="4">生成差异报告</el-dropdown-item>
              <el-dropdown-item v-if="permission.count_header_differenceOrder" command="6">创建差异单据</el-dropdown-item>
              <el-dropdown-item v-if="permission.count_header_randomTask" command="5" divided>随机盘点</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </span>
            </template>
        </nodes-crud>
        <assign-task
            :isShowDialogs="assignTaskVisible"
            :dataSourcetask="dataSourcetask"
            @randomchange="taskchange"
            @randomSuccess="taskSuccess"
            @randomCancel="taskCancel"
        ></assign-task>
        <randomcount
            :isShowDialogs="randomFormVisible"
            :dataSourcetask="dataSourcetask"
            @randomchange="randomchange"
            @randomSuccess="randomSuccess"
            @randomCancel="randomCancel"
        ></randomcount>
    </basic-container>
</template>
<script>
import {mapState} from "vuex";
import {
    getPage,
    add,
    remove,
    getDetail,
    getCNO,
    completeTask,
    differenceReport,
    repeat,
    completeDifferenceReport,
    abolish,
    createDifferenceOrder,
    exportFile,
} from "@/api/wms/count/countheader";
import assignTask from "./components/assignTask";
import randomcount from "./components/randomcount";
import {group as group_1} from "./countHeader/group_1";
import {group as group_2} from "./countHeader/group_2";
import {group as group_3} from "./countHeader/group_3";
import fileDownload from "js-file-download";
import {mapGetters} from "vuex";

export default {
    name: "countheader",
    components: {
        assignTask,
        randomcount,
    },
    data() {
        return {
            operation: false,
            operationButton: [
                "完成盘点",
                "复盘",
                "盘点作废",
                "生成差异报告",
                "创建差异单据",
                "随机盘点",
            ],
            loading: false,
            selectionList: [], //选中的数据
            data: [],
            option: {
                newBtn: true,
                multiDelBtn: false,
                viewBtn: true,
                editBtn: false,
                delBtn: false,
                menu: true,
                custom: false,
                ishide: true,
                groupButton: [
                    {
                        label: "导出差异",
                        hide: true,
                        type: "primary",
                        click: function (el) {
                            this.loading = true;
                            exportFile(el.form.countBillId)
                                .then((res) => {
                                    el.$message.success("操作成功，正在下载中...");
                                    console.log(res);
                                    fileDownload(res.data, "差异报告.xlsx");
                                })
                                .catch(() => {
                                    el.$message.error("系统模板目录配置有误或文件不存在");
                                })
                                .finally(() => {
                                    this.loading = false;
                                });
                        },
                    },
                ],
                menuItem: [
                    {
                        label: "分派任务",
                        command: 1,
                    },
                ],
                column: [
                    {
                        label: "盘点单编码",
                        prop: "countBillNo",
                        width: 180,
                        search: true,
                        view: true,
                        // sortable: true,
                    },
                    {
                        label: "所属库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId",
                        },
                    },
                    {
                        label: "盘点状态",
                        prop: "countBillState",
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=stockcount_state",
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "盘点类型",
                        prop: "countTag",
                        search: true,
                        type: "select",
                        dicUrl: "/api/blade-system/dict/dictionary?code=stockcount_type",
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                    },
                    {
                        label: "同步状态",
                        prop: "syncState",
                        width: 120,
                        type: "select",
                        dicUrl:
                            "/api/blade-system/dict/dictionary?code=" + this.$dict.syncState,
                        props: {
                            label: "dictValue",
                            value: "dictKey",
                        },
                        // sortable: true,
                    },
                    {label: "单据创建人", prop: "creator", width: 120},
                    {
                        label: "单据创建时间",
                        prop: "procTime",
                        width: 170,
                        sortable: true,
                        type: 'date-picker'
                    },
                    {label: "备注", prop: "countRemark", width: 200},
                ],
                group: [],
            },
            assignTaskVisible: false,
            dataSourcetask: {},
            randomFormVisible: false,
            countBillNo: "",
            form: {},
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                add: this.vaildData(this.permission.count_header_add, false),
                view: this.vaildData(this.permission.count_header_view, false),
                完成盘点: this.vaildData(this.permission.count_header_completeTask, false),
                复盘: this.vaildData(this.permission.count_header_repeat, false),
                盘点作废: this.vaildData(this.permission.count_header_abolish, false),
                生成差异报告: this.vaildData(this.permission.count_header_differenceReport, false),
                创建差异单据: this.vaildData(this.permission.count_header_differenceOrder, false),
                随机盘点: this.vaildData(this.permission.count_header_randomTask, false),
                生成任务: this.vaildData(this.permission.count_header_assignTask, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach((ele) => {
                ids.push(ele.countBillId);
            });
            return ids.join(",");
        },
        ...mapState("leftMenu", ["downButton"]),
    },
    watch: {
        downButton(val) {
            let state = false;
            //   console.log(val)
            //   console.log(this.operationButton)
            this.downButton.forEach((v) => {
                if (this.operationButton.includes(v)) {
                    state = true;
                }
            });
            this.operation = state;
        },
    },
    mounted() {
        this.option.group.push(group_1);
        this.option.group.push(group_2);
        this.option.group.push(group_3);
    },
    methods: {
        //默认渲染数据
        onLoad(page, params = {}) {
            this.loading = true;
            getPage(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then((res) => {
                let data = res.data.data;
                page.total = data.total;
                this.data = data.records;
                this.loading = false;
                this.selectionClear();
            });
        },
        rowSave(row, loading, done, type) {
            // 避免提交后，服务器抛异常，前端表格的数据就变了
            let data = Object.assign({}, row);
            data.countDetailList = row.countDetailCheckedList;
            add(data).then(
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
        beforeOpen(done, type, finish) {
            if (["edit", "view"].includes(type)) {
                getDetail(this.form.countBillId)
                    .then((res) => {
                        for (let key in res.data.data) {
                            if (res.data.data[key] === -1) {
                                res.data.data[key] = "";
                            }
                        }
                        res.data.data.countDetailList = res.data.data.countDetailVOList;
                        this.form = res.data.data;

                        let countList = this.form.countReportVOList;
                        if (countList.length > 0) {
                            if (
                                this.option.groupButton &&
                                this.option.groupButton.length > 0
                            ) {
                                this.option.groupButton[0].hide = false;
                                // setVisible(true);
                            }
                        } else {
                            if (
                                this.option.groupButton &&
                                this.option.groupButton.length > 0
                            ) {
                                this.option.groupButton[0].hide = true;
                                //setVisible(false);
                            }
                        }
                    })
                    .finally(() => {
                        done();
                    });
            }
            //setDialogType(type)
        },
        onDel(row, index) {
            this.$confirm("确定删除当前数据？", {
                confirmButtonText: "确定",
                cancelButtonText: "取消",
                type: "warning",
            }).then(() => {
                remove(row.countBillId).then(() => {
                    this.$refs.table.onLoad();
                    this.$message({
                        type: "success",
                        message: "操作成功!",
                    });
                });
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
        //选中的数据
        selectionChange(list) {
            this.selectionList = list;
        },
        selectionClear() {
            this.selectionList = [];
            this.$refs.table.toggleSelection();
        },
        // 完成任务
        compeletTask() {
            completeTask(this.ids).then(() => {
                this.currentPage = 1;
                this.$message({
                    type: "success",
                    message: "操作成功!",
                });
                this.$refs.table.onLoad();
            });
        },
        //差异报告
        differenceReport() {
            differenceReport(this.ids).then(() => {
                this.currentPage = 1;
                this.$message({
                    type: "success",
                    message: "操作成功!",
                });
                this.$refs.table.onLoad();
            });
        },
        handleCommand(cmd) {
            let tag = parseInt(cmd);
            if (tag === 5) {
                // 随机盘点
                this.randomFormVisible = true;
                return;
            }
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一个任务才能执行此操作！");
                return;
            }
            switch (tag) {
                case 1: // 完成盘点
                    this.compeletTask();
                    break;
                case 2: // 复盘
                    this.$confirm("是否确认复盘当前勾选的盘点单？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    }).then(() => {
                        this.loading = true;
                        repeat(this.ids)
                            .then((res) => {
                                this.$message.success(
                                    "操作成功（新盘点单编码：" + res.data + ")!"
                                );
                                this.$refs.table.onLoad();
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    });
                    break;
                case 3: // 盘点作废
                    this.$confirm("是否确认作废当前勾选的盘点单？", {
                        confirmButtonText: "确定",
                        cancelButtonText: "取消",
                        type: "warning",
                    }).then(() => {
                        this.loading = true;
                        abolish(this.ids)
                            .then((res) => {
                                this.$message.success("操作成功！");
                                this.$refs.table.onLoad();
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    });
                    break;
                case 4: // 生成差异报告
                    this.differenceReport();
                    break;
                case 6: // 创建差异单据
                    this.$confirm(
                        "是否确认创建差异单据，创建差异单据后将即时修改库存，且不可逆？",
                        {
                            confirmButtonText: "确定",
                            cancelButtonText: "取消",
                            type: "warning",
                        }
                    ).then(() => {
                        this.loading = true;
                        createDifferenceOrder(this.ids)
                            .then(() => {
                                this.currentPage = 1;
                                this.$message({
                                    type: "success",
                                    message: "操作成功!",
                                });
                                this.$refs.table.onLoad();
                            })
                            .finally(() => {
                                this.loading = false;
                            });
                    });
                    break;
            }
        },
        defaultgetCNO() {
            getCNO().then((res) => {
                this.countBillNo = res.data;
            });
        },
        //成功回调 - 随机盘点
        randomSuccess() {
            this.$refs.table.onLoad();
        },
        //取消回调
        randomCancel() {
            this.randomFormVisible = false;
        },
        randomchange(val) {
            this.randomFormVisible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        //成功回调
        taskSuccess() {
            this.assignTaskVisible = false;
            this.$refs.table.onLoad();
        },
        //取消回调
        taskCancel() {
            this.assignTaskVisible = false;
        },
        taskchange(val) {
            this.assignTaskVisible = val; //监听变化时触发的函数修改父组件的是否显示状态
        },
        // 操作按钮事件
        menuCommand(cmd, row) {
            switch (cmd) {
                case 1: // 分派任务
                    this.assignTaskVisible = true;
                    this.dataSourcetask = row;
                    break;
            }
        },
    },
};
</script>
<style lang="scss">
.dropdownPanel {
    margin-right: 10px;
}
</style>
