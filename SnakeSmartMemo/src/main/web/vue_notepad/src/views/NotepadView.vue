<template>
  <el-container direction="horizonal">
    <el-container id="sidePane"></el-container>
    <el-container direction="vertical">
    <el-row class="tools">
      <el-button icon="el-icon-finished" @click="sidePaneVisual">{{openCloseSide}}事件列表</el-button>
      <el-button icon="el-icon-microphone" @click="log()">语音输入</el-button>
      <el-button icon="el-icon-download" @click="log()">保存到本地</el-button>
      <el-button icon="el-icon-document-checked" @click="log()">智能事件识别</el-button>
      <el-button icon="el-icon-upload">上传到云端</el-button>
    </el-row>
    <!-- <el-button @click="formtoHtml">转换为Html样式</el-button> -->
    <div class="textEditor">
      <vue-html5-editor
      content=""
        v-model="ruleForm.content"
        height=600
        show-module-name="true"
        @change="updateData"
      ></vue-html5-editor>
    </div>
  </el-container>
  
  </el-container>
  
</template>
<script>
export default {
  name: "NotepadView",
  components: {},
  data() {
    return {
      openCloseSide:"关闭",
      ruleForm: {
        content: "hello",
      },
    };
  },
  methods: {
    updateData: function (data) {
      this.ruleForm.content = data;
      data = this.ruleForm.content;
    },
    log(){
      console.log(this.ruleForm.content);
    },
    formtoHtml(){
      document.execCommand("") //改进之处：可以转换为html文档
    },
    sidePaneVisual(){//开启/关闭侧边栏
      let sidePaneClass = document.getElementById('sidePane');
      if(sidePaneClass.getAttribute("style")){
        sidePaneClass.removeAttribute("style");
        this.openCloseSide="关闭";
      }else{
        sidePaneClass.setAttribute("style","display: none!important;");
        this.openCloseSide="打开";
      }
      
    }
  },
};
</script>
<style>
.tools {
  margin: 0 0 20px 0;
}
#sidePane {
  background-color:powderblue;
  height:auto;
  margin: 0 10px 0 0;
  border-radius: 5px;
}
</style>