<template>
    <basic-container>
        <el-container id="container" v-loading="loading">
            <el-main style="overflow: hidden;overflow-y: scroll;">
                <el-form ref="form"
                         :model="form.params"
                         :rules="form.rules"
                         label-position="right"
                         label-width="120px"
                         size="medium"
                         style="margin-left:10px;margin-right:10px;"
                >
                    <el-row>
                        <h3>常规</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库位编码" prop="locCode">
                                <el-input
                                    size="medium"
                                    :clearable="true"
                                    v-model="form.params.locCode"
                                    :disabled="true"
                                    placeholder="请输入编码"
                                    type="text"
                                ></el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库房" prop="whId">
                                <nodes-warehouse
                                    size="medium"
                                    v-model="form.params.whId"
                                ></nodes-warehouse>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库区" prop="zoneId">
                                <nodes-zone
                                    size="medium"
                                    v-model="form.params.zoneId"
                                ></nodes-zone>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库位类型" prop="locType">
                                <nodes-dictionary
                                    size="medium"
                                    v-model="form.params.locType"
                                    code="loc_type"
                                ></nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位种类" prop="locCategory">
                                <nodes-dictionary
                                    size="medium"
                                    v-model="form.params.locCategory"
                                    code="loc_category"
                                ></nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位处理" prop="locHandling">
                                <nodes-dictionary
                                    size="medium"
                                    v-model="form.params.locHandling"
                                    code="loc_handling"
                                ></nodes-dictionary>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="校验数位" prop="checkDigit">
                                <el-input
                                    size="medium"
                                    :clearable="true"
                                    v-model="form.params.checkDigit"
                                    maxlength="50"
                                    placeholder="请输入校验数位"
                                    show-word-limit
                                    type="text"
                                >
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="线路顺序" prop="logicAllocation">
                                <el-input-number
                                    v-model="form.params.logicAllocation"
                                    :min="0"
                                    controls-position="right"
                                    size="medium"></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="使用状态" prop="locFlag">
                                <nodes-dictionary
                                    size="medium"
                                    v-model="form.params.locFlag"
                                    code="loc_flag">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ABC分类" prop="abc">
                                <nodes-dictionary
                                    size="medium"
                                    v-model="form.params.abc"
                                    code="abc"
                                ></nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="上架顺序" prop="putOrder">
                                <el-input-number
                                    size="medium"
                                    v-model="form.params.putOrder"
                                    :min="0"
                                    controls-position="right"></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="容器类别" prop="lpnTypeId">
                                <nodes-lpn-type
                                    size="medium"
                                    v-model="form.params.lpnTypeId">
                                </nodes-lpn-type>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>维数</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="长度" prop="locLength">
                                <el-input
                                    size="medium"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    v-model="form.params.locLength"
                                    placeholder="请输入长度"
                                    :clearable="true">
                                    <template slot="append">mm</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="高度" prop="locHigh">
                                <el-input
                                    size="medium"
                                    v-model="form.params.locHigh"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入高度"
                                    :clearable="true">
                                    <template slot="append">mm</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="宽度" prop="locWide">
                                <el-input
                                    size="medium"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    :clearable="true"
                                    v-model="form.params.locWide"
                                    placeholder="请输入宽度">
                                    <template slot="append">mm</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="货架层" prop="locLevel">
                                <el-input
                                    size="medium"
                                    :clearable="true"
                                    v-model="form.params.locLevel"
                                    maxlength="50"
                                    placeholder="请输入货架层"
                                    show-word-limit
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架列" prop="locColumn">
                                <el-input
                                    size="medium"
                                    :clearable="true"
                                    v-model="form.params.locColumn"
                                    maxlength="50"
                                    placeholder="请输入货架列"
                                    show-word-limit
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架排" prop="locBank">
                                <el-input
                                    size="medium"
                                    :clearable="true"
                                    v-model="form.params.locBank"
                                    maxlength="50"
                                    placeholder="请输入货架排"
                                    show-word-limit
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容量" prop="capacity">
                                <el-input
                                    size="medium"
                                    v-model="form.params.capacity"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入容量"
                                    :clearable="true">
                                    <template slot="append">mm³</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="载重量" prop="loadWeight">
                                <el-input
                                    size="medium"
                                    v-model="form.params.loadWeight"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入载重量"
                                    :clearable="true">
                                    <template slot="append">kg</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="最大件数" prop="itemNum">
                                <el-input-number
                                    size="medium"
                                    v-model="form.params.itemNum"
                                    :min="0"
                                    controls-position="right"></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="最大托数" prop="trayNum">
                                <el-input-number
                                    size="medium"
                                    v-model="form.params.trayNum"
                                    :min="0"
                                    controls-position="right"></el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>混放规则</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="混放物品" prop="locSkuMix">
                                <el-select v-model="form.params.locSkuMix" clearable placeholder="请选择">
                                    <el-option
                                        v-for="item in form.mixOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                        <span style="float: left">{{ item.label }}</span>
                                    </el-option>
                                </el-select>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="混放批号" prop="locLotNoMix">
                                <el-select v-model="form.params.locLotNoMix" clearable placeholder="请选择">
                                    <el-option
                                        v-for="item in form.mixOptions"
                                        :key="item.value"
                                        :label="item.label"
                                        :value="item.value">
                                        <span style="float: left">{{ item.label }}</span>
                                    </el-option>
                                </el-select>
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
                        @click="onSubmit">保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose">关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>

import {editMixin} from "@/mixins/edit";
import {detailByEdit, edit} from "@/api/wms/basics/location"
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesZone from "@/components/wms/select/NodesZone";
import NodesDictionary from "@/components/wms/select/NodesDictionary";
import func from "@/util/func";
import NodesLpnType from "@/components/wms/select/NodesLpnType";

export default {
    name: "add",
    components: {NodesDictionary, NodesZone, NodesWarehouse, NodesLpnType},
    mixins: [editMixin],
    props: {
        locId: {type: String, required: true}
    },
    data() {
        return {
            form: {
                params: {
                    locCode: '',
                    whId: '',
                    zoneId: '',
                    locType: '',
                    locCategory: '',
                    locHandling: '',
                    checkDigit: '',
                    logicAllocation: '',
                    locFlag: '',
                    abc: '',
                    lpnTypeId: '',
                    locHigh: '',
                    locLength: '',
                    locWide: '',
                    capacity: '',
                    loadWeight: '',
                    locLevel: '',
                    locBank: '',
                    locColumn: '',
                    putOrder: 0,
                    itemNum: 0,
                    trayNum: 0,
                    locSkuMix: '',
                    locLotNoMix: ''
                },
                rules: {
                    locCode: [
                        {
                            required: true,
                            message: '请输入库位编码',
                            trigger: 'blur'
                        }
                    ],
                    whCode: [
                        {
                            required: true,
                            message: '请选择库房',
                            trigger: 'change'
                        }
                    ],
                    zoneId: [
                        {
                            required: true,
                            message: '请选择库区',
                            trigger: 'change'
                        }
                    ],
                },
                mixOptions: [
                    {
                        label: '允许',
                        value: "1"
                    },
                    {
                        label: '不允许',
                        value: "0"
                    }
                ]
            },
        }
    },
    created() {
        this.getDataSource();
    },
    watch: {
        locId() {
            this.refreshTable();
        }
    },
    methods: {
        refreshTable() {
            this.getDataSource();
        },
        getDataSource() {
            if (func.isEmpty(this.locId)) {
                return;
            }
            detailByEdit(this.locId)
                .then((res) => {
                    this.form.params = res.data.data
                })
        },
        submitFormParams() {
            return edit(this.form.params)
                .then(res => {
                    return {
                        msg: res.data.msg,
                        router: {
                            path: '/wms/warehouse/location',
                            query: {
                                isRefresh: 'true'
                            }
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

.el-input {
    width: 210px
}
</style>
