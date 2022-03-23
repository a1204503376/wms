<template>
    <el-dialog :title="title"
               :visible.sync="visible"
               :close-on-click-modal="true"
               @open="dialogOpen"
               :before-close="dialogClose"
               v-dialogDrag="true"
               :modal="false"
               width="60%"
               top="3vh"
               class="dialogs"
               append-to-body
               destroy-on-close>
        <div class="dialog-body">
            <el-table
                    v-loading="loading"
                    :data="data"
                    style="width: 100%"
                    height="300">
                <el-table-column
                        prop="serialNumber"
                        label="序列号">
                </el-table-column>
            </el-table>
        </div>
        <div slot="footer" class="dialog-footer">
            <el-button @click="dialogClose">关 闭</el-button>
        </div>
    </el-dialog>
</template>

<script>
    import {getList} from "@/api/wms/instock/sn";

    export default {
        name: "serial",
        props: {
            visible: {type: Boolean, default: true},
            dataSource: {type: String, default: ''}
        },
        data() {
            return {
                title: '序列号',
                loading: false,
                data: [],
                callback: {
                    visible: false
                }
            }
        },
        methods: {
            dialogOpen() {
                this.loading = true;
                getList(this.dataSource).then(res => {
                    const data = res.data.data;
                    this.data = [];
                    if (data && data.length > 0) {
                        data.forEach(item => {
                            this.data.push({serialNumber: item});
                        });
                    }
                }).finally(() => {
                    this.loading = false;
                });
            },
            dialogClose() {
                this.data = [];
                this.callback.visible = false;
                this.$emit('callback', this.callback);
            }
        }
    }
</script>

<style scoped>

</style>
