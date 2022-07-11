<template>
    <el-select
        v-model="val"
        :collapse-tags="true"
        :default-first-option="true"
        :loading="loading"
        :multiple="multiple"
        :remote-method="remoteMethod"
        :size="size"
        filterable
        placeholder="请输入客户编码或名称"
        remote
        reserve-keyword
        value-key="id"
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
import {getCustomerSelectResponseTop10List} from "@/api/wms/basics/customer";
import debounce from "lodash/debounce";
import func from "@/util/func";

export default {
    name: "NodesCustomer",
    model: {
        prop: 'selectVal',
        event: 'selectValChange'
    },
    props: {
        selectVal: [Array, String, Object],
        size: {type: String, required: false, default: () => "mini"},
        // 单选多选切换，默认为false
        multiple: {type: Boolean, required: false, default: false}
    },
    data() {
        return {
            options: [],
            val: this.selectVal,
            loading: false,
        }
    },
    watch: {
        selectVal(){
            this.setDefaultByProps();
        }
    },
    methods: {
        setDefaultByProps() {
            if (!func.isNotEmpty(this.selectVal)) {
                return;
            }
            let currentCustomer = this.options.find(item => item.id === this.selectVal.id);
            if (func.isEmpty(currentCustomer)) {
                this.options.push(this.selectVal);
            }
            this.val = this.selectVal;
        },
        // 防抖 在等待时间到达前的请求全部取消，保留最后一次
        remoteMethod: debounce(async function (key) {
            if (key !== '') {
                this.loading = true;
                let customerSelectQuery = {
                    key
                };
                let {data: {data}} = await getCustomerSelectResponseTop10List(customerSelectQuery);
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
