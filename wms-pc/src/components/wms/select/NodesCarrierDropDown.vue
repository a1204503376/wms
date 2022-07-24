<template>
    <el-select
        filterable
        v-model="val"
        collapse-tags
        placeholder="请选择"
        :multiple="multiple"
        :clearable="true"
        size="mini"
        style="width:100%;"
        :default-first-option="true"
        :loading="loading"
        :remote-method="remoteMethod"
        remote
        reserve-keyword
        value-key="skuCode"
        @change="onChange">
        <el-option
            v-for="item in dataSource"
            :key="item.code"
            :label="item.name"
            :value="item.code">
            <span style="float: left">{{ item.name }}</span>
            <span style="float: right; color: #8492a6; font-size: 13px">{{ item.code }}</span>
        </el-option>
    </el-select>

</template>

<script>
    // eslint-disable-next-line no-unused-vars
    import {carrierService} from "@/api/wms/basics/Carrier";
    import debounce from "lodash/debounce";


    export default {
        name: "carrierService",
        model: {
            prop: 'selectVal',
            event: 'selectValChange'
        },
        props: {
            selectVal: [Array, String],
            // 单选多选切换，默认为false
            multiple: {type: Boolean, required: false, default: false}
        },
        data() {
            return {
                val:this.selectVal,
                dataSource: []
            }
        },
        watch: {
            selectVal(newVal) {
              this.val=newVal;
              this.getDataSource();
            }
        },
        async created() {
            await this.remoteMethod();
        },
        methods: {
            // 防抖 在等待时间到达前的请求全部取消，保留最后一次
            remoteMethod: debounce(async function (key) {
                if (key !== '') {
                    this.loading = true;
                    let DropDownSelectQuery = {
                        nameOrCode:key
                    };
                    this.dataSource  = await carrierService.getDropDown(DropDownSelectQuery);
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
