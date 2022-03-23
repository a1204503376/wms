<template>
    <el-dialog title="数据校验"
               :visible.sync="visible"
               :close-on-click-modal="false"
               :close-on-press-escape="false"
               width="70%"
               @opened="dialogOpen"
               @close="cancel"
               append-to-body>
        <div class="el-dialog__body">
            <el-row style="padding:10px;">
                <el-col :span="18">文件名称：{{ this.fileName}}</el-col>
                <el-col :span="6" :style="{color:statusColor}">{{ this.status }}</el-col>
            </el-row>
            <el-row>
                <el-col :span="24">
                    <el-table
                        :data="tableData"
                        stripe
                        v-loading="loading"
                        height="400"
                        style="width: 100%">
                        <el-table-column
                            prop="index"
                            label="行号"
                            width="180">
                        </el-table-column>
                        <el-table-column
                            prop="message"
                            label="异常信息">
                        </el-table-column>
                    </el-table>
                </el-col>
            </el-row>
            <el-row style="padding:10px;">
                <el-col>
                    <span>{{ this.verifyResult }}</span>
                    <span style="color:green;">{{ this.successInfo }}</span>
                    <span style="color:red;">{{ this.errorInfo }}</span>
                </el-col>
            </el-row>
        </div>
        <span slot="footer" class="dialog-footer">
        <el-button style="float:left;" type="warning" plain
                   @click="onCopy">拷贝至剪贴板</el-button>
        <el-button @click="success" type="primary">保 存</el-button>
        <el-button @click="cancel">关 闭</el-button>
    </span>
    </el-dialog>
</template>

<script>
import request from '@/router/axios';

export default {
    name: "dataVerify",
    props: {
        visible: {type: Boolean, default: false},
        dataSource: {
            type: Object, default:function(){
                return {};
            }
        },
        dataVerifyUrl: {type: String, default: ""},
        uploadUrl: {type: String, default: ""}
    },
    data() {
        return {
            tableData: [],
            tableDataStr: [],
            allData: [],
            loading: true,
            status: '',
            statusColor: 'orange',
            verifyResult: '',
            successInfo: '',
            errorInfo: '',
            confirmVisible: false,
            fileName:'',
            callback: {
                visible: false,
                result: false
            }
        }
    },
    methods: {
        dialogOpen() {
            this.loading = true;
            this.status = '正在校验，请稍后...';
            this.statusColor = 'orange';
            this.verifyResult = '';

            this.fileName = this.dataSource.localFile.file.name;
            let formData = new FormData();
            formData.append("file", this.dataSource.localFile.file);
            request({
                url: this.dataVerifyUrl,
                method: "post",
                headers: {"Content-Type": "multipart/form-data"},
                data: formData,
            }).then(res => {
                this.allData = res.data.data;
                this.tableData = this.allData.filter(item => {
                    return !(item.message === null || item.message.length === 0);
                });
                this.loading = false;
                if (!this.allData || this.allData.length === 0) {
                    this.status = '效验完成，没有可保存数据！';
                    this.statusColor = 'orange';
                } else if (this.tableData && this.tableData.length > 0) {
                    this.status = '效验完成！';
                    this.statusColor = 'red';
                    this.tableDataStr = '';
                    this.tableDataStr = '序号\t\t\t异常消息\r\n';
                    for (let i = 0; i < this.tableData.length; i++) {
                        let obj = this.tableData[i];
                        this.tableDataStr += (obj.index + '\t\t\t' + obj.message + '\r\n');
                    }
                } else {
                    this.status = '校验完成，正在保存数据...';
                    this.statusColor = 'green';
                    if (this.allData.length !== 0) {
                        this.success();
                    }
                }
                this.verifyResult = '校验结果：';
                this.successInfo = '通过 ' + (this.allData.length - this.tableData.length) + '条  ';
                this.errorInfo = '异常 ' + this.tableData.length + ' 条';
            }).catch((err) => {
                this.cancel();
            });
        },
        cancel() {
            this.verifyResult = '';
            this.successInfo = '';
            this.errorInfo = '';
            this.loading = false;
            this.tableData = [];
            this.callback.visible = false;
            this.callback.result = false;
            this.$emit('callback', this.callback);
        },
        success() {
            // 筛选出通过验证的数据
            let sucessData = this.allData.filter(item => {
                return item.message === null || item.message.length === 0;
            });
            // 根据键去除重复
            const data = new Map();
            sucessData.filter((item) => !data.has(item.cacheKey) && data.set(item.cacheKey, item));
            if (data.size === 0) {
                this.$message.warning("没有数据可保存！");
                return;
            }
            if (this.tableData.length > 0) {
                this.confirmVisible = true;
                this.$confirm('是否确认不处理异常数据，继续保存验证通过的数据？', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning',
                    appendToBody: true
                }).then(() => {
                    this.submitData([...data.values()]);
                });
            } else {
                this.confirmVisible = false;
                this.submitData([...data.values()]);
            }
        },
        submitData(data) {
            if (this.uploadUrl && this.uploadUrl.length > 0) {
                request({
                    url: this.uploadUrl,
                    method: 'post',
                    data: data
                }).then(() => {
                    this.callback.visible = false;
                    this.callback.result = true;
                    this.$emit('callback', this.callback);
                });
            } else {
                this.callback.visible = false;
                this.callback.result = true;
                this.callback.data = data;
                this.$emit('callback', this.callback);
            }
        },
        onCopy() {
            this.$Clipboard({
                text: this.tableDataStr
            }).then(() => {
                this.$message({message: '内容已拷贝到剪切板', type: 'success'});
            }).catch(() => {
                this.$message({message: '拷贝失败！：', type: 'error'});
            });
        }
    }
}
</script>

<style scoped>
</style>
