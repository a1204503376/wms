<template>
    <el-dialog title="生成补货任务"
               :visible.sync="visible"
               :close-on-click-modal="false"
               @open="dialogOpen"
               :before-close="dialogClose"
               v-dialogDrag="true"
               width="80%"
               top="3vh"
               class="dialogs"
               append-to-body>
        <div class="dialog-body">
            <el-table
                    :data="form.tableData"
                    border
                    height="500"
                    style="width: 100%"
                    show-overflow-tooltip
                    v-loading="loading"
            >
                <el-table-column
                        type="index"
                        width="50">
                </el-table-column>
                <el-table-column prop="whName" label="库房" width="140" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="ownerName" label="货主" width="140" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="skuCode" label="物品编码" width="160" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="skuName" label="物品名称" width="160" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="wspName" label="包装" width="160" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="spec" label="规格" width="120" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="billCount" label="订单数量" :min-width="80" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="totalNeedQty" label="出库总量" :min-width="80" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="sourceZoneQty" label="可出库总量" width="100" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="repQty" label="补货数量" :min-width="120" :show-overflow-tooltip="true">
                    <template slot-scope="scope">
                        <el-input-number
                                v-model="scope.row['repQty']"
                                size="medium"
                                controls-position="right"
                                :min="0"
                                style="width:100%;"
                        ></el-input-number>
                    </template>
                </el-table-column>
                <el-table-column prop="totalStockQty" label="库存总量" :min-width="80" :show-overflow-tooltip="true">
                </el-table-column>
                <el-table-column prop="wsuName" label="计量单位" :min-width="80" :show-overflow-tooltip="true">
                </el-table-column>
            </el-table>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button type="primary"
                       @click="save"
                       :loading="loading">保 存
            </el-button>
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {createReplenishTask} from "@/api/wms/stock/transferheader";

    export default {
        name: "createTask",
        components: {},
        props: {
            visible: {type: Boolean, default: false},
            dataSource: {
                type: Array, default: function () {
                    return [];
                }
            }
        },
        data() {
            return {
                loading: false,
                form: {
                    tableData: [],
                },
                callback: {
                    result: false,
                    visible: false,
                },
                rules: {
                    repQty: [
                        {required: true, message: "补货数量不能为空", trigger: "blur"}
                    ]
                }
            }
        },
        methods: {
            dialogOpen() {
                this.form.tableData = Object.assign([], this.dataSource);
            },
            dialogClose() {
                this.form.tableData = [];
                this.callback.result = false;
                this.callback.visible = false;
                this.$emit('callback', this.callback);
            },
            save() {
                this.loading = true;
                createReplenishTask(this.form.tableData).then(res => {
                    this.$message.success('操作成功！');
                    this.callback.result = true;
                    this.callback.visible = false;
                    this.$emit('callback', this.callback);
                }).finally(() => {
                    this.loading = false;
                });
            }
        }
    }
</script>

<style scoped>

</style>
