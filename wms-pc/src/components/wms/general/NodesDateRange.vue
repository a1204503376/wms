<template>
    <el-date-picker
        style="width: 100%"
        v-model="value"
        @change="onChange"
        type="daterange"
        unlink-panels
        value-format="yyyy-MM-dd"
        format="yyyy-MM-dd"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        :picker-options="pickerOptions">
    </el-date-picker>
</template>

<script>
export default {
    name: "NodesDateRange",
    model: {
        prop: 'dateRange',
        event: 'dateRangeChange'
    },
    props:{
        dateRange:[]
    },
    watch: {
      dateRange(newVal){
          this.value = newVal;
      }
    },
    data() {
        return {
            value:this.dateRange,
            pickerOptions: {
                shortcuts: [{
                    text: '最近一周',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 7);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近一个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 30);
                        picker.$emit('pick', [start, end]);
                    }
                }, {
                    text: '最近三个月',
                    onClick(picker) {
                        const end = new Date();
                        const start = new Date();
                        start.setTime(start.getTime() - 3600 * 1000 * 24 * 90);
                        picker.$emit('pick', [start, end]);
                    }
                }]
            }
        }
    },
    methods:{
        onChange(val){
            this.$emit('dateRangeChange',val);
        }
    }
}
</script>

<style scoped>

</style>
