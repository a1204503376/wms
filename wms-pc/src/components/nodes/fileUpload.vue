import fileDownload from "js-file-download";
<template>
    <el-dialog
        title="文件上传"
        :visible.sync="visible"
        :close-on-click-modal="false"
        width="500"
        @close="cancel"
        append-to-body
    >
        <div class="el-dialog__body" align="center">
            <el-upload
                class="upload-demo"
                drag
                auto-upload
                with-credentials
                action
                style="height:240px;margin-top:50px;"
                :limit="1"
                :file-list="fileList"
                :on-exceed="onExceed"
                :before-upload="beforeUpload"
                :http-request="uploadFile"
                :on-success="uploadSuccess"
                :on-error="uploadError"
            >
                <i class="el-icon-upload"></i>
                <div class="el-upload__text">
                    将文件拖到此处，或
                    <em>点击上传</em>
                </div>
                <div class="el-upload__tip" slot="tip">只能上传 xls/xlsx 文件</div>
            </el-upload>
            <!--            <div class="el-upload__text">请先下载模板文件：<el-button type="text" @click="window.alert('111')">模板下载</el-button></div>-->
            <!--            <el-button size="medium">模板下载</el-button>-->
        </div>
        <span slot="footer" class="dialog-footer">
      <el-button v-show="this.templateUrl" size="medium" type="success" @click="download">模板下载</el-button>
      <el-button size="medium" @click="cancel">取 消</el-button>
            <!--            <el-button type="primary">确 定</el-button>-->
    </span>
    </el-dialog>
</template>

<script>
import request from "@/router/axios";
import fileDownload from "js-file-download";

export default {
    name: "FileUpload",
    props: {
        // 对话框显隐
        visible: {type: Boolean, default: false},
        // 模板下载url
        templateUrl: {type: String, default: ""},
        // 文件名称
        fileName: {type: String, default: "模板"}
    },
    data() {
        return {
            fileList: [],
            // 返回值
            callback: {
                visible: false, // 对话框显隐
                result: false, // 结果
                data: {
                    localFile: "", // 本地文件
                },
            },
        };
    },
    methods: {
        cancel() {
            this.fileList = [];
            this.callback.visible = false;
            this.callback.result = false;
            this.$emit("callback", this.callback);
        },
        success(localFile, remoteFile) {
            this.fileList = [];
            this.callback.visible = false;
            this.callback.result = true;
            this.callback.data.localFile = localFile;
            this.$emit("callback", this.callback);
        },
        beforeUpload(file) {
            if (
                file.type !==
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" &&
                file.type !== "application/vnd.ms-excel"
            ) {
                this.$message.error("只允许上传Excel文件！");
                return false;
            }
            return true;
        },
        uploadFile(file) {
            this.success(file);
        },
        uploadSuccess(/*response, file, fileList*/) {
            this.$message.success("上传成功！");
        },
        uploadError(err /*, file, fileList*/) {
            this.$message.error("上传失败：" + err);
        },
        onExceed() {
            this.$message.warning("同时只能上传一个文件！");
        },
        download() {
            if (this.templateUrl) {
                request({
                    url: this.templateUrl,
                    method: 'get',
                    responseType: 'blob'
                }).then(res => {
                    fileDownload(res.data, this.fileName + '.xlsx');
                    this.$message.success("下载成功！");
                });
            }
        },
    },
};
</script>

<style scoped>
</style>
