<template>
    <el-dialog
        title="下级菜单配置"
        :visible.sync="visible"
        width="30%"
        :close-on-click-modal="false"
        @open="dialogOpen"
        :before-close="dialogClose"
        v-dialogDrag="true"
        top="3vh"
        class="dialogs"
        append-to-body
    >
        <div class="dialog-body" v-loading="loading.content">
            <el-tabs type="border-card">
                <el-checkbox @change="handleCheckAllChange">全选</el-checkbox>
<!--                <el-tab-pane label="菜单权限">-->
                        <div class="fff" style="height: 400px">
                            <el-tree
                                :data="dataTree"
                                show-checkbox
                                :props="defaultProps"
                                ref="treeMenu"
                                node-key="id"
                                :default-checked-keys="menuTreeObj"
                            ></el-tree>
                        </div>
                </el-tab-pane>
<!--                <el-tab-pane label="数据权限">-->
<!--                        <div class="fff" style="height: 400px">-->
<!--                            <el-tree-->
<!--                                :data="dataScopeGrantList"-->
<!--                                show-checkbox-->
<!--                                :props="defaultProps"-->
<!--                                :default-checked-keys="dataScopeTreeObj"-->
<!--                                ref="dataScopeGrantList"-->
<!--                                node-key="id"-->
<!--                            ></el-tree>-->
<!--                        </div>-->
<!--                </el-tab-pane>-->
<!--                <el-tab-pane label="接口权限">-->
<!--                        <div class="fff" style="height: 400px">-->
<!--                            <el-tree-->
<!--                                :data="apiScopeGrantList"-->
<!--                                show-checkbox-->
<!--                                :props="defaultProps"-->
<!--                                :default-checked-keys="apiScopeTreeObj"-->
<!--                                ref="apiScopeGrantList"-->
<!--                                node-key="id"-->
<!--                            ></el-tree>-->
<!--                        </div>-->
<!--                </el-tab-pane>-->
            </el-tabs>
        </div>
        <span slot="footer" class="dialog-footer">
      <el-button type="primary" @click="save" :loading="loading.save"
      >确定</el-button
      >
      <el-button @click="dialogClose">取 消</el-button>
    </span>
    </el-dialog>
</template>
<script>
import { grantTree, grant, getTopTree } from "@/api/core/topMenu";

export default {
    name: "roleJurisDialog",
    checked:false,
    data() {
        return {
            defaultProps: {
                children: "children",
                label: "title",
            },
            loading: {
                save: false,
                content: false,
            },
            callback: {
                visible: false,
                result: false,
            },
            ops: {
                rail: {
                    background: "rgb(32, 34, 42);",
                },
                bar: {
                    background: "rgba(144, 146, 152, 0.3)",
                    //onlyShowBarOnScroll: true,
                },
            },
            dataTree: [],
            dataScopeGrantList: [],
            apiScopeGrantList: [],
            menuTreeObj: [], //选中菜单权限回显数据
            dataScopeTreeObj: [], //选中数据权限回显数据
            apiScopeTreeObj: [], //选中接口权限回显数据
        };
    },
    props: {
        visible: { type: Boolean, default: false },
        dataSource: { type: String, default: "" },
    },
    methods: {
        //确认
        save() {
            const menuList = this.$refs.treeMenu.getCheckedKeys();
            // const dataScopeList = this.$refs.dataScopeGrantList.getCheckedKeys();
            // const apiScopeList = this.$refs.apiScopeGrantList.getCheckedKeys();

            this.loading.save = true;
            let ids = this.dataSource.split(",");
            grant(ids, menuList).then(() => {
                this.loading.save = false;
                this.$message.success("操作成功!");
                this.callback.visible = false;
                this.callback.result = true;
                this.$emit("callback", this.callback);
            });
        },
        dialogClose() {
            this.callback.visible = false;
            this.callback.result = false;
            this.$emit("callback", this.callback);
        },
        dialogOpen() {
            this.loading.content = true;
            let loadingCount = 0;
            loadingCount++;
            grantTree()
                .then((res) => {
                    this.dataTree = res.data.data.menu;
                    this.dataScopeGrantList = res.data.data.dataScope;
                    this.apiScopeGrantList = res.data.data.apiScope;
                })
                .finally(() => {
                    loadingCount--;
                });
            loadingCount++;
            getTopTree(this.dataSource)
                .then((res) => {
                    this.menuTreeObj = res.data.data.menu;
                    this.dataScopeTreeObj = res.data.data.dataScope;
                    this.apiScopeTreeObj = res.data.data.apiScope;
                })
                .finally(() => {
                    loadingCount--;
                });
            let self = this;
            let interval = setInterval(function () {
                if (loadingCount === 0 && self.loading.content) {
                    self.loading.content = false;
                    clearInterval(interval);
                }
            }, 100);
        },
        handleCheckAllChange() {
            if (this.checked) {
                this.$refs.treeMenu.setCheckedNodes([]);//取消全选
                this.checked = false;
            } else {
                this.$refs.treeMenu.setCheckedNodes(this.dataTree); // 全选
                this.checked = true;
            }
        }
    },
};
</script>
<style lang="scss" scoped>
/deep/ .el-tabs__content {
    overflow: auto;
}
</style>
