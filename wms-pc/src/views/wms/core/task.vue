<template>
    <basic-container>
        <change-user
            :visible="dialogChangeUser.visible"
            :dataSource="dialogChangeUser.dataSource"
            @dialogResult="changeUserCallback"
        ></change-user>
        <user-online :visible="dialogUserOnline.visible" @dialogResult="userOnlineCallback"></user-online>
        <detail
            :visible="dialogDetail.visible"
            :task-id="dialogDetail.taskId"
            @dialogResult="detailCallback"
        ></detail>
        <nodes-crud
            ref="table"
            :option="option"
            :data="data"
            :table-loading="loading"
            :permission="permissionList"
            @on-load="onLoad"
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
              <el-dropdown-item v-if="permission.task_changeUser" command="1">人员变更</el-dropdown-item>
                  <el-dropdown-item v-if="permission.task_closeTask" command="3">关闭任务</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </span>
                <el-button v-if="permission.task_userOnline" size="mini" @click="dialogUserOnline.visible=true">人员情况</el-button>
            </template>
            <template slot="menuRight"></template>
        </nodes-crud>
    </basic-container>
</template>

<script>
import changeUser from "./task/changeUser";
import userOnline from "./task/userOnline";
import detail from "./task/detail";
import {getList, close} from "@/api/wms/core/task";
import {mapGetters} from "vuex";

export default {
    name: "task",
    components: {changeUser, userOnline, detail},
    data() {
        return {
            loading: false,
            data: [],
            selectionList: [],
            option: {
                newBtn: false,
                multiDelBtn: false,
                viewBtn: false,
                editBtn: false,
                delBtn: false,
                menu: true,
                menuItem: [
                    {
                        label: '查看详情',
                        command: 1,
                    }
                ],
                column: [
                    {
                        label: "库房",
                        prop: "whId",
                        search: true,
                        type: "select",
                        dicUrl: "/api/wms/warehouse/warehouse/list",
                        props: {
                            label: "whName",
                            value: "whId"
                        },
                    },
                    {
                        label: "任务类型",
                        prop: "taskTypeCd",
                        search: true,
                        type: "select",
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        },
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.taskType
                    },
                    {
                        label: "执行方式",
                        prop: "taskProcType",
                        sortable: true,
                        width: 120,
                        type: "select",
                        props: {
                            label: "dictValue",
                            value: "dictKey"
                        },
                        dicUrl: "/api/blade-system/dict/dictionary?code=" + this.$dict.taskProcType
                    },
                    {
                        label: "单据编码",
                        prop: "billNo",
                        sortable: true,
                        search: true,
                        width: 120,
                    },
                    {
                        label: "单据种类",
                        prop: "billTypeCd",
                        sortable: true,
                        width: 120,
                        type: "select",
                        dicUrl: "/api/wms/basedata/billtype/list",
                        props: {
                            label: 'billTypeName',
                            value: 'billTypeCd'
                        },
                    },
                    {
                        label: "用户编码",
                        prop: "userCode",
                        sortable: true,
                        width: 120
                    },
                    {
                        label: "用户名称",
                        prop: "userName",
                        sortable: true,
                        width: 120
                    },
                    {
                        label: "任务数量",
                        prop: "taskQty",
                        sortable: true,
                        width: 120
                    },
                    {
                        label: "开始时间",
                        prop: "beginTime",
                        sortable: true,
                        width: 120,
                        type: 'datePicker'
                    },
                    {
                        label: "分配状态",
                        prop: "isAllot",
                        width: 120,
                        search:false
                    },
                    {
                        label: "任务描述",
                        prop: "taskRemark",
                        sortable: true,
                        width: 120
                    },
                    {
                        label: "创建时间",
                        prop: "createTime",
                        overHidden: true,
                        sortable: true,
                        width: 120
                    }
                ]
            },
            dialogChangeUser: {
                visible: false,
                dataSource: []
            },
            dialogUserOnline: {
                visible: false
            },
            dialogDetail: {
                visible: false,
                taskId: undefined
            }
        };
    },
    computed: {
        ...mapGetters(["permission"]),
        permissionList() {
            return {
                cmdBtn: this.vaildData(this.permission.task_command, false),
                changeUserBtn: this.vaildData(this.permission.task_changeUser, false),
                userOnlineBtn: this.vaildData(this.permission.task_userOnline, false),
                detailBtn: this.vaildData(this.permission.task_detail, false),
            }
        },
        ids() {
            let ids = [];
            this.selectionList.forEach(ele => {
                ids.push(ele.taskId);
            });
            return ids.join(",");
        },
    },
    methods: {
        onLoad(page, params = {}) {
            this.loading = true;
            getList(
                page.currentPage,
                page.pageSize,
                Object.assign(params, this.query)
            ).then(res => {
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
        handleCommand(cmd) {
            if (!this.selectionList || this.selectionList.length == 0) {
                this.$message.warning("至少选择一个任务才能执行此操作！");
                return;
            }
            let tag = parseInt(cmd);
            switch (tag) {
                case 1:
                    this.dialogChangeUser.dataSource = this.selectionList;
                    this.dialogChangeUser.visible = true;
                    break;
                case 2:
                    break;
                case 3: // 关闭任务
                    close(this.ids).then(res => {
                        if (res.data.code === 200) {
                            this.$message.success("操作成功！");
                            this.$refs.table.onLoad();
                        }
                    });
                    break;
            }
        },
        changeUserCallback(result) {
            this.dialogChangeUser.visible = result.visible;
            if (result.result) {
                this.$refs.table.onLoad();
            }
        },
        userOnlineCallback(result) {
            this.dialogUserOnline.visible = result.visible;
        },
        detailCallback(result) {
            this.dialogDetail.visible = result.visible;
        },
        showDetail(row) {
            if (row) {
                this.dialogDetail.taskId = row.taskId;
                this.dialogDetail.visible = true;
            }
        },
        menuCommand(cmd, row, index) {
            switch (cmd) {
                case 1:
                    this.showDetail(row);
                    break;
            }
        }
    }
};
</script>

<style scoped>
</style>
