<template>
    <el-dialog
            :title="title"
            width="80%"
            top="3vh"
            append-to-body
            v-dialogDrag="true"
            :close-on-click-modal="false"
            :visible.sync="visible"
            :before-close="dialogClose"
            @open="dialogOpen"
    >
        <el-table ref="table"
                  :data="data"
                  height=500
                  style="cursor:default;"
                  v-loading="loading"
                  element-loading-text="数据正在加载中"
                  element-loading-spinner="el-icon-loading"
                  highlight-current-row
                  border
        >
            <el-table-column prop="userCode" v-if="false" label="用户编码" show-overflow-tooltip></el-table-column>
            <el-table-column prop="userName" label="用户名称" show-overflow-tooltip></el-table-column>
            <el-table-column prop="mobilePhone" label="联系方式" show-overflow-tooltip></el-table-column>
            <el-table-column prop="ipAddress" label="IP地址" show-overflow-tooltip></el-table-column>
            <el-table-column prop="macAddress" label="MAC地址" show-overflow-tooltip></el-table-column>
            <el-table-column prop="onlineTerminal" label="登录终端" show-overflow-tooltip></el-table-column>
            <el-table-column prop="lastLoginTime" label="登记时间" show-overflow-tooltip></el-table-column>
        </el-table>
        <span slot="footer" class="dialog-footer">
            <el-button @click="dialogClose">关 闭</el-button>
        </span>
    </el-dialog>
</template>

<script>
    import {getList} from "@/api/wms/core/userOnline";

    export default {
        name: "userOnline",
        props: {
            visible: {type: Boolean, default: false}
        },
        data() {
            return {
                title: '人员情况',
                loading: false,
                data: []
            }
        },
        methods: {
            dialogClose() {
                let dialogResult = {
                    visible: false
                }
                this.$emit('dialogResult', dialogResult);
            },
            dialogOpen() {
                this.loading = true;
                getList({loginStatus: 1}).then(res => {
                    this.data = res.data.data;
                    this.loading = false;
                });
            },
        }
    }
</script>

<style scoped>

</style>
