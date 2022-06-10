<template>
    <el-select
        v-model="val"
        :default-first-option="true"
        :loading="loading"
        :remote-method="remoteMethod"
        filterable
        placeholder="请输入供应商编码或名称"
        remote
        reserve-keyword
        size="mini"
        style="width: 450px"
        value-key="id"
        :multiple="multiple"
        @change="onChange">
        <el-option
            v-for="item in options"
            :key="item.id"
            :label="item.name"
            :value="item">
            <span style="float: left">{{ item.code }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.name }}</span>
        </el-option>
    </el-select>
</template>

<script>
import {getSupplierSelectResponseTop10List} from "@/api/wms/basics/supplier";
import debounce from "lodash/debounce";
import func from "@/util/func";

export default {
    name: "NodesSupplier",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Object,String,Array],
        // 是否多选 true:多选 默认为单选
        multiple: {type: Boolean, required: false, default:()=>false}
    },
    data() {
        return {
            options: [],
            val: this.selectVal,
            loading: false,
            isEdit: func.isNotEmpty(this.selectVal)
        }
    },
    created() {
        this.setDefaultByProps();
    },
    watch: {
        selectVal(){
            this.setDefaultByProps();
        }
    },
    methods: {
        setDefaultByProps(){
            this.val = this.selectVal;
            if (!this.isEdit){
                return;
            }
            let currentSupplier = this.options.find(item => item.id === this.selectVal.id);
            if (func.isEmpty(currentSupplier)){
                this.options.push(this.selectVal);
            }
        },
        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let supplierSelectQuery = {
                    key
                };
                let {data: {data}} = await getSupplierSelectResponseTop10List(supplierSelectQuery);
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
