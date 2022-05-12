<template>
    <el-select
        v-model="val"
        :default-first-option="true"
        :loading="loading"
        :remote-method="remoteMethod"
        filterable
        placeholder="请输入关键词"
        remote
        reserve-keyword
        size="mini"
        style="width: 300px"
        value-key="billTypeId"
        :multiple="multiple"
        :io-type="ioType"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.billTypeCd"
            :label="item.billTypeName"
            :value="item">
            <span style="float: left">{{ item.billTypeCd }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.billTypeName }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getBillTypeSelectResponseTop10List} from "@/api/wms/basics/billType";
import debounce from "lodash/debounce";
import func from "@/util/func";

export default {
    name: "NodesBillType",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Object,String,Array],
        // 是否多选 true:多选 默认为单选
        multiple: {type: Boolean, required: false, default:()=>false},
        // 查询的单据类型，"":查询所有, "I":查询入库单据类型, "0":查询出库单据类型
        ioType: {type: String, required: false, default:()=>''}
    },
    data() {
        return {
            options: [this.selectVal],
            val: this.selectVal,
            ioTypeVal: this.ioType,
            loading: false,
        }
    },
    watch: {
        val(newVal) {
            let result = newVal;
            if (func.isArray(newVal)) {
                result = newVal.map(d => d.billTypeId);
            } else if (func.isObject(newVal)) {
                result = newVal.billTypeId
            }
            this.$emit('selectValChange', result);
        }
    },
    methods: {
        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let BillTypeSelectQuery = {
                    key: key,
                    ioType: this.ioTypeVal
                };
                let {data: {data}} = await getBillTypeSelectResponseTop10List(BillTypeSelectQuery);
                this.options = data;
                this.loading = false;
            } else {
                this.options = [];
            }
        }, 500),
        onChange(val) {
            this.$emit('selectValChange', val);
        }
    }
}
</script>

<style scoped>

</style>
