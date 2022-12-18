<template>
  <el-container direction="horizonal" v-loading="loading">
    <el-container id="sidePane"></el-container>
    <el-container direction="vertical">
      <el-row class="tools">
        <el-button icon="el-icon-finished" @click="sidePaneVisual"
          >{{ openCloseSide }}事件列表</el-button
        >
        <el-button icon="el-icon-document-checked" @click="handleEventDealer()"
          >智能事件识别</el-button
        >
        <el-button icon="el-icon-microphone" @click="recoderVisible = true"
          >语音输入</el-button
        >
        <el-upload
          class="openLocal"
          action="#"
          :show-file-list="false"
          :http-request="httpRequestLocalFile"
        >
          <el-button icon="el-icon-monitor">从本地打开</el-button>
        </el-upload>
        <el-button icon="el-icon-download" @click="handleSaveLocalDialog()"
          >保存到本地</el-button
        >
        <el-button icon="el-icon-paperclip" @click="log()" :disabled="!hasLogin"
          >从云端打开</el-button
        >
        <el-button icon="el-icon-upload" :disabled="!hasLogin"
          >上传到云端</el-button
        >
        <el-button icon="el-icon-share" :disabled="!hasLogin">分享</el-button>
      </el-row>
      <!-- <el-button @click="formtoHtml">转换为Html样式</el-button> -->
      <div class="textEditor">
        <vue-html5-editor
          :content="ruleForm.content"
          v-model="ruleForm.content"
          show-module-name="true"
          @change="updateData"
        ></vue-html5-editor>
      </div>
      <el-dialog
        title="语音识别"
        :visible.sync="recoderVisible"
        width="30%"
        :modal="false"
        :show-close="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
        @open="handleRecoderOpen()"
        @close="handleRecoderClose()"
      >
        <el-button
          :icon="recoderStatsPic"
          @click="handleRecode()"
          :disabled="recoded"
          >{{ recorderStats }}录音</el-button
        >
        <el-button
          icon="el-icon-headset"
          :disabled="!recoded"
          @click="handleRecoderPlay()"
          >试听</el-button
        >
        <el-button
          icon="el-icon-refresh"
          :disabled="!recoded"
          @click="handleRecognizer()"
          >转换</el-button
        >
        <el-button icon="el-icon-close" @click="handleRecoderExit()"
          >取消</el-button
        >
      </el-dialog>
      <el-dialog
        title="保存到本地"
        :visible.sync="saveLocalVisible"
        width="30%"
        :modal="false"
        :destroy-on-close="true"
        :close-on-click-modal="false"
      >
        <el-form
          :model="saveLocalruleForm"
          :rules="saveLocalrules"
          ref="saveLocalruleForm"
          class="demo-ruleForm"
        >
          <el-form-item label="" prop="localFileName"
            ><el-input
              class="saveDialogInput"
              v-model="saveLocalruleForm.localFileName"
              auto-complete="off"
              placeholder="请输入文件名"
            ></el-input
          ></el-form-item>
          <el-button icon="el-icon-download" @click="handleSaveLocal()"
            >保存</el-button
          >
        </el-form>
      </el-dialog>
    </el-container>
  </el-container>
</template>
<script>
import Recorder from "js-audio-recorder";
import "../components/recoder.js";
export default {
  name: "NotepadView",
  components: {},
  data() {
    return {
      loading: false,
      hasLogin: false,
      openCloseSide: "关闭",
      recoderVisible: false,
      recorderStats: "开始",
      recoderStatsPic: "el-icon-mic",
      recoded: false,
      saveLocalVisible: false,
      ruleForm: {
        content: "",
      },
      saveLocalruleForm: {
        localFileName: "",
      },
      saveLocalrules: {
        localFileName: [
          { required: true, message: "请输入文件名", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    updateData: function (data) {
      this.ruleForm.content = data;
      data = this.ruleForm.content;
    },
    log() {
      console.log(this.ruleForm.content);
    },
    formtoHtml() {
      document.execCommand(""); //改进之处：可以转换为html文档
    },
    sidePaneVisual() {
      //开启/关闭侧边栏
      let sidePaneClass = document.getElementById("sidePane");
      if (sidePaneClass.getAttribute("style")) {
        sidePaneClass.removeAttribute("style");
        this.openCloseSide = "关闭";
      } else {
        sidePaneClass.setAttribute("style", "display: none!important;");
        this.openCloseSide = "打开";
      }
    },
    handleRecoderOpen() {
      //console.log("打开录音窗口");
      this.recorderStats = "开始";
      this.recoderStatsPic = "el-icon-mic";
      this.recoded = false;
    },
    handleRecoderClose() {
      //console.log("销毁实例");
      this.recorder.destroy(); // 毁实例
      this.timer = null;
    },
    handleRecode() {
      if (this.recorderStats == "开始") {
        this.recorderStats = "结束";
        this.recoderStatsPic = "el-icon-check";
        this.recorder = new Recorder({
          sampleBits: 8, // 采样位数，支持 8 或 16，默认是16
          sampleRate: 11025, // 采样率，支持 11025、16000、22050、24000、44100、48000，根据浏览器默认值，我的chrome是48000
          numChannels: 1, // 声道，支持 1 或 2， 默认是1
        });
        Recorder.getPermission().then(
          () => {
            //console.log("开始录音");
            this.recorder.start(); // 开始录音
          },
          (error) => {
            this.$message({
              message: "请先允许该网页使用麦克风",
              type: "info",
            });
            console.log(`${error.name} : ${error.message}`);
          }
        );
      } else if (this.recorderStats == "结束") {
        this.recorderStats = "完成";
        this.recoded = true;
        //console.log("停止录音");
        this.recorder.stop();
      }
    },
    handleRecoderPlay() {
      this.recorder.play();
    },
    handleRecognizer() {
      //console.log("开始转换");
      if (this.recorder == null || this.recorder.duration === 0) {
        this.$message({
          message: "请先录音",
          type: "error",
        });
        return false;
      }
      this.recorder.pause(); // 暂停录音
      this.timer = null;
      //console.log('上传录音')// 上传录音

      const formData = new FormData();
      const blob = this.recorder.getWAVBlob(); // 获取wav格式音频数据
      // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
      const newbolb = new Blob([blob], { type: "audio/wav" });
      const fileOfBlob = new File([newbolb], new Date().getTime() + ".wav");
      formData.append("file", fileOfBlob);
      //const url = window.URL.createObjectURL(fileOfBlob)
      //this.src = url
      //注释内容：将录音保存到前端服务器上
      this.$axios.post("ssm/recognize", formData).then((resp) => {
        console.log(resp);
      });

      //this.recorder.destroy();
      // const axios = require('axios')
      // axios.post(url, formData).then(res => {
      //   console.log(res.data.data[0].url)
      // })
    },
    handleRecoderExit() {
      this.handleRecoderClose();
      this.recoderVisible = false;
    },
    handleSaveLocalDialog() {
      if (this.ruleForm.content == ""||this.ruleForm.content == "<br>") {
        this.$message({
          message: "没有编写内容，保存失败",
          type: "error",
        });
        return;
      }
      this.saveLocalVisible = true;
    },
    handleSaveLocal() {
      this.$refs.saveLocalruleForm.validate((valid) => {
        if (valid) {
          console.log("valid");
          let fileName = this.saveLocalruleForm.localFileName;
          const formData = new FormData();
          const blob = this.ruleForm.content; // 获取html格式文本数据
          // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
          const newbolb = new Blob([blob], { type: "text/html" });
          const fileOfBlob = new File([newbolb], fileName + ".html");
          formData.append("file", fileOfBlob);
          let url = window.URL.createObjectURL(fileOfBlob);
          let a = document.createElement("a");
          a.style.display = "none";
          a.href = url;
          a.download = fileOfBlob.name;
          document.body.appendChild(a);
          a.click();
          document.body.removeChild(a);
        }
      });
    },
    // handleChange(file, fileList) {
    //   let localFile = file;
    //   const formData = new FormData();
    //   formData.append(file.name,file);
    //   console.log(file);
    // },
    httpRequestLocalFile(data) {
      //console.log(data.file);
      this.$confirm("此操作将覆盖原记事本数据, 是否继续?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          this.loading = true;
          let reader = new FileReader();
          const theThis = this;
          reader.readAsText(data.file);
          reader.onload = function () {
            theThis.ruleForm.content = this.result;
          };
          this.loading = false;
          this.$message({
            type: "success",
            message: "读取本地文件成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消读取本地文件",
          });
        });
    },handleEventDealer(){
      this.loading = true;
      const formData = new FormData();
      const blob = this.ruleForm.content; // 获取html格式文本数据
          // 此处获取到blob对象后需要设置fileName满足当前项目上传需求，其它项目可直接传把blob作为file塞入formData
      const newbolb = new Blob([blob], { type: "text/html" });
      const fileOfBlob = new File([newbolb], new Date().getTime());
      formData.append("file", fileOfBlob);
      this.$axios.post("ssm/event", formData).then((resp) => {
        console.log(resp.data);
      });
      this.loading = false;
    },
  },
  created() {
    this.recorder = new Recorder({
      sampleBits: 8, // 采样位数，支持 8 或 16，默认是16
      sampleRate: 11025, // 采样率，支持 11025、16000、22050、24000、44100、48000，根据浏览器默认值，我的chrome是48000
      numChannels: 1, // 声道，支持 1 或 2， 默认是1
    });
    if (this.$store.getters.getUser.username) {
      this.hasLogin = true;
    } else {
      this.hasLogin = false;
    }
  },
};
</script>
<style>
.tools {
  margin: 0 0 20px 0;
}
#sidePane {
  background-color: powderblue;
  height: auto;
  margin: 0 10px 0 0;
  border-radius: 5px;
}
/* .saveDialogTitle {
  font: 17px "Microsoft Yahei";
  margin: 0 0 20px 10px;
} */
.saveDialogInput {
  margin: 0 0 10px 0;
}
.openLocal {
  display: inline;
  margin: 0 10px;
}
</style>