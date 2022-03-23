<template>
    <div class="icon-box">
        <el-input
                readonly
                placeholder="请选择图标"
                v-model="value"
                @focus="showIconDialog = true"
                :disabled="isView"
        >
            <el-button slot="append" :icon="value" @click="showIconDialog = true" :disabled="isView"></el-button>
        </el-input>
        <el-dialog :title="icontitleText" :visible.sync="showIconDialog" width="60%" append-to-body>
            <el-tabs v-model="activeName">
                <el-tab-pane label="通用图标" name="first">
                    <el-row>
                        <el-col
                                v-for="(item,index) in cities"
                                :key="index"
                                :span="2"
                                style="margin-bottom:15px"
                        >
                            <el-button :icon="item" @click="selectIcon(item)"></el-button>
                        </el-col>
                    </el-row>
                </el-tab-pane>
                <el-tab-pane label="系统图标" name="second">
                    <el-row>
                        <el-col
                                v-for="(item,index) in citiesTwo"
                                :key="index"
                                :span="2"
                                style="margin-bottom:15px"
                        >
                            <el-button :icon="item" @click="selectIcon(item)"></el-button>
                        </el-col>
                    </el-row>
                </el-tab-pane>
            </el-tabs>
            <span slot="footer" class="dialog-footer">
        <!-- <el-button type="primary" @click="childAdd">确 认</el-button> -->
        <el-button @click="childCancel">关 闭</el-button>
      </span>
        </el-dialog>
    </div>
</template>

<script>
    import iconList from "@/components/config/iconList.js";

    export default {
        name: "iconList",
        props: {
            value: {
                type: String,
                default: ""
            },
            icontitleText: {
                type: String,
                default: "新增"
            },
            isView: {
                type: Boolean,
                default: false
            }
        },
        model: {
            prop: "value",
            event: "change"
        },
        data() {
            return {
                activeName: "first",
                showIconDialog: false,
                cities: iconList[0].list,
                citiesTwo: iconList[1].list
            };
        },
        methods: {
            selectIcon(iconName) {
                this.$emit("change", iconName);
                this.showIconDialog = false;
            },
            childAdd() {
                this.$emit("child-add");
            },
            //取消
            childCancel(menuForm) {
                this.showIconDialog = false;
            }
        }
    };
</script>

<style lang="scss" scoped>
</style>
