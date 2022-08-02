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
        value-key="id"
        :multiple="multiple"
        :size="size"
        :disabled="disabled"
        :clearable="true"
        @clear="clear"
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
        selectVal: [Object, Array],
        // 是否多选 true:多选 默认为单选
        multiple: {type: Boolean, required: false, default: () => false},
        // 组件大小，默认为mini, 支持 medium/small/mini
        size: {type: String, required: false, default: () => "mini"},
        // 是否禁用 默认为 false不禁用
        disabled: {type: Boolean, required: false, default: () => false}
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
        selectVal() {
            this.setDefaultByProps();
        }
    },
    methods: {
        setDefaultByProps() {
            this.val = this.selectVal;
            if (!this.isEdit) {
                return;
            }
            let currentSupplier = this.options.find(item => item.id === this.selectVal.id);
            if (func.isEmpty(currentSupplier)) {
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
            if (val === "") {
                val = {
                    id: '',
                    code: '',
                    name: ''
                }
            }
            this.$emit('selectValChange', val);
        },
        clear() {
            console.log("clear");
            // TODO 默认清空方法将 selectVal 变成了 "", 调教表单报错
        }
    }
}
</script>

<style scoped>

</style>
