<template>
    <div slot="reference">
      <el-badge :value="this.msgNum"  v-if="this.msgNum>0" >
        <i class="el-icon-bell" @click="msgList"></i>
      </el-badge>
        <el-badge   v-if="this.msgNum<=0" >
            <i class="el-icon-bell" @click="msgList"></i>
        </el-badge>
    </div>
</template>

<script>


import {getLogMsgCount} from "@/api/wms/log/log";

export default {
  name: "top-notice",
  data () {
    return {
        msgNum:0,
    }
  },
  created () {
    this.getMsgNum(this);
      this.getMsgNumByTime();
  },
  methods: {
      msgList(){
          this.$router.push({
              name: '消息列表'
          });
      },
      getMsgNum(that){
          getLogMsgCount().then((res) => {
              that.msgNum  = res.data.data;
          });
      },
      getMsgNumByTime () {
          const that = this;
           setInterval(function(){
               that.getMsgNum(that)
            }, 60 * 1000);
    },
  }
};
</script>

<style lang="scss" scoped>
</style>

