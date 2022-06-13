<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="mini"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>定时任务-新增 </h3>
                    </el-row>

                        <el-row>
                        <el-col :span="8">
                            <el-form-item label="任务名称" prop="crontabTaskName">
                                <el-input v-model="form.params.crontabTaskName"></el-input>
                            </el-form-item>
                        </el-col>
                        </el-row>
                        <el-row>
                        <el-col :span="8">
                            <el-form-item label="url" prop="url">
                                <el-input v-model="form.params.url"></el-input>
                            </el-form-item>
                        </el-col>
                        </el-row>
                       <el-row>
                        <el-col :span="8">
                            <el-form-item label="请求方法" prop="method">
                                <el-input v-model="form.params.method"></el-input>
                            </el-form-item>
                        </el-col>
                       </el-row>
                    <el-row>
                        <el-col :span="8" >
                            <el-form-item label="corn表达式" prop="cron">
                                <el-input v-model="form.params.cron"></el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>


                </el-form>
            </el-main>
            <el-footer>
                <el-row style="margin-top: 10px;line-height:60px;text-align:right;">
                    <el-button
                        :loading="loading"
                        type="primary"
                        @click="onSubmit"
                    >
                        保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >
                        关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>

import {newCrontabTask} from "@/api/crontab/task";
import {editMixin} from "@/mixins/edit";

export default {
    name: "new",
    mixins: [editMixin],
    data() {
        return {
            form: {
                params: {
                    crontabTaskName: '',
                    url: '',
                    method: '',
                    cron: '',
                    enabled:0
                },
                rules: {

                    crontabTaskName: [
                        {
                            required: true,
                            message: '请输入任务名称',
                            trigger: 'blur'
                        }
                    ],
                    url: [
                        {
                            required: true,
                            message: '请输入url',
                            trigger: 'blur'
                        }
                    ],
                    method: [
                        {
                            required: true,
                            message: '请输入请求方法',
                            trigger: 'blur'
                        }
                    ],
                    cron: [
                        {
                            required: true,
                            message: '请输入cron表达式',
                            trigger: 'blur'
                        }
                    ]

                }
            }
        }
    },
    created() {

    },
    methods: {
        submitFormParams() {
          return newCrontabTask(this.form.params)
          .then(res => {
            return {
              msg: res.data.msg,
              router: {
                            path: '/wms/crontab/crontabTask1',
                        }
            };
          });
        },

    }
}
</script>

<style scoped>
.d-table-header-required:before {
    content: "*";
    color: #F56C6C;
    margin-right: 4px;
}
</style>
