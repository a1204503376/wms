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
                         style="margin-left:10px;margin-right:10px;">
                    <el-row>
                        <h3>常规</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库位编码" prop="locCode">
                                <el-input
                                    v-model="form.params.locCode"
                                    placeholder="请输入内容"
                                    size="medium"
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库房" prop="whId">
                                <nodes-warehouse
                                    v-model="form.params.whId"
                                    size="medium">
                                </nodes-warehouse>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库区" prop="zoneId">
                                <nodes-zone
                                    v-model="form.params.zoneId"
                                    size="medium">
                                </nodes-zone>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="库位类型" prop="locType">
                                <nodes-dictionary
                                    v-model="form.params.locType"
                                    code="loc_type"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位种类" prop="locCategory">
                                <nodes-dictionary
                                    v-model="form.params.locCategory"
                                    code="loc_category"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="库位处理" prop="locHandling">
                                <nodes-dictionary
                                    v-model="form.params.locHandling"
                                    code="loc_handling"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="校验数位" prop="checkDigit">
                                <el-input
                                    v-model="form.params.checkDigit"
                                    maxlength="50"
                                    placeholder="请输入内容"
                                    show-word-limit
                                    size="medium"
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="线路顺序" prop="logicAllocation">
                                <el-input-number
                                    v-model="form.params.logicAllocation"
                                    :min="0"
                                    controls-position="right"
                                    size="medium">
                                </el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-tooltip placement="top">
                            <div slot="content">破损:不能上架库存<br/>冻结:可以上架库存，但库存状态为冻结<br/>业务系统冻结:不能上架库存到该库位</div>
                            <el-col :span="8">
                                <el-form-item label="库位状态" prop="locFlag">
                                    <nodes-dictionary
                                        v-model="form.params.locFlag"
                                        code="loc_flag"
                                        size="medium">
                                    </nodes-dictionary>
                                </el-form-item>
                            </el-col>
                        </el-tooltip>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="ABC分类" prop="abc">
                                <nodes-dictionary
                                    v-model="form.params.abc"
                                    code="abc"
                                    size="medium">
                                </nodes-dictionary>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="上架顺序" prop="putOrder">
                                <el-input-number
                                    v-model="form.params.putOrder"
                                    :min="0"
                                    controls-position="right"
                                    size="medium"></el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="容器类别" prop="lpnTypeId">
                                <nodes-lpn-type
                                    v-model="form.params.lpnTypeId"
                                    size="medium">
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
                                    v-model="form.params.locLength"
                                    maxLength="10"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入内容" size="medium">
                                    <template slot="append">mm</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="高度" prop="locHigh">
                                <el-input
                                    v-model="form.params.locHigh"
                                    maxLength="10"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入内容" size="medium">
                                    <template slot="append">mm</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="宽度" prop="locWide">
                                <el-input
                                    v-model="form.params.locWide"
                                    maxLength="10"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入内容" size="medium">
                                    <template slot="append">mm</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="货架层" prop="locLevel">
                                <el-input
                                    v-model="form.params.locLevel"
                                    maxlength="50"
                                    placeholder="请输入内容"
                                    show-word-limit
                                    size="medium"
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架列" prop="locColumn">
                                <el-input
                                    v-model="form.params.locColumn"
                                    maxlength="50"
                                    placeholder="请输入内容"
                                    show-word-limit
                                    size="medium"
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="货架排" prop="locBank">
                                <el-input
                                    v-model="form.params.locBank"
                                    maxlength="50"
                                    placeholder="请输入内容"
                                    show-word-limit
                                    size="medium"
                                    type="text">
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="容量" prop="capacity">
                                <el-input v-model="form.params.capacity"
                                          maxLength="10"
                                          oninput="value=value.replace(/[^\d]/g,'')"
                                          placeholder="请输入内容" size="medium">
                                    <template slot="append">mm³</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="载重量" prop="loadWeight">
                                <el-input
                                    v-model="form.params.loadWeight"
                                    maxLength="10"
                                    oninput="value=value.replace(/[^\d]/g,'')"
                                    placeholder="请输入内容" size="medium">
                                    <template slot="append">kg</template>
                                </el-input>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="最大件数" prop="itemNum">
                                <el-input-number
                                    v-model="form.params.itemNum"
                                    :min="0"
                                    controls-position="right"
                                    size="medium">
                                </el-input-number>
                            </el-form-item>
                        </el-col>
                        <el-col :span="8">
                            <el-form-item label="最大托数" prop="trayNum">
                                <el-input-number
                                    v-model="form.params.trayNum"
                                    :min="0"
                                    controls-position="right"
                                    size="medium">
                                </el-input-number>
                            </el-form-item>
                        </el-col>
                    </el-row>
                    <el-row>
                        <h3>混放规则</h3>
                    </el-row>
                    <el-row>
                        <el-col :span="8">
                            <el-form-item label="混放物品" prop="locSkuMix">
                                <el-select v-model="form.params.locSkuMix" clearable placeholder="请选择"
                                           size="medium">
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
                                <el-select v-model="form.params.locLotNoMix" clearable placeholder="请选择"
                                           size="medium">
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
                        @click="onSubmit"
                    >保 存
                    </el-button>
                    <el-button
                        :loading="loading"
                        @click="onClose"
                    >关 闭
                    </el-button>
                </el-row>
            </el-footer>
        </el-container>
    </basic-container>
</template>

<script>

import {editMixin} from "@/mixins/edit";
import {add} from "@/api/wms/basics/location"
import NodesWarehouse from "@/components/wms/select/NodesWarehouse";
import NodesZone from "@/components/wms/select/NodesZone";
import NodesDictionary from "@/components/wms/select/NodesDictionary";
import NodesLpnType from "@/components/wms/select/NodesLpnType";

export default {
    name: "add",
    components: {NodesLpnType, NodesDictionary, NodesZone, NodesWarehouse},
    mixins: [editMixin],
    data() {
        return {
            form: {
                params: {
                    locCode: '',
                    whId: '',
                    zoneId: '',
                    locType: null,
                    locCategory: null,
                    locHandling: null,
                    checkDigit: '',
                    logicAllocation: '',
                    locFlag: null,
                    abc: null,
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
                    whId: [
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
    methods: {
        submitFormParams() {
            return add(this.form.params).then(res => {
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

.el-input {
    width: 210px;
}

.d-table-header-required:before {
    content: "*";
    color: #F56C6C;
    margin-right: 4px;
}


</style>
